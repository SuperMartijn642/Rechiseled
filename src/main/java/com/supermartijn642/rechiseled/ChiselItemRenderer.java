package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Created 04/04/2025 by SuperMartijn642
 */
public class ChiselItemRenderer implements ItemModel.Unbaked {

    public static final MapCodec<ChiselItemRenderer> CODEC = MapCodec.unit(new ChiselItemRenderer());
    private static final ThreadLocal<Boolean> RECURSION_GUARD = ThreadLocal.withInitial(() -> false);
    private static final SpecialModelRenderer<ItemStack> ICON_RENDERER = new SpecialModelRenderer<>() {

        @Override
        public void render(ItemStack icon, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, boolean hasFoil){
            if(RECURSION_GUARD.get() != null)
                return;
            RECURSION_GUARD.set(true);
            try{
                // Render the icon
                poseStack.pushPose();
                poseStack.translate(0.25, 0.75, 1);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                ClientUtils.getItemRenderer().renderStatic(icon, ItemDisplayContext.GUI, combinedLight, combinedOverlay, poseStack, bufferSource, null, 0);
                poseStack.popPose();
            }finally{
                RECURSION_GUARD.remove();
            }
        }

        @Override
        public @Nullable ItemStack extractArgument(ItemStack stack){
            return null;
        }
    };

    @Override
    public MapCodec<? extends ItemModel.Unbaked> type(){
        return CODEC;
    }

    @Override
    public ItemModel bake(ItemModel.BakingContext bakingContext){
        return (renderState, stack, modelResolver, transformType, level, entity, someRandomId) -> {
            if(transformType != ItemDisplayContext.GUI)
                return;
            RECURSION_GUARD.set(true);
            try{
                // Get the stored item
                ItemStack storedStack = ChiselItem.getStoredStack(stack);
                // Add the renderer for the stored item
                if(!storedStack.isEmpty())
                    renderState.newLayer().setupSpecialModel(ICON_RENDERER, storedStack);
            }finally{
                RECURSION_GUARD.remove();
            }
        };
    }

    @Override
    public void resolveDependencies(Resolver resolver){
    }
}
