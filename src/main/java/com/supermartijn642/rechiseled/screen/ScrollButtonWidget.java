package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ScrollButtonWidget extends AbstractButtonWidget {
    private static final ResourceLocation GREY_BUTTONS = ResourceLocation.fromNamespaceAndPath("rechiseled", "textures/screen/scroll_buttons.png");
    private final boolean up;
    public ScrollButtonWidget(int x, int y, int width, int height, boolean up, Runnable onPress) {
        super(x, y, width, height, onPress);
        this.up = up;
    }

    @Override
    public Component getNarrationMessage() {
        return TextComponents.translation("rechiseled.chiseling.preview").get();
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){

        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, (this.isFocused() ? 1 : 0) / 2f, (this.up ? 0 : 1) / 2f, 1 / 2f, 1 / 2f);

    }
}
