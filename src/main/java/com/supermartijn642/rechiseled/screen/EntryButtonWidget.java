package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.chiseling.ChiselingRecipeDatapackPlugin;
import com.supermartijn642.rechiseled.screen.preview.ScreenItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
        ItemWithWorth item = display == null ? null : display.getItem(this.connecting.get());
        if(item == null)
            return null;
        return TextComponents.translation("rechiseled.chiseling.select_block", TextComponents.item(item.item()).get()).get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        DisplayEntry display = this.entry.get();
        ItemWithWorth item = display == null ? null : display.getItem(this.connecting.get());
        if(item != null){
            tooltips.accept(TextComponents.item(item.item()).get());
            if(ClientUtils.getMinecraft().options.advancedItemTooltips){
                ResourceLocation recipe = display.entry().recipe();
                if(recipe != null)
                    tooltips.accept(TextComponents.translation("rechiseled.chiseling.entry.recipe", TextComponents.string(recipe.toString()).color(ChatFormatting.DARK_GRAY).get()).color(ChatFormatting.GRAY).get());
                ResourceLocation owner = display.entry().owner();
                if(!owner.equals(ChiselingRecipeDatapackPlugin.IDENTIFIER))
                    tooltips.accept(TextComponents.translation("rechiseled.chiseling.entry.owner", TextComponents.string(owner.toString()).color(ChatFormatting.DARK_GRAY).get()).color(ChatFormatting.GRAY).get());
            }
        }
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        DisplayEntry display = this.entry.get();

        boolean hasEntry = display != null;
        boolean selected = hasEntry && this.selectedEntry.get() == display;
        boolean hasCorrectItem = hasEntry && display.hasItem(this.connecting.get());

        ScreenUtils.bindTexture(TEXTURE);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0, (selected ? 1 : hasEntry ? hasCorrectItem ? this.isFocused() ? 2 : 0 : this.isFocused() ? 4 : 3 : 0) / 5f, 1, 1 / 5f);
    }

    @Override
    public void renderForeground(WidgetRenderContext context, int mouseX, int mouseY){
        DisplayEntry display = this.entry.get();
        ItemWithWorth item = display == null ? null : display.getItem(this.connecting.get());
        if(display != null)
            ScreenItemRenderer.drawItem(context.poseStack(), item.item(), this.x + this.width / 2d, this.y + this.height / 2d, this.width - 4, 0, 0, false);
    }
}
