package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.supermartijn642.core.gui.ScreenUtils;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends GuiIngredient<ItemStack> {

    private static final MatrixStack EMPTY_MATRIX_STACK = new MatrixStack();

    private final MutableRectangle2d rect;
    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    public JEIScrollableSlotsSlot(int index, IIngredientRenderer<ItemStack> ingredientRenderer, IIngredientHelper<ItemStack> ingredientHelper, int cycleOffset){
        super(index, false, ingredientRenderer, ingredientHelper, new MutableRectangle2d(-10000, -10000, 0, 0), 1, 1, cycleOffset);
        this.rect = (MutableRectangle2d)this.getRect();
    }

    public void updatePosition(int x, int y, int scissorX, int scissorY, int scissorWidth, int scissorHeight){
        this.x = x;
        this.y = y;
        this.scissorX = scissorX;
        this.scissorY = scissorY;
        this.scissorWidth = scissorWidth;
        this.scissorHeight = scissorHeight;
        if(x + 16 < scissorX || x >= scissorX + scissorWidth || y + 16 < scissorY || y >= scissorY + scissorHeight)
            this.rect.set(-10000, -10000, 0, 0);
        else{
            x = Math.max(x, scissorX);
            y = Math.max(y, scissorY);
            this.rect.set(
                x, y,
                Math.min(this.x + 18, scissorX + scissorWidth) - x,
                Math.min(this.y + 18, scissorY + scissorHeight) - y
            );
        }
    }

    @Override
    public void draw(int xOffset, int yOffset){
        ScreenUtils.withScissor(
            EMPTY_MATRIX_STACK,
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> super.draw(
                xOffset + this.x - this.getRect().getX(),
                yOffset + this.y - this.getRect().getY()
            )
        );
    }

    @Override
    public void drawHighlight(int color, int xOffset, int yOffset){
        ScreenUtils.withScissor(
            EMPTY_MATRIX_STACK,
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> {
                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x - this.getRect().getX(), this.y - this.getRect().getY(), 0);
                super.drawHighlight(color, xOffset, yOffset);
                RenderSystem.popMatrix();
            }
        );
    }

    private static class MutableRectangle2d extends Rectangle2d {

        private int x, y, width, height;

        public MutableRectangle2d(int x, int y, int width, int height){
            super(0, 0, 0, 0);
            this.set(x, y, width, height);
        }

        void set(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getX(){
            return this.x;
        }

        @Override
        public int getY(){
            return this.y;
        }

        @Override
        public int getWidth(){
            return this.width;
        }

        @Override
        public int getHeight(){
            return this.height;
        }

        @Override
        public boolean contains(int x, int y){
            return x >= this.x && x <= this.y + this.width && y >= this.y && y <= this.y + this.height;
        }
    }
}
