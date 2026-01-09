package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class PreviewModeButtonWidget extends AbstractButtonWidget {

    public static final Identifier GREY_BUTTONS = Rechiseled.identifier("screen/grey_buttons");

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
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        boolean selected = this.mode == this.currentMode.get();
        graphics.submitSprite(GREY_BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(0, (!this.isClickable() ? 1 : this.isFocused() ? 2 : 0) / 3f, 1, 1 / 3f));
        graphics.submitSprite(this.mode.icon(selected), this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
