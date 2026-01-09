package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.screen.preview.ScreenItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 2/3/2021 by SuperMartijn642
 */
public class EntryButtonWidget extends AbstractButtonWidget {

    private static final ResourceLocation TEXTURE = Rechiseled.identifier("textures/screen/buttons.png");

    private final int anchorY;
    private final Supplier<DisplayEntry> entry;
    private final Supplier<DisplayEntry> selectedEntry;
    private final Supplier<Boolean> connecting;

    public EntryButtonWidget(int x, int y, int width, int height,
                             Supplier<DisplayEntry> entrySupplier,
                             Supplier<DisplayEntry> selectedEntrySupplier,
                             Runnable onClick,
                             Supplier<Boolean> connecting){
        super(x, y, width, height, onClick);
        this.anchorY = y;
        this.entry = entrySupplier;
        this.selectedEntry = selectedEntrySupplier;
        this.connecting = connecting;
    }

    public void setVerticalOffset(float offset){
        this.y = (int)(this.anchorY - offset * this.height);
    }

    @Override
    protected boolean isClickable(){
        return this.entry.get() != null && this.entry.get() != this.selectedEntry.get();
    }

    @Override
    public Component getNarrationMessage(){
        DisplayEntry display = this.entry.get();
        if(display == null)
            return null;
        Item item = display.getItem(this.connecting.get());
        return TextComponents.translation("rechiseled.chiseling.select_block", TextComponents.item(item).get()).get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        DisplayEntry display = this.entry.get();
        if(display != null)
            tooltips.accept(TextComponents.item(display.getItem(this.connecting.get())).get());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY){
        DisplayEntry display = this.entry.get();

        boolean hasEntry = display != null;
        boolean selected = hasEntry && this.selectedEntry.get() == display;
        boolean hasCorrectItem = hasEntry && display.hasItem(this.connecting.get());

        ScreenUtils.bindTexture(TEXTURE);
        ScreenUtils.drawTexture(poseStack, this.x, this.y, this.width, this.height, 0, (selected ? 1 : hasEntry ? hasCorrectItem ? this.isFocused() ? 2 : 0 : this.isFocused() ? 4 : 3 : 0) / 5f, 1, 1 / 5f);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY){
        DisplayEntry display = this.entry.get();
        if(display != null){
            Item item = display.getItem(this.connecting.get());
            ScreenItemRenderer.drawItem(item, this.x + this.width / 2d, this.y + this.height / 2d, (this.width - 2) * 1.416, 0, 0, false);
        }
    }
}
