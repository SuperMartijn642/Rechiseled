package com.supermartijn642.rechiseled.mixin;

import com.supermartijn642.rechiseled.ChiselItemRenderer;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 20/01/2026 by SuperMartijn642
 */
@Mixin(RenderItem.class)
public class RenderItemMixin {

    @Inject(
        method = "renderItemModel",
        at = @At("HEAD")
    )
    private void renderItemModel(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType transformType, boolean leftHand, CallbackInfo ci) {
        if(stack.getItem() == Rechiseled.chisel)
            ChiselItemRenderer.INSTANCE.isInGui = transformType == ItemCameraTransforms.TransformType.GUI;
    }

    @Inject(
        method = "renderItemModelIntoGUI(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V",
        at = @At("HEAD")
    )
    private void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel model, CallbackInfo ci) {
        if(stack.getItem() == Rechiseled.chisel)
            ChiselItemRenderer.INSTANCE.isInGui = true;
    }
}
