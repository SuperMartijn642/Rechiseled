package com.supermartijn642.rechiseled.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
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
public class RechiseledModel implements IModelGeometry<RechiseledModel> {

    private final boolean shouldConnect;
    private final ResourceLocation parent;
    private final List<BlockPart> elements;
    private final Map<String,Either<Material,String>> textureMap;
    private final boolean ambientOcclusion;
    private final BlockModel.GuiLight guiLight;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> itemOverrides;

    public RechiseledModel(boolean shouldConnect, ResourceLocation parent, List<BlockPart> elements, Map<String,Either<Material,String>> textureMap, boolean ambientOcclusion, BlockModel.GuiLight guiLight, ItemCameraTransforms cameraTransforms, List<ItemOverride> itemOverrides){
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

        TextureAtlasSprite particle = spriteGetter.apply(owner.resolveTexture("particle"));
        List<BlockPart> elements = this.getElements(bakery::getModel);
        ItemCameraTransforms transforms = this.getTransforms(modelGetter);

        Map<Direction,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(Direction.class);

        for(BlockPart part : elements){
            for(Direction direction : part.faces.keySet()){
                BlockPartFace face = part.faces.get(direction);

                TextureAtlasSprite sprite = spriteGetter.apply(owner.resolveTexture(face.texture));
                boolean connecting = ((RechiseledBlockPartFace)face).connecting;
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

        for(BlockPart part : this.getElements(modelGetter)){
            for(BlockPartFace face : part.faces.values()){
                Material texture = owner.resolveTexture(face.texture);
                if(Objects.equals(texture, MissingTextureSprite.getLocation().toString()))
                    missingTextureErrors.add(Pair.of(face.texture, owner.getModelName()));

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
