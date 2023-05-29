package com.supermartijn642.rechiseled.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.supermartijn642.rechiseled.model.RechiseledBlockModel;
import com.supermartijn642.rechiseled.model.RechiseledModel;
import com.supermartijn642.rechiseled.model.RechiseledModelDeserializer;
import net.minecraft.client.renderer.block.model.BlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

/**
 * Created 31/03/2023 by SuperMartijn642
 */
@Deprecated
@Mixin(BlockModel.Deserializer.class)
public class BlockModelDeserializerMixin {

    @Unique
    private static boolean shouldIgnore = false;

    @Inject(
        method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockModel;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void deserialize(JsonElement json, Type type, JsonDeserializationContext context, CallbackInfoReturnable<BlockModel> ci) throws JsonParseException{
        if(shouldIgnore)
            return;

        JsonElement loader = json.getAsJsonObject().get("loader");
        if(loader != null && loader.isJsonPrimitive() && loader.getAsJsonPrimitive().isString() && loader.getAsString().equals("rechiseled:connecting_model")){
            // Obtain the original model
            shouldIgnore = true;
            //noinspection DataFlowIssue
            BlockModel.Deserializer deserializer = (BlockModel.Deserializer)(Object)this;
            BlockModel originalModel = deserializer.deserialize(json, type, context);
            shouldIgnore = false;

            // Load Rechiseled's model
            RechiseledModel customModel = RechiseledModelDeserializer.deserialize(json.getAsJsonObject(), context);

            // Create a dummy block model
            RechiseledBlockModel newModel = new RechiseledBlockModel(originalModel, customModel);
            ci.setReturnValue(newModel);
            ci.cancel();
        }
    }
}
