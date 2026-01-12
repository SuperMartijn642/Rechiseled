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
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Created 04/04/2025 by SuperMartijn642
 */
public class ChiselItemRenderer implements ItemModel.Unbaked {

    public static final MapCodec<ChiselItemRenderer> CODEC = MapCodec.unit(new ChiselItemRenderer());
    private static final ThreadLocal<Boolean> RECURSION_GUARD = new ThreadLocal<>();
    private static final SpecialModelRenderer<ItemStackRenderState> ICON_RENDERER = new SpecialModelRenderer<>() {

        @Override
        public void submit(@Nullable ItemStackRenderState iconRenderState, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector output, int combinedLight, int combinedOverlay, boolean hasFoil, int k){
            if(RECURSION_GUARD.get() != null)
                return;
            RECURSION_GUARD.set(true);
            try{
                // Submit the icon
                poseStack.pushPose();
                poseStack.translate(0.25, 0.75, 1);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                iconRenderState.submit(poseStack, output, combinedLight, combinedOverlay, 0);
                poseStack.popPose();
            }finally{
                RECURSION_GUARD.remove();
            }
        }

        @Override
        public void getExtents(Consumer<Vector3fc> set){
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
            RECURSION_GUARD.set(true);
            try{
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
                    ClientUtils.getMinecraft().getItemModelResolver().updateForTopItem(iconRenderState, storedStack, ItemDisplayContext.GUI, level, entity, someRandomId);
                    renderState.newLayer().setupSpecialModel(ICON_RENDERER, iconRenderState);
                    renderState.appendModelIdentityElement(storedStack.getItem());
                }
            }finally{
                RECURSION_GUARD.remove();
            }
        };
    }

    @Override
    public void resolveDependencies(Resolver resolver){
    }
}
