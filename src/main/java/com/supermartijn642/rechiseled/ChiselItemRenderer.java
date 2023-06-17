package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Created 18/06/2023 by SuperMartijn642
 */
public class ChiselItemRenderer implements CustomItemRenderer {

    @Override
    public void render(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay){
        renderChisel(stack, transformType, poseStack, bufferSource, combinedLight, combinedOverlay);
        if(transformType == ItemDisplayContext.GUI){
            ItemStack storedStack = ChiselItem.getStoredStack(stack);
            if(!storedStack.isEmpty()){
                poseStack.pushPose();
                poseStack.translate(0.25, 0.75, 1);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                ClientUtils.getItemRenderer().renderStatic(storedStack, ItemDisplayContext.GUI, combinedLight, combinedOverlay, poseStack, bufferSource, null, 0);
                poseStack.popPose();
            }
        }
    }

    private static void renderChisel(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay){
        ItemRenderer renderer = ClientUtils.getItemRenderer();
        BakedModel model = renderer.getModel(stack, null, null, 0);
        for(var subModel : model.getRenderPasses(stack, true)){
            for(var renderType : subModel.getRenderTypes(stack, true)){
                VertexConsumer vertexConsumer;
                if(stack.hasFoil()){
                    poseStack.pushPose();
                    PoseStack.Pose pose = poseStack.last();
                    if(transformType == ItemDisplayContext.GUI)
                        MatrixUtil.mulComponentWise(pose.pose(), 0.5f);
                    else if(transformType.firstPerson())
                        MatrixUtil.mulComponentWise(pose.pose(), 0.75f);
                    vertexConsumer = ItemRenderer.getCompassFoilBufferDirect(bufferSource, renderType, pose);
                    poseStack.popPose();
                }else
                    vertexConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, stack.hasFoil());
                renderer.renderModelLists(subModel, stack, combinedLight, combinedOverlay, poseStack, vertexConsumer);
            }
        }
    }
}
