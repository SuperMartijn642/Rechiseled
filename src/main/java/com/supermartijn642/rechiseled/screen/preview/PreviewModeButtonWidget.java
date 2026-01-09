package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class PreviewModeButtonWidget extends AbstractButtonWidget {

    private static final ResourceLocation GREY_BUTTONS = Rechiseled.identifier("textures/screen/grey_buttons.png");

    private final PreviewMode mode;
    private final Supplier<PreviewMode> currentMode;
    private final Supplier<Boolean> enabled;

    public PreviewModeButtonWidget(int x, int y, int width, int height,
                                   PreviewMode mode,
                                   Supplier<PreviewMode> currentMode,
                                   Supplier<Boolean> enabled,
                                   Runnable onPress){
        super(x, y, width, height, onPress);
        this.mode = mode;
        this.currentMode = currentMode;
        this.enabled = enabled;
    }

    @Override
    protected boolean isClickable(){
        return this.enabled.get() && this.currentMode.get() != this.mode;
    }

    @Override
    public Component getNarrationMessage(){
        return TextComponents.translation("rechiseled.chiseling.preview").get();
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        boolean selected = this.mode == this.currentMode.get();
        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0, (!this.isClickable() ? 1 : this.isFocused() ? 2 : 0) / 3f, 1, 1 / 3f);
        ScreenUtils.bindTexture(this.mode.icon(selected));
        ScreenUtils.drawTexture(context.poseStack(), this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
