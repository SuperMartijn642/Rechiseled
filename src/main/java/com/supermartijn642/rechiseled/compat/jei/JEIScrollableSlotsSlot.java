package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.gui.ingredients.RecipeSlot;
import mezz.jei.common.gui.ingredients.RecipeSlots;
import mezz.jei.common.gui.recipes.layout.builder.IRecipeLayoutSlotSource;
import mezz.jei.common.ingredients.IngredientAcceptor;
import mezz.jei.common.ingredients.RegisteredIngredients;
import mezz.jei.common.util.ImmutableRect2i;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends RecipeSlot implements IRecipeLayoutSlotSource {

    private static final ImmutableRect2i INACTIVE_RECT = new ImmutableRect2i(10000, 10000, 0, 0);

    private final IngredientAcceptor ingredients;
    private final IIngredientVisibility ingredientVisibility;
    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    public JEIScrollableSlotsSlot(RegisteredIngredients registeredIngredients, RecipeIngredientRole role, int ingredientCycleOffset, int legacyIngredientIndex, IIngredientVisibility ingredientVisibility, int x, int y){
        super(registeredIngredients, role, x, y, ingredientCycleOffset, legacyIngredientIndex);
        this.ingredients = new IngredientAcceptor(registeredIngredients);
        this.ingredientVisibility = ingredientVisibility;
        this.x = x;
        this.y = y;
        this.scissorX = x - 1;
        this.scissorY = y - 1;
        this.scissorWidth = 18;
        this.scissorHeight = 18;
    }

    public JEIScrollableSlotsSlot(RegisteredIngredients registeredIngredients, RecipeIngredientRole role, int ingredientCycleOffset, int legacyIngredientIndex, IIngredientVisibility ingredientVisibility){
        this(registeredIngredients, role, ingredientCycleOffset, legacyIngredientIndex, ingredientVisibility, 10000, 10000);
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
    public void draw(PoseStack poseStack){
        ScreenUtils.withScissor(
            poseStack,
            this.scissorX, this.scissorY, this.scissorWidth, this.scissorHeight,
            () -> {
                poseStack.pushPose();
                poseStack.translate(this.x - this.getRect().getX(), this.y - this.getRect().getY(), 0);
                super.draw(poseStack);
                poseStack.popPose();
            }
        );
    }

    @Override
    public void setRecipeSlots(RecipeSlots recipeSlots, IntSet focusMatches){
        this.set(this.ingredients.getAllIngredients(), focusMatches, this.ingredientVisibility);
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
