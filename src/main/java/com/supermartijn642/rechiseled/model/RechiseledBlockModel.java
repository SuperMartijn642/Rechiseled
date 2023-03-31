package com.supermartijn642.rechiseled.model;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * Created 31/03/2023 by SuperMartijn642
 */
public class RechiseledBlockModel extends BlockModel {

    private final RechiseledModel model;

    public RechiseledBlockModel(BlockModel original, RechiseledModel model){
        super(original.parentLocation, original.elements, original.textureMap, original.hasAmbientOcclusion, original.guiLight, original.transforms, original.overrides);
        this.model = model;

        // Add the textures from the rechiseled model to this model
        model.textureMap.forEach((name, either) -> {
            this.textureMap.put(name, either.isLeft() ? Either.left(either.left().left()) : Either.right(either.right()));
        });
    }

    @Override
    public BakedModel bake(ModelBakery bakery, BlockModel blockModel, Function<Material,TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation, boolean gui3d){
        ItemOverrides itemOverrides = this.overrides.isEmpty() ? ItemOverrides.EMPTY : new ItemOverrides(bakery, this, bakery::getModel, this.overrides);
        return this.model.bake(this, bakery, spriteGetter, modelTransform, itemOverrides, modelLocation, gui3d);
    }

    public RechiseledModel getRechiseledModel(){
        return this.model;
    }
}
