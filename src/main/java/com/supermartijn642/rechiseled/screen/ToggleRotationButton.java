package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.BooleanSupplier;

/**
 * Created 20/04/2023 by SuperMartijn642
 */
public class ToggleRotationButton extends AbstractButtonWidget {

    public static final Identifier TEXTURE = Rechiseled.identifier("screen/rotation_icon");

    private final BooleanSupplier rotating;
    private final BooleanSupplier hasItem;

    public ToggleRotationButton(int x, int y, int width, int height, BooleanSupplier rotating, Runnable onPress, BooleanSupplier hasItem){
        super(x, y, width, height, onPress);
        this.rotating = rotating;
        this.hasItem = hasItem;
    }

    @Override
    protected boolean isClickable(){
        return this.hasItem.getAsBoolean();
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(TEXTURE, this.x, this.y, this.width, this.height, p -> p.uv(this.rotating.getAsBoolean() ? 0 : 0.5f, this.isFocused() && this.hasItem.getAsBoolean() ? 0.5f : 0, 0.5f, 0.5f));
        super.render(context, graphics, mouseX, mouseY);
    }

    @Override
    public Component getNarrationMessage(){
        return null;
    }
}
