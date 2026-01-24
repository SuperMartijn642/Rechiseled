package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BaseContainerWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.ScissorWidget;
import com.supermartijn642.core.gui.widget.premade.ScrollbarWidget;
import com.supermartijn642.core.gui.widget.premade.TextFieldWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.*;
import com.supermartijn642.rechiseled.packet.PacketChiselAll;
import com.supermartijn642.rechiseled.packet.PacketSelectEntry;
import com.supermartijn642.rechiseled.screen.preview.EntryPreviewWidget;
import com.supermartijn642.rechiseled.screen.preview.PreviewMode;
import com.supermartijn642.rechiseled.screen.preview.PreviewModeButtonWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 23/12/2021 by SuperMartijn642
 */
public class BaseChiselingContainerScreen<T extends BaseChiselingContainer> extends BaseContainerWidget<T> {

    private static final ResourceLocation BACKGROUND = Rechiseled.identifier("textures/screen/chiseling_background.png");
    private static final int OPTION_ROWS = 5, OPTION_COLUMNS = 6;

    private static PreviewMode previewMode = PreviewMode.SINGLE;
    private static String searchText = "";
    private static String formattedSearchText = "";
    private static boolean showBlocks = true, showStairs = false, showSlabs = false, showNonConnecting = true;
    private static boolean filtersMatchShape = true;

    private final Component title;
    private ChiselAllWidget chiselAllWidget;
    private final List<EntryButtonWidget> entryButtons = new ArrayList<>();
    private TextFieldWidget searchField;
    private FilterOptionsWidget filterOptionsWidget;

    private DisplayEntry lastContainerEntry;
    private ChiselingRecipe recipe;
    private final List<DisplayEntry> allEntries = new ArrayList<>();
    private final List<DisplayEntry> visibleEntries = new ArrayList<>();
    private DisplayEntry selectedEntry;
    private boolean connecting = false;
    private float scrollOffset;
    private int scrollIndexOffset;

    public BaseChiselingContainerScreen(Component title){
        super(0, 0, 260, 243);
        this.title = title;
    }

    @Override
    protected void addWidgets(){
        // Entry buttons
        ScissorWidget scissorWidget = this.addWidget(ScissorWidget.create(9, 34, 120, 110));
        for(int row = 0; row < OPTION_ROWS + 1; row++){
            for(int column = 0; column < OPTION_COLUMNS; column++){
                int index = row * OPTION_COLUMNS + column;
                int x = 9 + 20 * column;
                int y = 34 + 22 * row;

                EntryButtonWidget button = new EntryButtonWidget(
                    x, y, 20, 22,
                    () -> this.getDisplayEntry(index),
                    () -> this.selectedEntry,
                    () -> this.selectDisplayEntry(this.getDisplayEntry(index)),
                    () -> this.connecting
                );
                scissorWidget.addWidget(button);
                this.entryButtons.add(button);
            }
        }
        this.addWidget(ScrollbarWidget.builder(110)
            .position(132, 34)
            .scrollValue(
                () -> this.scrollOffset,
                () -> 0,
                () -> (int)Math.ceil((float)this.visibleEntries.size() / OPTION_COLUMNS) - OPTION_ROWS
            )
            .onChange((oldValue, newValue) -> this.setScrollOffset((float)newValue))
            .smoothScrolling()
            .scrollWheelValueChange(0)
            .background(null)
            .build()
        );

        // Search
        this.searchField = this.addWidget(new TextFieldWidget(8, 18, 122, 12, "", 20, s -> {
            s = s.trim();
            if(!s.equals(searchText)){
                searchText = s;
                formattedSearchText = s.toLowerCase();
                this.updateDisplayEntries();
            }
        }));
        this.searchField.setSuggestion("Search");
        this.searchField.setTextSuppressed(searchText);
        this.searchField.setActive(false);
        this.filterOptionsWidget = this.addWidget(new FilterOptionsWidget(
            131, 18,
            () -> showBlocks, () -> showStairs, () -> showSlabs, () -> showNonConnecting,
            this::toggleShowBlocks, this::toggleShowStairs, this::toggleShowSlabs, this::toggleShowNonConnecting,
            () -> {
                showBlocks = showStairs = showSlabs = showNonConnecting = true;
                filtersMatchShape = false;
                this.updateDisplayEntries();
            }
        ));
        this.filterOptionsWidget.setActive(false);

        // Preview
        this.addWidget(new EntryPreviewWidget(155, 20, 68, 69, () -> {
            DisplayEntry display = this.selectedEntry;
            ItemWithWorth item = display == null ? null : display.getItem(this.connecting);
            return item == null ? null : item.item();
        }, () -> previewMode));
        Supplier<Boolean> enablePreviewButtons = () -> {
            DisplayEntry display = this.selectedEntry;
            ItemWithWorth item = display == null ? null : display.getItem(this.connecting);
            return item != null && item.item() instanceof BlockItem;
        };
        this.addWidget(new PreviewModeButtonWidget(227, 21, 19, 21, PreviewMode.PANEL, () -> previewMode, enablePreviewButtons, () -> previewMode = PreviewMode.PANEL));
        this.addWidget(new PreviewModeButtonWidget(227, 44, 19, 21, PreviewMode.ROW, () -> previewMode, enablePreviewButtons, () -> previewMode = PreviewMode.ROW));
        this.addWidget(new PreviewModeButtonWidget(227, 67, 19, 21, PreviewMode.SINGLE, () -> previewMode, enablePreviewButtons, () -> previewMode = PreviewMode.SINGLE));

        // Shape and connecting buttons
        this.addWidget(new ConnectingToggleWidget(229, 112, 15, 16, () -> this.connecting, () -> this.selectedEntry, this::toggleConnecting));
        this.addWidget(new ShapeSelectionWidget(210, 99, 12, 13, ChiselingBlockShape.BLOCK, () -> this.selectedEntry, () -> this.changeShape(ChiselingBlockShape.BLOCK)));
        this.addWidget(new ShapeSelectionWidget(210, 113, 12, 13, ChiselingBlockShape.STAIRS, () -> this.selectedEntry, () -> this.changeShape(ChiselingBlockShape.STAIRS)));
        this.addWidget(new ShapeSelectionWidget(210, 127, 12, 13, ChiselingBlockShape.SLAB, () -> this.selectedEntry, () -> this.changeShape(ChiselingBlockShape.SLAB)));

        // Chisel all
        this.chiselAllWidget = this.addWidget(new ChiselAllWidget(157, 109, 19, 21, () -> this.selectedEntry, this::chiselAll));
    }

