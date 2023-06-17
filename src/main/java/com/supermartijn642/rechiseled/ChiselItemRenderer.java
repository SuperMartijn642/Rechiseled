package com.supermartijn642.rechiseled;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

/**
 * Created 18/06/2023 by SuperMartijn642
 */
public class ChiselItemRenderer implements CustomItemRenderer {

    @Override
    public void render(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay){
        renderChisel(stack, transformType, poseStack, bufferSource, combinedLight, combinedOverlay);
        if(transformType == ItemCameraTransforms.TransformType.GUI){
            ItemStack storedStack = ChiselItem.getStoredStack(stack);
            if(!storedStack.isEmpty()){
                poseStack.pushPose();
                poseStack.translate(0.25, 0.75, 1);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                ClientUtils.getItemRenderer().renderStatic(storedStack, ItemCameraTransforms.TransformType.GUI, combinedLight, combinedOverlay, poseStack, bufferSource);
                poseStack.popPose();
            }
        }
    }

    private static void renderChisel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay){
        RenderType renderType = RenderTypeLookup.getRenderType(stack, true);
        IVertexBuilder vertexConsumer;
        if(stack.hasFoil()){
            poseStack.pushPose();
            MatrixStack.Entry pose = poseStack.last();
            if(transformType == ItemCameraTransforms.TransformType.GUI)
                pose.pose().multiply(0.5f);
            else if(transformType.firstPerson())
                pose.pose().multiply(0.75f);
            vertexConsumer = ItemRenderer.getCompassFoilBufferDirect(bufferSource, renderType, pose);
            poseStack.popPose();
        }else
            vertexConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, stack.hasFoil());
        ItemRenderer renderer = ClientUtils.getItemRenderer();
        renderer.renderModelLists(renderer.getModel(stack, null, null), stack, combinedLight, combinedOverlay, poseStack, vertexConsumer);
    }
}
