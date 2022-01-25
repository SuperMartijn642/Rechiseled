package com.supermartijn642.rechiseled.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
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
public class RechiseledModel implements IModelGeometry<RechiseledModel> {

    private final boolean shouldConnect;
    private final ResourceLocation parent;
    private final List<BlockPart> elements;
    private final Map<String,Pair<String,Boolean>> textureMap;
    private final ItemCameraTransforms cameraTransforms;

    public RechiseledModel(boolean shouldConnect, ResourceLocation parent, List<BlockPart> elements, Map<String,Pair<String,Boolean>> textureMap, boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms cameraTransforms, List<ItemOverride> itemOverrides){
        this.shouldConnect = shouldConnect;
        this.parent = parent;
        this.elements = elements;
        this.textureMap = textureMap;
        this.cameraTransforms = cameraTransforms;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<ResourceLocation,TextureAtlasSprite> spriteGetter, ISprite iSprite, VertexFormat format, ItemOverrideList overrides){
        Function<ResourceLocation,IUnbakedModel> modelGetter = bakery::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(this.getTexture("particle", modelGetter).getFirst());
        List<BlockPart> elements = this.getElements(modelGetter);
        ItemCameraTransforms transforms = this.getTransforms(modelGetter);

        Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockPart part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockPartFace face = part.faces.get(direction);

                Pair<ResourceLocation,Boolean> texture = this.getTexture(face.texture, modelGetter);
                TextureAtlasSprite sprite = spriteGetter.apply(texture.getFirst());
                boolean connecting = texture.getSecond();
                BakedQuad quad = BlockModel.makeBakedQuad(part, face, sprite, direction, iSprite);
                Direction cullFace = iSprite.getState().apply(java.util.Optional.empty()).map(transform -> transform.rotateTransform(face.cullForDirection)).orElse(face.cullForDirection);

                quads.putIfAbsent(cullFace, new ArrayList<>());
                quads.get(cullFace).add(new Tuple<>(quad, connecting));
            }
        }

        return this.shouldConnect ?
            new RechiseledConnectedBakedModel(quads, owner.useSmoothLighting(), owner.isShadedInGui(), false, particle, overrides, transforms) :
            new RechiseledBakedModel(quads, owner.useSmoothLighting(), owner.isShadedInGui(), false, particle, overrides, transforms);
    }

    @Override
    public Collection<ResourceLocation> getTextureDependencies(IModelConfiguration owner, Function<ResourceLocation,IUnbakedModel> modelGetter, Set<String> missingTextureErrors){
        Set<ResourceLocation> textures = Sets.newHashSet();

        ResourceLocation particles = this.getTexture("particle", modelGetter).getFirst();
        if(Objects.equals(particles, MissingTextureSprite.getLocation()))
            missingTextureErrors.add(String.format("%s in %s", particles, owner.getModelName()));

        for(BlockPart part : this.getElements(modelGetter)){
            for(BlockPartFace face : part.faces.values()){
                ResourceLocation texture = this.getTexture(face.texture, modelGetter).getFirst();
                if(Objects.equals(texture, MissingTextureSprite.getLocation()))
                    missingTextureErrors.add(String.format("%s in %s", face.texture, owner.getModelName()));

                textures.add(texture);
            }
        }

        return textures;
    }

    private Pair<ResourceLocation,Boolean> getTexture(String texture, Function<ResourceLocation,IUnbakedModel> modelGetter){
        if(texture.charAt(0) == '#')
            texture = texture.substring(1);

        List<String> list = Lists.newArrayList();

        while(true){
            Either<Pair<String,Boolean>,String> either = this.findTextureEntry(texture, modelGetter);
            Optional<Pair<String,Boolean>> optional = either.left();
            if(optional.isPresent())
                return optional.map(pair -> pair.mapFirst(ResourceLocation::new)).get();

            texture = either.right().get();
            if(list.contains(texture)){
                System.err.printf("Unable to resolve texture due to reference chain %s->%s%n", Joiner.on("->").join(list), texture);
                return Pair.of(MissingTextureSprite.getLocation(), false);
            }

            list.add(texture);
        }
    }

    private Either<Pair<String,Boolean>,String> findTextureEntry(String texture, Function<ResourceLocation,IUnbakedModel> modelGetter){
        if(this.textureMap.containsKey(texture)){
            if(this.textureMap.get(texture).getFirst().charAt(0) == '#')
                return Either.right(this.textureMap.get(texture).getFirst().substring(1));
            return Either.left(this.textureMap.get(texture));
        }

        IUnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent instanceof BlockModel){
            if(((BlockModel)parent).customData.hasCustomGeometry() && ((BlockModel)parent).customData.getCustomGeometry() instanceof RechiseledModel)
                return ((RechiseledModel)((BlockModel)parent).customData.getCustomGeometry()).findTextureEntry(texture, modelGetter);
            else{
                for(BlockModel blockmodel = (BlockModel)parent; blockmodel != null; blockmodel = blockmodel.parent){
                    String location = blockmodel.textureMap.get(texture);
                    if(location != null && !location.isEmpty() && !MissingTextureSprite.getLocation().toString().equals(location)){
                        if(location.charAt(0) == '#')
                            return Either.right(location.substring(1));
                        return Either.left(Pair.of(location, false));
                    }
                }
            }
        }

        return Either.left(Pair.of(MissingTextureSprite.getLocation().toString(), false));
    }

    private List<BlockPart> getElements(Function<ResourceLocation,IUnbakedModel> modelGetter){
        List<BlockPart> elements = this.elements;
        if(this.elements.isEmpty()){
            IUnbakedModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
            if(parent instanceof BlockModel){
                if(((BlockModel)parent).customData.hasCustomGeometry()){
                    IModelGeometry<?> geometry = ((BlockModel)parent).customData.getCustomGeometry();
                    if(geometry instanceof RechiseledModel)
                        elements = ((RechiseledModel)geometry).getElements(modelGetter);
                }else
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
            if(((BlockModel)parent).customData.hasCustomGeometry()){
                IModelGeometry<?> geometry = ((BlockModel)parent).customData.getCustomGeometry();
                if(geometry instanceof RechiseledModel)
                    return ((RechiseledModel)geometry).getTransform(transformType, modelGetter);
            }else
                return ((BlockModel)parent).getTransforms().getTransform(transformType);
        }
        return this.cameraTransforms.getTransform(transformType);
    }
}