    @Override
    public void update(){
        this.updateRecipe();
        super.update();
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
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        super.render(context, mouseX, mouseY);
        // Highlight blocks that can chiseled
        for(int index = 1; index < this.container.slots.size(); index++){
            Slot slot = this.container.getSlot(index);
            ItemStack stack = slot.getItem();
            //noinspection DataFlowIssue
            if(stack.isEmpty() || (stack.hasTag() && !stack.getTag().isEmpty()))
                continue;

            // Check if the stack is in the current recipe
            if(this.recipe != null && this.recipe.contains(stack.getItem()))
                ScreenUtils.fillRect(context.poseStack(), slot.x + 13, slot.y, 3, 3, 52 / 355f, 108 / 355f, 173 / 355f, 0.5f);
            else if(ChiselingRecipeManager.get(true).getRecipeForItem(stack.getItem()) != null)
                ScreenUtils.fillRect(context.poseStack(), slot.x + 13, slot.y, 3, 3, 1, 207 / 355f, 74 / 355f, 0.5f);
        }
    }

    @Override
    public void renderForeground(WidgetRenderContext context, int mouseX, int mouseY){
        // Render chisel all slot overlays
        this.chiselAllWidget.chiselableItems = 0;
        if(this.recipe != null && this.chiselAllWidget.isFocused()){
            boolean allShapes = Screen.hasShiftDown();
            int items = 0;
            for(int index = 1; index < this.container.slots.size(); index++){
                Slot slot = this.container.getSlot(index);
                ItemStack stack = slot.getItem();
                //noinspection DataFlowIssue
                if(stack.hasTag() && !stack.getTag().isEmpty())
                    continue;
                if(stack.getItem() == this.selectedEntry.getItem(this.connecting).item())
                    continue;

                // Check if the stack is chiselable
                boolean isChiselable;
                if(allShapes)
                    isChiselable = this.recipe.contains(stack.getItem());
                else{
                    isChiselable = false;
                    ChiselingBlockShape shape = this.selectedEntry.shape();
                    for(ChiselingEntry entry : this.recipe.entries()){
                        if((entry.hasRegularItem(shape) && entry.getRegularItem(shape).item() == stack.getItem())
                            || (entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == stack.getItem())){
                            isChiselable = true;
                            break;
                        }
                    }
                }
                if(!isChiselable)
                    continue;
                items += stack.getCount();

                // Render overlay
                ScreenUtils.fillRect(context.poseStack(), slot.x, slot.y, 16, 16, 0, 20, 100, 0.5f);
            }
            this.chiselAllWidget.chiselableItems = items;
        }

        super.renderForeground(context, mouseX, mouseY);
        ScreenUtils.drawCenteredString(context.poseStack(), this.title, 51, 2, ScreenUtils.ACTIVE_TEXT_COLOR);
        ScreenUtils.drawString(context.poseStack(), ClientUtils.getPlayer().getInventory().getName(), 50, 150);
    }

