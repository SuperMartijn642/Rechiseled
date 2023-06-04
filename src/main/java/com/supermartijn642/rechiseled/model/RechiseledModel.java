package com.supermartijn642.rechiseled.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.*;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
@Deprecated
public class RechiseledModel implements IModelGeometry<RechiseledModel> {

    private final boolean shouldConnect;
    private final ResourceLocation parent;
    private final List<BlockPart> elements;
    private final Map<String,Either<Pair<Material,Boolean>,String>> textureMap;
    private final boolean ambientOcclusion;
    private final BlockModel.GuiLight guiLight;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> itemOverrides;

    public RechiseledModel(boolean shouldConnect, ResourceLocation parent, List<BlockPart> elements, Map<String,Either<Pair<Material,Boolean>,String>> textureMap, boolean ambientOcclusion, BlockModel.GuiLight guiLight, ItemCameraTransforms cameraTransforms, List<ItemOverride> itemOverrides){
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
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material,TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation){
        Function<ResourceLocation,IUnbakedModel> modelGetter = bakery::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(this.getTexture("particle", modelGetter).getFirst());
        List<BlockPart> elements = this.getElements(modelGetter);
        ItemCameraTransforms transforms = this.getTransforms(modelGetter);

        Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockPart part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockPartFace face = part.faces.get(direction);

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
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation,IUnbakedModel> modelGetter, Set<Pair<String,String>> missingTextureErrors){
        Set<Material> textures = Sets.newHashSet();

        Material particles = this.getTexture("particle", modelGetter).getFirst();
        if(Objects.equals(particles.texture(), MissingTextureSprite.getLocation()))
            missingTextureErrors.add(Pair.of("particle", owner.getModelName()));

        for(BlockPart part : this.getElements(modelGetter)){
            for(BlockPartFace face : part.faces.values()){
                Material texture = this.getTexture(face.texture, modelGetter).getFirst();
                if(Objects.equals(texture.texture(), MissingTextureSprite.getLocation()))
                    missingTextureErrors.add(Pair.of(face.texture, owner.getModelName()));

                textures.add(texture);
            }
        }

        return textures;
    }

    private Pair<Material,Boolean> getTexture(String texture, Function<ResourceLocation,IUnbakedModel> modelGetter){
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
                return Pair.of(new Material(AtlasTexture.LOCATION_BLOCKS, MissingTextureSprite.getLocation()), false);
            }

            list.add(texture);
        }
    }

    private Either<Pair<Material,Boolean>,String> findTextureEntry(String texture, Function<ResourceLocation,IUnbakedModel> modelGetter){
        if(this.textureMap.containsKey(texture))
            return this.textureMap.get(texture);

        IUnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
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

        return Either.left(Pair.of(new Material(AtlasTexture.LOCATION_BLOCKS, MissingTextureSprite.getLocation()), false));
    }

    private List<BlockPart> getElements(Function<ResourceLocation,IUnbakedModel> modelGetter){
        List<BlockPart> elements = this.elements;
        if(this.elements.isEmpty()){
            IUnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
            if(parent instanceof BlockModel){
                if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel)
                    elements = ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).getElements(modelGetter);
                else
                    elements = ((BlockModel)parent).getElements();
            }
        }

        return elements;
    }

    public ItemCameraTransforms getTransforms(Function<ResourceLocation,IUnbakedModel> modelGetter){
        ItemTransformVec3f thirdPersonLeftHand = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, modelGetter);
        ItemTransformVec3f thirdPersonRightHand = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, modelGetter);
        ItemTransformVec3f firstPersonLeftHand = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, modelGetter);
        ItemTransformVec3f firstPersonRightHand = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, modelGetter);
        ItemTransformVec3f head = this.getTransform(ItemCameraTransforms.TransformType.HEAD, modelGetter);
        ItemTransformVec3f gui = this.getTransform(ItemCameraTransforms.TransformType.GUI, modelGetter);
        ItemTransformVec3f ground = this.getTransform(ItemCameraTransforms.TransformType.GROUND, modelGetter);
        ItemTransformVec3f fixed = this.getTransform(ItemCameraTransforms.TransformType.FIXED, modelGetter); // For item frames
        return new ItemCameraTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed);
    }

    private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType transformType, Function<ResourceLocation,IUnbakedModel> modelGetter){
        if(this.cameraTransforms.hasTransform(transformType))
            return this.cameraTransforms.getTransform(transformType);

        IUnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent instanceof BlockModel){
            if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel){
                return ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).getTransform(transformType, modelGetter);
            }else
                return ((BlockModel)parent).getTransforms().getTransform(transformType);
        }
        return this.cameraTransforms.getTransform(transformType);
    }
}
