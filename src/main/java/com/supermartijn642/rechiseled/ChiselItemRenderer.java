package com.supermartijn642.rechiseled;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

/**
 * Created 18/06/2023 by SuperMartijn642
 */
public class ChiselItemRenderer implements CustomItemRenderer {

    @Override
    public void render(ItemStack stack){
        renderChisel(stack);
        ItemStack storedStack = ChiselItem.getStoredStack(stack);
        if(!storedStack.isEmpty()){
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.25f, 0.75f, 0.5f);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            ClientUtils.getItemRenderer().renderItem(storedStack, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.popMatrix();
        }
    }

    private static void renderChisel(ItemStack stack){
        RenderItem renderer = ClientUtils.getItemRenderer();
        IBakedModel model = renderer.getItemModelWithOverrides(stack, null, null);
        renderer.renderModel(model, stack);
        if(stack.hasEffect())
            renderer.renderEffect(model);
    }
}
