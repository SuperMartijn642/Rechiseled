package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
public class ShapeSelectionWidget extends AbstractButtonWidget {

    public static final Identifier SMALL_GREY_BUTTONS = Rechiseled.identifier("screen/grey_buttons");
    public static final Identifier BLOCK_ICON = Rechiseled.identifier("screen/icon_1x1");
    public static final Identifier STAIRS_ICON = Rechiseled.identifier("screen/icon_stairs");
    public static final Identifier SLAB_ICON = Rechiseled.identifier("screen/icon_slab");

    private final ChiselingBlockShape shape;
    private final Supplier<DisplayEntry> currentEntry;
    private final Identifier icon;

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
    public void renderBackground(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        boolean canClick = this.isClickable();
        graphics.submitSprite(SMALL_GREY_BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(0, (canClick ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f));
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        DisplayEntry display = this.currentEntry.get();
        if(display != null && display.entry().hasShape(this.shape))
            graphics.submitSprite(this.icon, this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