    private void updateDisplayEntries(){
        this.visibleEntries.clear();
        for(DisplayEntry entry : this.allEntries){
            if(this.matchesFilters(entry))
                this.visibleEntries.add(entry);
        }
        this.setScrollOffset(this.scrollOffset);
    }

    private void updateRecipe(){
        if(this.container.currentRecipe == null && this.recipe == null)
            return;
        if(this.container.currentRecipe != null && this.recipe == this.container.currentRecipe){
            if(this.lastContainerEntry != null && this.lastContainerEntry.entry() == this.container.currentEntry && this.lastContainerEntry.shape() == this.container.shape)
                return;
            if(this.selectedEntry != null && this.selectedEntry.entry() == this.container.currentEntry && this.selectedEntry.shape() == this.container.shape){
                this.lastContainerEntry = new DisplayEntry(-1, this.container.currentEntry, this.container.shape);
                return;
            }
        }
        this.recipe = this.container.currentRecipe;
        this.lastContainerEntry = new DisplayEntry(-1, this.container.currentEntry, this.container.shape);

        // Reset everything
        this.allEntries.clear();
        this.selectedEntry = null;
        this.connecting = false;
        this.searchField.setTextSuppressed("");
        searchText = "";
        formattedSearchText = "";
        this.searchField.setActive(this.recipe != null);
        this.filterOptionsWidget.setActive(this.recipe != null);

        // Update for new recipe
        DisplayEntry matchingDisplay = null;
        if(this.recipe != null){
            Item currentItem = this.container.getCurrentStack().getItem();
            for(ChiselingBlockShape shape : ChiselingBlockShape.values()){
                for(int i = 0; i < this.recipe.entries().size(); i++){
                    ChiselingEntry entry = this.recipe.entries().get(i);
                    if(entry.hasShape(shape)){
                        DisplayEntry display = new DisplayEntry(i, entry, shape);
                        this.allEntries.add(display);
                        if(matchingDisplay == null && entry == this.container.currentEntry){
                            if(entry.hasRegularItem(shape) && entry.getRegularItem(shape).item() == currentItem){
                                matchingDisplay = display;
                                this.connecting = false;
                            }else if(entry.hasConnectingItem(shape) && entry.getConnectingItem(shape).item() == currentItem){
                                matchingDisplay = display;
                                this.connecting = true;
                            }
                        }
                    }
                }
            }
        }
        this.updateDisplayEntries();
        if(matchingDisplay == null)
            this.setScrollOffset(0);
        else
            this.selectDisplayEntry(matchingDisplay);
    }

    private boolean matchesFilters(DisplayEntry entry){
        if((!showBlocks && entry.shape() == ChiselingBlockShape.BLOCK)
            || (!showStairs && entry.shape() == ChiselingBlockShape.STAIRS)
            || (!showSlabs && entry.shape() == ChiselingBlockShape.SLAB)
            || (!showNonConnecting && !entry.hasItem(true)))
            return false;
        return (entry.hasItem(false) && this.doesItemMatchSearch(entry.getItem(false).item()))
            || (entry.hasItem(true) && this.doesItemMatchSearch(entry.getItem(true).item()));
    }

    private boolean doesItemMatchSearch(Item item){
        if(formattedSearchText.isEmpty())
            return true;

        boolean isModSearch = formattedSearchText.charAt(0) == '@';
        if(isModSearch){
            if(formattedSearchText.length() == 1)
                return true;
            ResourceLocation identifier = BuiltInRegistries.ITEM.getKey(item);
            if(identifier.getNamespace().toLowerCase().startsWith(formattedSearchText.substring(1)))
                return true;
            String modName = ModList.get().getModContainerById(identifier.getNamespace()).map(ModContainer::getModInfo).map(IModInfo::getDisplayName).orElse(null);
            return modName != null && modName.toLowerCase().startsWith(formattedSearchText);
        }

        String name = TextComponents.item(item).format();
        return name.toLowerCase().contains(formattedSearchText);
    }

    private DisplayEntry getDisplayEntry(int index){
        index += this.scrollIndexOffset * OPTION_COLUMNS;
        return index >= 0 && index < this.visibleEntries.size() ? this.visibleEntries.get(index) : null;
    }

