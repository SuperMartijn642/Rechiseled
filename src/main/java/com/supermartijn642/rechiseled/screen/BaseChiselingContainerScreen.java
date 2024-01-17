package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BaseContainerWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipe;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.packet.PacketToggleConnecting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Created 23/12/2021 by SuperMartijn642
 */
public class BaseChiselingContainerScreen<T extends BaseChiselingContainer> extends BaseContainerWidget<T> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("rechiseled", "textures/screen/chiseling_background.png");

    /**
     * 0 - 1 block
     * 1 - row of 3 blocks
     * 2 - 3x3 blocks
     */
    public static int previewMode = 0;

    private final Component title;
    private ChiselAllWidget chiselAllWidget;

    private ScrollWidget scrollWidget;

    private final int numColumns = 4;

    private final int numRows = 5;

    private int numScrolls;

    private int currentScroll = 0;

    private boolean uiIsDirty = true;

    public BaseChiselingContainerScreen(Component title){
        super(0, 0, 222, 226);
        this.title = title;
    }

    @Override
    protected void addWidgets(){
        this.scrollWidget = new ScrollWidget(96, 17, 14, 110, this::scrollChanged);
        this.addWidget(this.scrollWidget);

        this.container.onUiDirtied = this::uiDirtied;

        int offsetX = 9;
        int offsetY = 17;
        int entryWidth = 20;
        int entryHeight = 22;
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int index = row * numColumns + column;
                int x = offsetX + entryWidth * column;
                int y = offsetY + entryHeight * row;

                EntryButtonWidget entryWidget = new EntryButtonWidget(x, y, entryWidth, entryHeight,
                        () -> this.getEntry((numColumns * currentScroll) + index),
                        () -> this.container.currentEntry,
                        () -> this.selectEntry((numColumns * currentScroll) + index),
                        () -> this.container.connecting);

                this.addWidget(entryWidget);
            }
        }

        this.addWidget(new EntryPreviewWidget(117, 17, 68, 69, () -> {
            ChiselingEntry entry = this.container.currentEntry;
            if(entry == null)
                return null;
            return (this.container.connecting && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
        }, () -> previewMode));
        Supplier<Boolean> enablePreviewButtons = () -> {
            ChiselingEntry entry = this.container.currentEntry;
            if(entry == null)
                return false;
            Item currentItem = (this.container.connecting && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
            return currentItem instanceof BlockItem;
        };
        this.addWidget(new PreviewModeButtonWidget(193, 18, 19, 21, 2, () -> previewMode, enablePreviewButtons, () -> previewMode = 2));
        this.addWidget(new PreviewModeButtonWidget(193, 41, 19, 21, 1, () -> previewMode, enablePreviewButtons, () -> previewMode = 1));
        this.addWidget(new PreviewModeButtonWidget(193, 64, 19, 21, 0, () -> previewMode, enablePreviewButtons, () -> previewMode = 0));
        this.addWidget(new ConnectingToggleWidget(193, 99, 19, 21, () -> this.container.connecting, () -> this.container.currentEntry, this::toggleConnecting));
        this.chiselAllWidget = this.addWidget(new ChiselAllWidget(127, 99, 19, 21, () -> this.container.currentEntry, this::chiselAll));
    }

    @Override
    public Component getNarrationMessage(){
        return this.title;
    }

    @Override
    public void renderBackground(WidgetRenderContext context, int mouseX, int mouseY){
        ScreenUtils.bindTexture(BACKGROUND);
        ScreenUtils.drawTexture(context.poseStack(), 0, 0, this.width, this.height);
        super.renderBackground(context, mouseX, mouseY);
    }

    @Override
    public void renderForeground(WidgetRenderContext context, int mouseX, int mouseY){
        // Render chisel all slot overlays
        if(this.container.currentRecipe != null && this.chiselAllWidget != null && this.chiselAllWidget.isFocused()){
            for(int index = 1; index < this.container.slots.size(); index++){
                Slot slot = this.container.getSlot(index);
                ItemStack stack = slot.getItem();

                for(ChiselingEntry entry : this.container.currentRecipe.getEntries()){
                    if((!stack.hasTag() || stack.getTag().isEmpty())
                        && ((entry.hasConnectingItem() && stack.getItem() == entry.getConnectingItem())
                        || (entry.hasRegularItem() && stack.getItem() == entry.getRegularItem()))){
                        ScreenUtils.fillRect(context.poseStack(), slot.x, slot.y, 16, 16, 0, 20, 100, 0.5f);
                    }
                }
            }
        }

        super.renderForeground(context, mouseX, mouseY);
        ScreenUtils.drawString(context.poseStack(), ClientUtils.getPlayer().getInventory().getName(), 31, 133);
    }

    @Override
    public boolean mouseScrolled(int mouseX, int mouseY, double scrollAmount, boolean hasBeenHandled){
        if(mouseX >= 8 && mouseX <= 110 && mouseY >= 16 && mouseY <= 127) {
            this.currentScroll -= (int)scrollAmount;

            clampCurrentScroll();

            if (this.scrollWidget != null)
                this.scrollWidget.setScrollRatio(this.numScrolls == 0 ? 0f : (float) this.currentScroll / this.numScrolls);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount, hasBeenHandled);
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY) {
        super.render(context, mouseX, mouseY);

        if(uiIsDirty){
            updateScrollData(this.container.currentEntry);
            uiIsDirty = false;
        }
    }

    private ChiselingEntry getEntry(int index){
        ChiselingRecipe recipe = this.container.currentRecipe;
        if(recipe == null)
            return null;
        return index >= 0 && index < recipe.getEntries().size() ? recipe.getEntries().get(index) : null;
    }

    private void selectEntry(int index){
        Rechiseled.CHANNEL.sendToServer(new PacketSelectEntry(index));
    }

    private void toggleConnecting(){
        Rechiseled.CHANNEL.sendToServer(new PacketToggleConnecting());
    }

    private void chiselAll(){
        Rechiseled.CHANNEL.sendToServer(new PacketChiselAll());
    }

    private void scrollChanged(){
        this.currentScroll = (int)(this.numScrolls * this.scrollWidget.getScrollRatio());
        clampCurrentScroll();
    }

    private void uiDirtied(){
        uiIsDirty = true;
    }

    private void updateScrollData(ChiselingEntry entry){
        boolean hasRecipe = this.container.currentRecipe != null;
        int numRecipes = hasRecipe ? this.container.currentRecipe.getEntries().size() : 0;
        if(this.container.currentRecipe == null) {
            numScrolls = 0;
        }else{
            numScrolls = (int) Math.ceil((double) numRecipes / numColumns) - numRows;
            if(numScrolls < 0)
                numScrolls = 0;
        }

        if(hasRecipe){
            var entries = container.currentRecipe.getEntries();
            if(entry != null && entries.contains(entry)){
                int indexOfItem = entries.indexOf(entry);
                int itemRow = (int) Math.ceil((double) (indexOfItem + 1) / this.numColumns);

                this.currentScroll = itemRow - numRows;

                clampCurrentScroll();
            }
        }else{
            this.currentScroll = 0;
        }

        if(this.scrollWidget != null){
            this.scrollWidget.setIntSnapMax(this.numScrolls);
            this.scrollWidget.setIsActive(this.numScrolls > 0);
            this.scrollWidget.setScrollRatio(this.numScrolls == 0f ? 0f : (float) this.currentScroll / this.numScrolls);
        }
    }

    private void clampCurrentScroll(){
        if(this.currentScroll < 0)
            this.currentScroll = 0;
        if(this.currentScroll > this.numScrolls)
            this.currentScroll = this.numScrolls;
    }
}
