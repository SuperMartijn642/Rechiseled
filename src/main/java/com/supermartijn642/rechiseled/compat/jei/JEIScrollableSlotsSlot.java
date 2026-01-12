package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.ScreenUtils;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.ingredients.ICycler;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RendererOverrides;
import mezz.jei.library.gui.recipes.layout.builder.RecipeSlotBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created 17/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsSlot extends RecipeSlot {

    private static final ImmutableRect2i INACTIVE_RECT = new ImmutableRect2i(-10000, -10000, 0, 0);

    private int x, y;
    private int scissorX, scissorY, scissorWidth, scissorHeight;

    private JEIScrollableSlotsSlot(RecipeIngredientRole role, ImmutableRect2i rect, ICycler cycler, List<IRecipeSlotTooltipCallback> tooltipCallbacks, List<Optional<ITypedIngredient<?>>> allIngredients, @Nullable List<Optional<ITypedIngredient<?>>> focusedIngredients, @Nullable IDrawable background, @Nullable IDrawable overlay, @Nullable String slotName, @Nullable RendererOverrides rendererOverrides){
        super(role, rect, cycler, tooltipCallbacks, allIngredients, focusedIngredients, background, overlay, slotName, rendererOverrides);
        this.x = rect.getX();
        this.y = rect.getY();
        this.scissorX = this.x - 1;
        this.scissorY = this.y - 1;
        this.scissorWidth = 18;
        this.scissorHeight = 18;
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

    public static class Builder extends RecipeSlotBuilder {

        private final Consumer<JEIScrollableSlotsSlot> slotConsumer;

        public Builder(IIngredientManager ingredientManager, int index, RecipeIngredientRole role, int x, int y){
            super(ingredientManager, index, role, x, y);
            this.slotConsumer = null;
        }

        public Builder(IIngredientManager ingredientManager, int index, RecipeIngredientRole role, Consumer<JEIScrollableSlotsSlot> slotConsumer){
            super(ingredientManager, index, role, -10000, -10000);
            this.slotConsumer = slotConsumer;
        }

        @Override
        public IRecipeSlotDrawable build(Set<Integer> focusMatches, ICycler cycler){
            List<Optional<ITypedIngredient<?>>> allIngredients = JEIFieldAccess.getRecipeSlotBuilderIngredients(this).getAllIngredients();
            List<Optional<ITypedIngredient<?>>> focusedIngredients = null;
            if(!focusMatches.isEmpty()){
                focusedIngredients = new ArrayList<>();
                for(Integer i : focusMatches){
                    if(i < allIngredients.size()){
                        Optional<ITypedIngredient<?>> ingredient = allIngredients.get(i);
                        focusedIngredients.add(ingredient);
                    }
                }
            }
            JEIScrollableSlotsSlot slot = new JEIScrollableSlotsSlot(
                JEIFieldAccess.getRecipeSlotBuilderRole(this),
                JEIFieldAccess.getRecipeSlotBuilderRect(this),
                cycler,
                JEIFieldAccess.getRecipeSlotBuilderTooltipCallbacks(this),
                allIngredients,
                focusedIngredients,
                JEIFieldAccess.getRecipeSlotBuilderBackground(this),
                JEIFieldAccess.getRecipeSlotBuilderOverlay(this),
                JEIFieldAccess.getRecipeSlotBuilderSlotName(this),
                JEIFieldAccess.getRecipeSlotBuilderRendererOverrides(this)
            );
            if(this.slotConsumer != null)
                this.slotConsumer.accept(slot);
            return slot;
        }
    }
}
