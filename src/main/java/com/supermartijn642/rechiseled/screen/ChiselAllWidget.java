package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class ChiselAllWidget extends AbstractButtonWidget {

    private static final ResourceLocation GREY_BUTTONS = Rechiseled.identifier("textures/screen/grey_buttons.png");
    private static final ResourceLocation CHISEL_TEXTURE = Rechiseled.identifier("textures/item/chisel.png");

    private final Supplier<DisplayEntry> currentEntry;
    public int chiselableItems = 0;

    public ChiselAllWidget(int x, int y, int width, int height, Supplier<DisplayEntry> currentEntry, Runnable onPress){
        super(x, y, width, height, onPress);
        this.currentEntry = currentEntry;
    }

    @Override
    protected boolean isClickable(){
        return this.currentEntry.get() != null;
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        if(this.currentEntry.get() != null){
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.chisel_all").bold().get());
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.chisel_all.shift", TextComponents.translation("key.keyboard.left.shift").color(ChatFormatting.GOLD).get()).get());
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.chisel_all.items", TextComponents.number(this.chiselableItems).get()).italic().color(ChatFormatting.GRAY).get());
        }
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        boolean hasEntry = this.currentEntry.get() != null;
        ScreenUtils.bindTexture(GREY_BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.width, this.height, 0, (hasEntry ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f);
        ScreenUtils.bindTexture(CHISEL_TEXTURE);
        drawColoredTexture(context.poseStack(), this.x + 1, this.y + 2, this.width - 2, this.height - 4, hasEntry ? 1 : 50 / 255f, hasEntry ? 1 : 50 / 255f, hasEntry ? 1 : 50 / 255f);
    }

    private static void drawColoredTexture(PoseStack poseStack, float x, float y, float width, float height, float red, float green, float blue){
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(matrix, x, y + height, 0.0F).uv(0, 1).color(red, green, blue, 1).endVertex();
        buffer.vertex(matrix, x + width, y + height, 0.0F).uv(1, 1).color(red, green, blue, 1).endVertex();
        buffer.vertex(matrix, x + width, y, 0.0F).uv(1, 0).color(red, green, blue, 1).endVertex();
        buffer.vertex(matrix, x, y, 0.0F).uv(0, 0).color(red, green, blue, 1).endVertex();
        tesselator.end();
    }
}
