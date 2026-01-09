package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
public class ShapeSelectionWidget extends AbstractButtonWidget {

    private static final ResourceLocation SMALL_GREY_BUTTONS = Rechiseled.identifier("textures/screen/grey_buttons.png");
    private static final ResourceLocation BLOCK_ICON = Rechiseled.identifier("textures/screen/icon_1x1.png");
    private static final ResourceLocation STAIRS_ICON = Rechiseled.identifier("textures/screen/icon_stairs.png");
    private static final ResourceLocation SLAB_ICON = Rechiseled.identifier("textures/screen/icon_slab.png");

    private final ChiselingBlockShape shape;
    private final Supplier<DisplayEntry> currentEntry;
    private final ResourceLocation icon;

    public ShapeSelectionWidget(int x, int y, int width, int height, ChiselingBlockShape shape, Supplier<DisplayEntry> currentEntry, Runnable onPress){
        super(x, y, width, height, onPress);
        this.shape = shape;
        this.currentEntry = currentEntry;
        this.icon = shape == ChiselingBlockShape.BLOCK ? BLOCK_ICON
            : shape == ChiselingBlockShape.STAIRS ? STAIRS_ICON : SLAB_ICON;
    }

    @Override
    protected boolean isClickable(){
        DisplayEntry display = this.currentEntry.get();
        return display != null && this.shape != display.shape() && display.entry().hasShape(this.shape);
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        DisplayEntry display = this.currentEntry.get();
        if(display != null && display.entry().hasShape(this.shape))
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.select_shape", TextComponents.fromTextComponent(this.shape.translation()).color(ChatFormatting.GOLD).get()).get());
    }

    @Override
    public void renderBackground(WidgetRenderContext context, int mouseX, int mouseY){
        boolean canClick = this.isClickable();
        ScreenUtils.bindTexture(SMALL_GREY_BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0, (canClick ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f);
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        DisplayEntry display = this.currentEntry.get();
        if(display != null && display.entry().hasShape(this.shape)){
            ScreenUtils.bindTexture(this.icon);
            ScreenUtils.drawTexture(context.poseStack(), this.x + 1, this.y + 2, this.width - 2, this.height - 4);
        }
    }
}
