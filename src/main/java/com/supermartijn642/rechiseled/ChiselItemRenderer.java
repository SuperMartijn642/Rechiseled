package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
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
    private static final ThreadLocal<Boolean> RECURSION_GUARD = ThreadLocal.withInitial(() -> false);
    private static final SpecialModelRenderer<ItemStackRenderState> ICON_RENDERER = new SpecialModelRenderer<>() {

        @Override
        public void submit(@Nullable ItemStackRenderState iconRenderState, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector output, int combinedLight, int combinedOverlay, boolean hasFoil, int k){
            if(RECURSION_GUARD.get() != null)
                return;
            // Submit the icon
            RECURSION_GUARD.set(true);
            poseStack.pushPose();
            poseStack.translate(0.25, 0.75, 1);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            iconRenderState.submit(poseStack, output, combinedLight, combinedOverlay, 0);
            poseStack.popPose();
            RECURSION_GUARD.set(false);
        }

        @Override
        public void getExtents(Set<Vector3f> set){
        }

        @Override
        public @Nullable ItemStackRenderState extractArgument(ItemStack stack){
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
            if(transformType != ItemDisplayContext.GUI || RECURSION_GUARD.get() != null)
                return;
            // Get the stored item
            ItemStack storedStack = ChiselItem.getStoredStack(stack);
            // Add the renderer for the stored item
            if(!storedStack.isEmpty()){
                ItemStackRenderState iconRenderState = new ItemStackRenderState() {
                    @Override
                    public void appendModelIdentityElement(Object object){
                        renderState.appendModelIdentityElement(object);
                    }
                };
                RECURSION_GUARD.set(true);
                ClientUtils.getMinecraft().getItemModelResolver().updateForTopItem(iconRenderState, storedStack, ItemDisplayContext.GUI, level, entity, someRandomId);
                RECURSION_GUARD.remove();
                renderState.newLayer().setupSpecialModel(ICON_RENDERER, iconRenderState);
                renderState.appendModelIdentityElement(storedStack.getItem());
            }
        };
    }

    @Override
    public void resolveDependencies(Resolver resolver){
    }
}
