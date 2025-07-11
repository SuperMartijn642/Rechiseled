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
import org.joml.Vector3f;

import java.util.Set;

/**
 * Created 18/06/2023 by SuperMartijn642
 */
public class ChiselItemRenderer implements ItemModel.Unbaked {

    public static final MapCodec<ChiselItemRenderer> CODEC = MapCodec.unit(new ChiselItemRenderer());
    private static final SpecialModelRenderer<ItemStack> ICON_RENDERER = new SpecialModelRenderer<>() {
        private static final ThreadLocal<Boolean> RECURSION_GUARD = ThreadLocal.withInitial(() -> false);

        @Override
        public void render(ItemStack icon, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, boolean hasFoil){
            // Render the icon
            RECURSION_GUARD.set(true);
            poseStack.pushPose();
            poseStack.translate(0.25, 0.75, 1);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            ClientUtils.getItemRenderer().renderStatic(icon, ItemDisplayContext.GUI, combinedLight, combinedOverlay, poseStack, bufferSource, null, 0);
            poseStack.popPose();
            RECURSION_GUARD.remove();
        }

        @Override
        public void getExtents(Set<Vector3f> set){
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
            // Get the stored item
            ItemStack storedStack = ChiselItem.getStoredStack(stack);
            // Add the renderer for the stored item
            if(!storedStack.isEmpty())
                renderState.newLayer().setupSpecialModel(ICON_RENDERER, storedStack);
        };
    }

    @Override
    public void resolveDependencies(Resolver resolver){
    }
}
