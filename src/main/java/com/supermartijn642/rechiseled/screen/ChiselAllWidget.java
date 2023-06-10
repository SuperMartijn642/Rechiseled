package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class ChiselAllWidget extends AbstractButtonWidget {

    private static final ResourceLocation GREY_BUTTONS = new ResourceLocation("rechiseled", "textures/screen/grey_buttons.png");
    private static final ResourceLocation CHISEL_TEXTURE = new ResourceLocation("rechiseled", "textures/item/chisel.png");

    private final Supplier<ChiselingEntry> currentEntry;

    public ChiselAllWidget(int x, int y, int width, int height, Supplier<ChiselingEntry> currentEntry, Runnable onPress){
        super(x, y, width, height, onPress);
        this.currentEntry = currentEntry;
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        if(this.currentEntry.get() != null)
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.chisel_all").get());
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        ChiselingEntry currentEntry = this.currentEntry.get();
        boolean hasEntry = currentEntry != null;

        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0, (hasEntry ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f);

        ScreenUtils.bindTexture(CHISEL_TEXTURE);
        ScreenUtils.drawTexture(context.poseStack(), this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
