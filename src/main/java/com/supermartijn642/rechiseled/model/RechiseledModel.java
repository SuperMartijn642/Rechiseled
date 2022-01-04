package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
    private final Map<String,String> textureMap;
    private final ItemCameraTransforms cameraTransforms;

    public RechiseledModel(boolean shouldConnect, ResourceLocation parent, List<BlockPart> elements, Map<String,String> textureMap, boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms cameraTransforms, List<ItemOverride> itemOverrides){
        this.shouldConnect = shouldConnect;
        this.parent = parent;
        this.elements = elements;
        this.textureMap = textureMap;
        this.cameraTransforms = cameraTransforms;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<ResourceLocation,TextureAtlasSprite> spriteGetter, ISprite iSprite, VertexFormat format, ItemOverrideList overrides){
        Function<ResourceLocation,IUnbakedModel> modelGetter = bakery::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(this.getTexture("#particle", bakery::getModel));
        List<BlockPart> elements = this.getElements(bakery::getModel);
        ItemCameraTransforms transforms = this.getTransforms(modelGetter);

        Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockPart part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockPartFace face = part.faces.get(direction);

                TextureAtlasSprite sprite = spriteGetter.apply(this.getTexture(face.texture, bakery::getModel));
                boolean connecting = ((RechiseledBlockPartFace)face).connecting;
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

    private ResourceLocation getTexture(String name, Function<ResourceLocation,IUnbakedModel> modelGetter){
        while(name != null && name.charAt(0) == '#')
            name = this.resolveTexture(name, modelGetter);

        return name == null ? MissingTextureSprite.getLocation() : new ResourceLocation(name);
    }

    private String resolveTexture(String name, Function<ResourceLocation,IUnbakedModel> modelGetter){
        if(this.textureMap.containsKey(name.substring(1)))
            return this.textureMap.get(name.substring(1));

        if(this.parent != null){
            IUnbakedModel parent = modelGetter.apply(this.parent);

            if(parent instanceof BlockModel){
                if(((BlockModel)parent).customData.hasCustomGeometry()){
                    IModelGeometry<?> geometry = ((BlockModel)parent).customData.getCustomGeometry();
                    if(geometry instanceof RechiseledModel)
                        return ((RechiseledModel)geometry).resolveTexture(name, modelGetter);
                }else{
                    String location = ((BlockModel)parent).getTexture(name);
                    if(!MissingTextureSprite.getLocation().toString().equals(location))
                        return location;
                }
            }
        }

        return null;
    }

    @Override
    public Collection<ResourceLocation> getTextureDependencies(IModelConfiguration owner, Function<ResourceLocation,IUnbakedModel> modelGetter, Set<String> missingTextureErrors){
        Set<ResourceLocation> textures = Sets.newHashSet();

        textures.add(this.getTexture("#particle", modelGetter));

        for(BlockPart part : this.getElements(modelGetter)){
            for(BlockPartFace face : part.faces.values()){
                ResourceLocation texture = this.getTexture(face.texture, modelGetter);
                if(Objects.equals(texture, MissingTextureSprite.getLocation()))
                    missingTextureErrors.add(String.format("%s in ?", face.texture));

                textures.add(texture);
            }
        }

        return textures;
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
