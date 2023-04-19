package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Created 20/04/2023 by SuperMartijn642
 */
public class ToggleRotationButton extends AbstractButtonWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rechiseled", "textures/screen/rotation_icon.png");

    public static boolean rotate = true;

    public ToggleRotationButton(int x, int y, int width, int height){
        super(x, y, width, height, () -> rotate = !rotate);
    }

    @Override
    public void render(int mouseX, int mouseY){
        ScreenUtils.bindTexture(TEXTURE);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, rotate ? 0 : 0.5f, this.isFocused() ? 0.5f : 0, 0.5f, 0.5f);
        super.render(mouseX, mouseY);
    }

    @Override
    public ITextComponent getNarrationMessage(){
        return null;
    }
}
