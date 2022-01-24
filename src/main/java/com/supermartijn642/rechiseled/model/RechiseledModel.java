package com.supermartijn642.rechiseled.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.*;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModel implements IModelGeometry<RechiseledModel> {

    private final boolean shouldConnect;
    private final ResourceLocation parent;
    private final List<BlockElement> elements;
    private final Map<String,Either<Pair<Material,Boolean>,String>> textureMap;
    private final boolean ambientOcclusion;
    private final BlockModel.GuiLight guiLight;
    private final ItemTransforms cameraTransforms;
    private final List<ItemOverride> itemOverrides;

    public RechiseledModel(boolean shouldConnect, ResourceLocation parent, List<BlockElement> elements, Map<String,Either<Pair<Material,Boolean>,String>> textureMap, boolean ambientOcclusion, BlockModel.GuiLight guiLight, ItemTransforms cameraTransforms, List<ItemOverride> itemOverrides){
        this.shouldConnect = shouldConnect;
        this.parent = parent;
        this.elements = elements;
        this.textureMap = textureMap;
        this.ambientOcclusion = ambientOcclusion;
        this.guiLight = guiLight;
        this.cameraTransforms = cameraTransforms;
        this.itemOverrides = itemOverrides;
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material,TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation){
        Function<ResourceLocation,UnbakedModel> modelGetter = bakery::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(owner.resolveTexture("particle"));
        List<BlockElement> elements = this.getElements(bakery::getModel);
        ItemTransforms transforms = this.getTransforms(modelGetter);

        Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockElement part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockElementFace face = part.faces.get(direction);

                Pair<Material,Boolean> texture = this.getTexture(face.texture, modelGetter);
                TextureAtlasSprite sprite = spriteGetter.apply(texture.getFirst());
                boolean connecting = texture.getSecond();
                BakedQuad quad = BlockModel.makeBakedQuad(part, face, sprite, direction, modelTransform, modelLocation);
                Direction cullFace = face.cullForDirection == null ? null : Direction.rotate(modelTransform.getRotation().getMatrix(), face.cullForDirection);

                quads.putIfAbsent(cullFace, new ArrayList<>());
                quads.get(cullFace).add(new Tuple<>(quad, connecting));
            }
        }

        return this.shouldConnect ?
            new RechiseledConnectedBakedModel(quads, owner.useSmoothLighting(), owner.isShadedInGui(), owner.isSideLit(), false, particle, overrides, transforms) :
            new RechiseledBakedModel(quads, owner.useSmoothLighting(), owner.isShadedInGui(), owner.isSideLit(), false, particle, overrides, transforms);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation,UnbakedModel> modelGetter, Set<Pair<String,String>> missingTextureErrors){
        Set<Material> textures = Sets.newHashSet();

        for(BlockElement part : this.getElements(modelGetter)){
            for(BlockElementFace face : part.faces.values()){
                Material texture = this.getTexture(face.texture, modelGetter).getFirst();
                if(Objects.equals(texture.texture(), MissingTextureAtlasSprite.getLocation()))
                    missingTextureErrors.add(Pair.of(face.texture, owner.getModelName()));

                textures.add(texture);
            }
        }

        return textures;
    }

    private Pair<Material,Boolean> getTexture(String texture, Function<ResourceLocation,UnbakedModel> modelGetter){
        if(texture.charAt(0) == '#')
            texture = texture.substring(1);

        List<String> list = Lists.newArrayList();

        while(true){
            Either<Pair<Material,Boolean>,String> either = this.findTextureEntry(texture, modelGetter);
            Optional<Pair<Material,Boolean>> optional = either.left();
            if(optional.isPresent())
                return optional.get();

            texture = either.right().get();
            if(list.contains(texture)){
                System.err.printf("Unable to resolve texture due to reference chain %s->%s%n", Joiner.on("->").join(list), texture);
                return Pair.of(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()), false);
            }

            list.add(texture);
        }
    }

    private Either<Pair<Material,Boolean>,String> findTextureEntry(String texture, Function<ResourceLocation,UnbakedModel> modelGetter){
        if(this.textureMap.containsKey(texture))
            return this.textureMap.get(texture);

        UnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent instanceof BlockModel){
            if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel)
                return ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).findTextureEntry(texture, modelGetter);
            else{
                for(BlockModel blockmodel = (BlockModel)parent; blockmodel != null; blockmodel = blockmodel.parent){
                    Either<Material,String> either = blockmodel.textureMap.get(texture);
                    if(either != null)
                        return either.mapLeft(material -> Pair.of(material, false));
                }
            }
        }

        return Either.left(Pair.of(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()), false));
    }

    private List<BlockElement> getElements(Function<ResourceLocation,UnbakedModel> modelGetter){
        List<BlockElement> elements = this.elements;
        if(this.elements.isEmpty()){
            UnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
            if(parent instanceof BlockModel){
                if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel)
                    elements = ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).getElements(modelGetter);
                else
                    elements = ((BlockModel)parent).getElements();
            }
        }

        return elements;
    }

    public ItemTransforms getTransforms(Function<ResourceLocation,UnbakedModel> modelGetter){
        ItemTransform thirdPersonLeftHand = this.getTransform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, modelGetter);
        ItemTransform thirdPersonRightHand = this.getTransform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, modelGetter);
        ItemTransform firstPersonLeftHand = this.getTransform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, modelGetter);
        ItemTransform firstPersonRightHand = this.getTransform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, modelGetter);
        ItemTransform head = this.getTransform(ItemTransforms.TransformType.HEAD, modelGetter);
        ItemTransform gui = this.getTransform(ItemTransforms.TransformType.GUI, modelGetter);
        ItemTransform ground = this.getTransform(ItemTransforms.TransformType.GROUND, modelGetter);
        ItemTransform fixed = this.getTransform(ItemTransforms.TransformType.FIXED, modelGetter); // For item frames
        return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed);
    }

    private ItemTransform getTransform(ItemTransforms.TransformType transformType, Function<ResourceLocation,UnbakedModel> modelGetter){
        if(this.cameraTransforms.hasTransform(transformType))
            return this.cameraTransforms.getTransform(transformType);

        UnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent instanceof BlockModel){
            if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel){
                return ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).getTransform(transformType, modelGetter);
            }else
                return ((BlockModel)parent).getTransforms().getTransform(transformType);
        }
        return this.cameraTransforms.getTransform(transformType);
    }
}
