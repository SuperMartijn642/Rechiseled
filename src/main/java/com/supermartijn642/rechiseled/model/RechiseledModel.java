package com.supermartijn642.rechiseled.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.supermartijn642.core.render.TextureAtlases;
import com.supermartijn642.core.util.Either;
import com.supermartijn642.core.util.Pair;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModel {

    private final boolean shouldConnect;
    final Map<String,Either<Pair<Material,Boolean>,String>> textureMap;

    public RechiseledModel(boolean shouldConnect, Map<String,Either<Pair<Material,Boolean>,String>> textureMap){
        this.shouldConnect = shouldConnect;
        this.textureMap = textureMap;
    }

    public BakedModel bake(RechiseledBlockModel owner, ModelBaker bakery, Function<Material,TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation, boolean gui3d){
        Function<ResourceLocation,UnbakedModel> modelGetter = bakery::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(this.getTexture(owner, "particle", modelGetter).left());
        List<BlockElement> elements = owner.getElements();
        ItemTransforms transforms = owner.getTransforms();

        Map<Direction,List<Pair<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockElement part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockElementFace face = part.faces.get(direction);

                Pair<Material,Boolean> texture = this.getTexture(owner, face.texture, modelGetter);
                TextureAtlasSprite sprite = spriteGetter.apply(texture.left());
                boolean connecting = texture.right();
                BakedQuad quad = BlockModel.bakeFace(part, face, sprite, direction, modelTransform, modelLocation);
                Direction cullFace = face.cullForDirection == null ? null : Direction.rotate(modelTransform.getRotation().getMatrix(), face.cullForDirection);

                quads.computeIfAbsent(cullFace, o -> new ArrayList<>()).add(Pair.of(quad, connecting));
            }
        }

        return this.shouldConnect ?
            new RechiseledConnectedBakedModel(quads, owner.hasAmbientOcclusion(), gui3d, owner.getGuiLight().lightLikeBlock(), false, particle, overrides, transforms) :
            new RechiseledBakedModel(quads, owner.hasAmbientOcclusion(), gui3d, owner.getGuiLight().lightLikeBlock(), false, particle, overrides, transforms);
    }

    private Pair<Material,Boolean> getTexture(RechiseledBlockModel owner, String texture, Function<ResourceLocation,UnbakedModel> modelGetter){
        if(texture.charAt(0) == '#')
            texture = texture.substring(1);

        List<String> list = Lists.newArrayList();

        while(true){
            Either<Pair<Material,Boolean>,String> either = this.findTextureEntry(owner, texture, modelGetter);
            if(either.isLeft())
                return either.left();

            texture = either.right();
            if(list.contains(texture)){
                System.err.printf("Unable to resolve texture due to reference chain %s->%s%n", Joiner.on("->").join(list), texture);
                return Pair.of(new Material(TextureAtlases.getBlocks(), MissingTextureAtlasSprite.getLocation()), false);
            }

            list.add(texture);
        }
    }

    private Either<Pair<Material,Boolean>,String> findTextureEntry(RechiseledBlockModel owner, String texture, Function<ResourceLocation,UnbakedModel> modelGetter){
        BlockModel blockModel = owner;
        while(blockModel != null){
            if(blockModel instanceof RechiseledBlockModel){
                RechiseledModel model = ((RechiseledBlockModel)blockModel).getRechiseledModel();
                if(model.textureMap.containsKey(texture))
                    return model.textureMap.get(texture);
            }
            if(blockModel.textureMap.containsKey(texture))
                return blockModel.textureMap.get(texture).map(material -> Either.left(Pair.of(material, false)), Either::right);
            blockModel = blockModel.parent;
        }
        return Either.left(Pair.of(new Material(TextureAtlases.getBlocks(), MissingTextureAtlasSprite.getLocation()), false));
    }
}
