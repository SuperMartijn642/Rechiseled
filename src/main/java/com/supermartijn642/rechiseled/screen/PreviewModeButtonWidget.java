package com.supermartijn642.rechiseled.screen;

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
    public static final Identifier[][] ICONS = {
        {
            Rechiseled.identifier("screen/icon_1x1"),
            Rechiseled.identifier("screen/icon_1x1_grey")
        },
        {
            Rechiseled.identifier("screen/icon_3x1"),
            Rechiseled.identifier("screen/icon_3x1_grey")
        },
        {
            Rechiseled.identifier("screen/icon_3x3"),
            Rechiseled.identifier("screen/icon_3x3_grey")
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
    public Component getNarrationMessage(){
        return TextComponents.translation("rechiseled.chiseling.preview").get();
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        int currentMode = this.currentMode.get();
        boolean selected = this.mode == currentMode;

        boolean enabled = this.enabled.get();
        graphics.submitSprite(GREY_BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(0, ((!enabled || selected) ? 1 : this.isFocused() ? 2 : 0) / 3f, 1, 1 / 3f));

        graphics.submitSprite(ICONS[this.mode][selected ? 1 : 0], this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
