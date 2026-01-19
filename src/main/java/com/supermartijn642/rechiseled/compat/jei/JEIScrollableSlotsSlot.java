package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends GuiIngredient<ItemStack> {

    private final Rect2i rect;
    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    public JEIScrollableSlotsSlot(int index, IIngredientRenderer<ItemStack> ingredientRenderer, IIngredientHelper<ItemStack> ingredientHelper, int cycleOffset){
        super(index, false, ingredientRenderer, ingredientHelper, new Rect2i(-10000, -10000, 0, 0), 1, 1, cycleOffset);
        this.rect = this.getRect();
    }

    public void updatePosition(int x, int y, int scissorX, int scissorY, int scissorWidth, int scissorHeight){
        this.x = x;
        this.y = y;
        this.scissorX = scissorX;
        this.scissorY = scissorY;
        this.scissorWidth = scissorWidth;
        this.scissorHeight = scissorHeight;
        if(x + 16 < scissorX || x >= scissorX + scissorWidth || y + 16 < scissorY || y >= scissorY + scissorHeight){
            this.rect.setPosition(-10000, -10000);
            this.rect.setWidth(0);
            this.rect.setHeight(0);
        }else{
            x = Math.max(x, scissorX);
            y = Math.max(y, scissorY);
            this.rect.setPosition(x, y);
            this.rect.setWidth(Math.min(this.x + 18, scissorX + scissorWidth) - x);
            this.rect.setHeight(Math.min(this.y + 18, scissorY + scissorHeight) - y);
        }
    }

    @Override
    public void draw(PoseStack poseStack, int xOffset, int yOffset){
        ScreenUtils.withScissor(
            poseStack,
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> super.draw(
                poseStack,
                xOffset + this.x - this.getRect().getX(),
                yOffset + this.y - this.getRect().getY()
            )
        );
    }

    @Override
    public void drawHighlight(PoseStack poseStack, int color, int xOffset, int yOffset){
        ScreenUtils.withScissor(
            poseStack,
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> {
                poseStack.pushPose();
                poseStack.translate(this.x - this.getRect().getX(), this.y - this.getRect().getY(), 0);
                super.drawHighlight(poseStack, color, xOffset, yOffset);
                poseStack.popPose();
            }
        );
    }
}
