package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.AbstractButtonWidget;
import com.supermartijn642.core.gui.widget.IHoverTextWidget;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class ChiselAllWidget extends AbstractButtonWidget implements IHoverTextWidget {

    private static final ResourceLocation GREY_BUTTONS = new ResourceLocation("rechiseled", "textures/screen/grey_buttons.png");
    private static final ResourceLocation CHISEL_TEXTURE = new ResourceLocation("rechiseled", "textures/item/chisel.png");

    private final Supplier<ChiselingEntry> currentEntry;

    public ChiselAllWidget(int x, int y, int width, int height, Supplier<ChiselingEntry> currentEntry, Runnable onPress){
        super(x, y, width, height, onPress);
        this.currentEntry = currentEntry;
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return this.getHoverText();
    }

    @Override
    public ITextComponent getHoverText(){
        boolean hasEntry = this.currentEntry.get() != null;
        return hasEntry ? TextComponents.translation("rechiseled.chiseling.chisel_all").get() : null;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        ChiselingEntry currentEntry = this.currentEntry.get();
        boolean hasEntry = currentEntry != null;

        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, 0, (hasEntry ? this.hovered ? 2 : 0 : 1) / 3f, 1, 1 / 3f);

        GlStateManager._enableAlphaTest();
        ScreenUtils.bindTexture(CHISEL_TEXTURE);
        ScreenUtils.drawTexture(this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
