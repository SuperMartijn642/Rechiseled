package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
    public ITextComponent getNarrationMessage(){
        Holder<ITextComponent> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    protected void getTooltips(Consumer<ITextComponent> tooltips){
        if(this.currentEntry.get() != null)
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.chisel_all").get());
    }

    @Override
    public void render(int mouseX, int mouseY){
        ChiselingEntry currentEntry = this.currentEntry.get();
        boolean hasEntry = currentEntry != null;

        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, 0, (hasEntry ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f);

        GlStateManager.enableAlpha();
        ScreenUtils.bindTexture(CHISEL_TEXTURE);
        ScreenUtils.drawTexture(this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
