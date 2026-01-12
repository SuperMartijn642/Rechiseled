package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RecipeSlots;
import mezz.jei.library.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import mezz.jei.library.ingredients.IngredientAcceptor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends RecipeSlot implements IRecipeLayoutSlotSource {

    private static final ImmutableRect2i INACTIVE_RECT = new ImmutableRect2i(-10000, -10000, 0, 0);

    private final IngredientAcceptor ingredients;
    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    public JEIScrollableSlotsSlot(IIngredientManager ingredientManager, RecipeIngredientRole role, int ingredientCycleOffset, int x, int y){
        super(ingredientManager, role, x, y, ingredientCycleOffset);
        this.ingredients = new IngredientAcceptor(ingredientManager);
        this.x = x;
        this.y = y;
        this.scissorX = x - 1;
        this.scissorY = y - 1;
        this.scissorWidth = 18;
        this.scissorHeight = 18;
    }

    public JEIScrollableSlotsSlot(IIngredientManager ingredientManager, RecipeIngredientRole role, int ingredientCycleOffset){
        this(ingredientManager, role, ingredientCycleOffset, -10000, -10000);
    }

    public void updatePosition(int x, int y, int scissorX, int scissorY, int scissorWidth, int scissorHeight){
        this.x = x;
        this.y = y;
        this.scissorX = scissorX;
        this.scissorY = scissorY;
        this.scissorWidth = scissorWidth;
        this.scissorHeight = scissorHeight;
        if(x + 16 < scissorX || x >= scissorX + scissorWidth || y + 16 < scissorY || y >= scissorY + scissorHeight)
            JEIFieldAccess.setRecipeSlotRect(this, INACTIVE_RECT);
        else{
            x = Math.max(x, scissorX);
            y = Math.max(y, scissorY);
            JEIFieldAccess.setRecipeSlotRect(this, new ImmutableRect2i(
                x, y,
                Math.min(this.x + 16, scissorX + scissorWidth) - x,
                Math.min(this.y + 16, scissorY + scissorHeight) - y
            ));
        }
    }

    public void addItem(Item item){
        this.ingredients.addItemStack(new ItemStack(item));
    }

    @Override
    public void draw(GuiGraphics graphics){
        ScreenUtils.withScissor(
            graphics.pose(),
            this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight,
            () -> {
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                poseStack.translate(this.x - this.getRect().getX(), this.y - this.getRect().getY(), 0);
                super.draw(graphics);
                poseStack.popPose();
            }
        );
    }

    @Override
    public void setRecipeSlots(RecipeSlots recipeSlots, IntSet focusMatches, IIngredientVisibility ingredientVisibility){
        this.set(this.ingredients.getAllIngredients(), focusMatches, ingredientVisibility);
        recipeSlots.addSlot(this);
    }

    @Override
    public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType){
        return this.ingredients.getIngredients(ingredientType);
    }

    @Override
    public Stream<IIngredientType<?>> getIngredientTypes(){
        return this.ingredients.getIngredientTypes();
    }

    @Override
    public IntSet getMatches(IFocusGroup focuses){
        return this.ingredients.getMatches(focuses, this.getRole());
    }

    @Override
    public int getIngredientCount(){
        return this.ingredients.getAllIngredients().size();
    }
}
