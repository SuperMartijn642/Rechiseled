package com.supermartijn642.rechiseled.compat.jei;

import com.supermartijn642.core.gui.ScreenUtils;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends GuiIngredient<ItemStack> {

    private final Rectangle rect;
    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    public JEIScrollableSlotsSlot(int index, IIngredientRenderer<ItemStack> ingredientRenderer, IIngredientHelper<ItemStack> ingredientHelper, int cycleOffset){
        super(index, false, ingredientRenderer, ingredientHelper, new Rectangle(-10000, -10000, 0, 0), 1, 1, cycleOffset);
        this.rect = this.getRect();
    }

    public void updatePosition(int x, int y, int scissorX, int scissorY, int scissorWidth, int scissorHeight){
        this.x = x;
        this.y = y;
        this.scissorX = scissorX;
        this.scissorY = scissorY;
        this.scissorWidth = scissorWidth;
        this.scissorHeight = scissorHeight;
        if(x + 16 < scissorX || x >= scissorX + scissorWidth || y + 16 < scissorY || y >= scissorY + scissorHeight)
            this.rect.setBounds(-10000, -10000, 0, 0);
        else{
            x = Math.max(x, scissorX);
            y = Math.max(y, scissorY);
            this.rect.setBounds(
                x, y,
                Math.min(this.x + 18, scissorX + scissorWidth) - x,
                Math.min(this.y + 18, scissorY + scissorHeight) - y
            );
        }
    }

    @Override
    public void draw(Minecraft minecraft, int xOffset, int yOffset){
        ScreenUtils.withScissor(
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> super.draw(
                minecraft,
                xOffset + this.x - this.rect.x,
                yOffset + this.y - this.rect.y
            )
        );
    }

    @Override
    public void drawHighlight(Minecraft minecraft, Color color, int xOffset, int yOffset){
        ScreenUtils.withScissor(
            this.scissorX + xOffset, this.scissorY + yOffset, this.scissorWidth, this.scissorHeight,
            () -> {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.x - this.rect.x, this.y - this.rect.y, 0);
                super.drawHighlight(minecraft, color, xOffset, yOffset);
                GlStateManager.popMatrix();
            }
        );
    }
}
