package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.Widget;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Created 2/3/2021 by SuperMartijn642
 */
public class BlockButtonWidget extends Widget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rechiseled", "textures/screen/buttons.png");

    private final Supplier<ChiselingEntry> entry;
    private final Supplier<ChiselingEntry> selectedEntry;
    private final Runnable onClick;
    private final Supplier<Boolean> connecting;
    private final Supplier<Integer> guiLeft, guiTop;

    public BlockButtonWidget(int x, int y, int width, int height,
                             Supplier<ChiselingEntry> entrySupplier,
                             Supplier<ChiselingEntry> selectedEntrySupplier,
                             Runnable onClick,
                             Supplier<Boolean> connecting,
                             Supplier<Integer> guiLeft,
                             Supplier<Integer> guiTop){
        super(x, y, width, height);
        this.entry = entrySupplier;
        this.selectedEntry = selectedEntrySupplier;
        this.onClick = onClick;
        this.connecting = connecting;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }

    @Override
    protected Component getNarrationMessage(){
        ChiselingEntry entry = this.entry.get();
        if(entry == null)
            return null;
        Item item = (this.connecting.get() && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
        return TextComponents.translation("rechiseled.chiseling.select_block", TextComponents.item(item).get()).get();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks){
        ChiselingEntry entry = this.entry.get();

        boolean hasEntry = entry != null;
        boolean selected = hasEntry && this.selectedEntry.get() == entry;
        boolean hasCorrectItem = hasEntry && (this.connecting.get() ? entry.hasConnectingItem() : entry.hasRegularItem());

        ScreenUtils.bindTexture(TEXTURE);
        ScreenUtils.drawTexture(matrixStack, this.x, this.y, this.width, this.height, 0, (selected ? 1 : hasEntry ? hasCorrectItem ? this.hovered ? 2 : 0 : this.hovered ? 4 : 3 : 0) / 5f, 1, 1 / 5f);

        if(hasEntry){
            Item item = (this.connecting.get() && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
            if(item instanceof BlockItem){
                BlockCapture capture = new BlockCapture(((BlockItem)item).getBlock());
                ScreenBlockRenderer.drawBlock(capture, this.guiLeft.get() + this.x + this.width / 2d, this.guiTop.get() + this.y + this.height / 2d, this.width, 135, 40, true);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button){
        if(mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
            ChiselingEntry entry = this.entry.get();
            if(entry != null)
                this.onClick.run();
        }
    }
}