    private void selectDisplayEntry(DisplayEntry entry){
        if(entry == null || this.selectedEntry == entry)
            return;
        boolean changedShape = this.selectedEntry == null || this.selectedEntry.shape() != entry.shape();
        boolean isSameRecipeEntry = this.selectedEntry != null && entry.entry() == this.selectedEntry.entry();
        this.selectedEntry = entry;
        if(!entry.hasItem(this.connecting))
            this.connecting = !this.connecting;
        if(this.container.currentEntry != entry.entry() || this.container.shape != entry.shape())
            Rechiseled.CHANNEL.sendToServer(new PacketSelectEntry(entry.entryIndex(), entry.shape(), this.connecting));
        // Update filters
        if(filtersMatchShape && changedShape){
            showBlocks = this.selectedEntry.shape() == ChiselingBlockShape.BLOCK;
            showStairs = this.selectedEntry.shape() == ChiselingBlockShape.STAIRS;
            showSlabs = this.selectedEntry.shape() == ChiselingBlockShape.SLAB;
            this.updateDisplayEntries();
        }
        if(!isSameRecipeEntry)
            this.updateFiltersMatchShape(entry.shape());
        // Scroll to entry
        int index = this.visibleEntries.indexOf(entry);
        if(index < 0)
            return;
        int row = index / OPTION_COLUMNS;
        if(row < this.scrollOffset || row + 1 > this.scrollIndexOffset + OPTION_ROWS)
            this.setScrollOffset(row - 2);
    }

    private void toggleConnecting(){
        if(this.selectedEntry == null || !this.selectedEntry.hasItem(!this.connecting))
            return;
        this.connecting = !this.connecting;
        Rechiseled.CHANNEL.sendToServer(new PacketSelectEntry(this.selectedEntry.entryIndex(), this.selectedEntry.shape(), this.connecting));
    }

    private void changeShape(ChiselingBlockShape shape){
        if(this.selectedEntry == null || this.selectedEntry.shape() == shape || !this.selectedEntry.entry().hasShape(shape))
            return;
        for(DisplayEntry display : this.allEntries){
            if(display.entry() == this.selectedEntry.entry() && display.shape() == shape){
                this.selectDisplayEntry(display);
                break;
            }
        }

        // If only one shape was being shown, change the shape filters along with the entry's shape
        shape = this.selectedEntry.shape();
        if(filtersMatchShape){
            showBlocks = shape == ChiselingBlockShape.BLOCK;
            showStairs = shape == ChiselingBlockShape.STAIRS;
            showSlabs = shape == ChiselingBlockShape.SLAB;
            this.updateDisplayEntries();
        }
    }

    private void toggleShowBlocks(){
        showBlocks = !showBlocks;
        this.updateFiltersMatchShape(this.selectedEntry.shape());
        this.updateDisplayEntries();
    }

    private void toggleShowStairs(){
        showStairs = !showStairs;
        this.updateFiltersMatchShape(this.selectedEntry.shape());
        this.updateDisplayEntries();
    }

    private void toggleShowSlabs(){
        showSlabs = !showSlabs;
        this.updateFiltersMatchShape(this.selectedEntry.shape());
        this.updateDisplayEntries();
    }

    private void toggleShowNonConnecting(){
        showNonConnecting = !showNonConnecting;
        this.updateDisplayEntries();
    }

    private void updateFiltersMatchShape(ChiselingBlockShape shape){
        filtersMatchShape = (shape == ChiselingBlockShape.BLOCK) == showBlocks
            && (shape == ChiselingBlockShape.STAIRS) == showStairs
            && (shape == ChiselingBlockShape.SLAB) == showSlabs;
    }

    private void chiselAll(){
        Rechiseled.CHANNEL.sendToServer(new PacketChiselAll(Screen.hasShiftDown()));
    }

    private void setScrollOffset(float offset){
        int rows = (int)Math.ceil((float)this.visibleEntries.size() / OPTION_COLUMNS);
        this.scrollOffset = Math.max(0, Math.min(rows - OPTION_ROWS, offset));
        this.scrollIndexOffset = (int)Math.floor(this.scrollOffset);
        for(EntryButtonWidget button : this.entryButtons)
            button.setVerticalOffset(this.scrollOffset % 1);
    }

    @Override
    public boolean mouseScrolled(int mouseX, int mouseY, double scrollAmount, boolean hasBeenHandled){
        if(!hasBeenHandled && mouseX > 8 && mouseX < 145 && mouseY > 33 && mouseY < 145){
            this.setScrollOffset(this.scrollOffset - (float)scrollAmount / 3);
            hasBeenHandled = true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount, hasBeenHandled);
    }
}
