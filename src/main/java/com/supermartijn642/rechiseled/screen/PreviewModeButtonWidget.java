package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class PreviewModeButtonWidget extends AbstractButtonWidget {

    private static final ResourceLocation GREY_BUTTONS = new ResourceLocation("rechiseled", "textures/screen/grey_buttons.png");
    private static final ResourceLocation[][] ICONS = {
        {
            new ResourceLocation("rechiseled", "textures/screen/icon_1x1.png"),
            new ResourceLocation("rechiseled", "textures/screen/icon_1x1_grey.png")
        },
        {
            new ResourceLocation("rechiseled", "textures/screen/icon_3x1.png"),
            new ResourceLocation("rechiseled", "textures/screen/icon_3x1_grey.png")
        },
        {
            new ResourceLocation("rechiseled", "textures/screen/icon_3x3.png"),
            new ResourceLocation("rechiseled", "textures/screen/icon_3x3_grey.png")
        }
    };

    private final int mode;
    private final Supplier<Integer> currentMode;
    private final Supplier<Boolean> enabled;

    public PreviewModeButtonWidget(int x, int y, int width, int height, int mode, Supplier<Integer> currentMode, Supplier<Boolean> enabled, Runnable onPress){
        super(x, y, width, height, onPress);
        this.mode = mode;
        this.currentMode = currentMode;
        this.enabled = enabled;
    }

    @Override
    public ITextComponent getNarrationMessage(){
        return TextComponents.translation("rechiseled.chiseling.preview").get();
    }

    @Override
    public void render(int mouseX, int mouseY){
        int currentMode = this.currentMode.get();
        boolean selected = this.mode == currentMode;

        boolean enabled = this.enabled.get();
        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, 0, ((!enabled || selected) ? 1 : this.isFocused() ? 2 : 0) / 3f, 1, 1 / 3f);

        GlStateManager.enableAlpha();
        ScreenUtils.bindTexture(ICONS[this.mode][selected ? 1 : 0]);
        ScreenUtils.drawTexture(this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
