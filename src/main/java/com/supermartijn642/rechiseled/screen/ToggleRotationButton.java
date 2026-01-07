package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 20/04/2023 by SuperMartijn642
 */
public class ToggleRotationButton extends AbstractButtonWidget {

    public static final ResourceLocation TEXTURE = Rechiseled.identifier("screen/rotation_icon");

    public static boolean rotate = true;

    public ToggleRotationButton(int x, int y, int width, int height){
        super(x, y, width, height, () -> rotate = !rotate);
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(TEXTURE, this.x, this.y, this.width, this.height, p -> p.uv(rotate ? 0 : 0.5f, this.isFocused() ? 0.5f : 0, 0.5f, 0.5f));
        super.render(context, graphics, mouseX, mouseY);
    }

    @Override
    public Component getNarrationMessage(){
        return null;
    }
}
