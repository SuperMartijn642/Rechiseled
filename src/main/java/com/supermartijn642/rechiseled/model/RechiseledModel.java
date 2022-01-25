package com.supermartijn642.rechiseled.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.util.Pair;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.*;
import java.util.function.Function;

/**
 * Created 21/12/2021 by SuperMartijn642
 */
public class RechiseledModel implements IModel {

    private static final FaceBakery faceBakery = new FaceBakery();

    private final ResourceLocation modelLocation;
    private final boolean shouldConnect;
    private final ResourceLocation parent;
    private final List<BlockPart> elements;
    private final Map<String,Pair<String,Boolean>> textureMap;
    private final Boolean ambientOcclusion;
    private final Boolean gui3d;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> itemOverrides;

    public RechiseledModel(ResourceLocation modelLocation, boolean shouldConnect, ResourceLocation parent, List<BlockPart> elements, Map<String,Pair<String,Boolean>> textureMap, Boolean ambientOcclusion, Boolean gui3d, ItemCameraTransforms cameraTransforms, List<ItemOverride> itemOverrides){
        this.modelLocation = modelLocation;
        this.shouldConnect = shouldConnect;
        this.parent = parent;
        this.elements = elements;
        this.textureMap = textureMap;
        this.ambientOcclusion = ambientOcclusion;
        this.gui3d = gui3d;
        this.cameraTransforms = cameraTransforms;
        this.itemOverrides = itemOverrides;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation,TextureAtlasSprite> spriteGetter){
        Function<ResourceLocation,IModel> modelGetter = RechiseledModel::getModel;

        TextureAtlasSprite particle = spriteGetter.apply(this.getTexture("particle", modelGetter).first());
        List<BlockPart> elements = this.getElements(modelGetter);
        boolean ambientOcclusion = this.getAmbientOcclusion();
        boolean gui3d = this.getGui3d();
        ItemOverrideList itemOverrides = new ItemOverrideList(this.getItemOverrides());
        ItemCameraTransforms transforms = this.getTransforms(modelGetter);

        Map<EnumFacing,List<Tuple<BakedQuad,Boolean>>> quads = Maps.newEnumMap(EnumFacing.class);

        for(BlockPart part : elements){
            for(EnumFacing direction : part.mapFaces.keySet()){
                BlockPartFace face = part.mapFaces.get(direction);

                Pair<ResourceLocation,Boolean> texture = this.getTexture(face.texture, modelGetter);
                TextureAtlasSprite sprite = spriteGetter.apply(texture.first());
                boolean connecting = texture.second();
                BakedQuad quad = faceBakery.makeBakedQuad(part.positionFrom, part.positionTo, face, sprite, direction, ModelRotation.X0_Y0, part.partRotation, false, part.shade);
                EnumFacing cullFace = state.apply(java.util.Optional.empty()).map(transform -> transform.rotate(face.cullFace)).orElse(face.cullFace);

                quads.putIfAbsent(cullFace, new ArrayList<>());
                quads.get(cullFace).add(new Tuple<>(quad, connecting));
            }
        }

        return this.shouldConnect ?
            new RechiseledConnectedBakedModel(quads, ambientOcclusion, gui3d, false, particle, itemOverrides, transforms) :
            new RechiseledBakedModel(quads, ambientOcclusion, gui3d, false, particle, itemOverrides, transforms);
    }

    @Override
    public Collection<ResourceLocation> getTextures(){
        Set<ResourceLocation> textures = Sets.newHashSet();

        textures.add(this.getTexture("particle", RechiseledModel::getModel).first());

        for(BlockPart part : this.getElements(RechiseledModel::getModel)){
            for(BlockPartFace face : part.mapFaces.values()){
                ResourceLocation texture = this.getTexture(face.texture, RechiseledModel::getModel).first();
                textures.add(texture);
            }
        }

        return textures;
    }

    private Pair<ResourceLocation,Boolean> getTexture(String texture, Function<ResourceLocation,IModel> modelGetter){
        if(texture.charAt(0) == '#')
            texture = texture.substring(1);

        List<String> list = Lists.newArrayList();

        while(true){
            Either<Pair<String,Boolean>,String> either = this.findTextureEntry(texture, modelGetter);
            if(either.isLeft()){
                Pair<String,Boolean> pair = either.left().get();
                return Pair.of(new ResourceLocation(pair.first()), pair.second());
            }

            texture = either.right().get();
            if(list.contains(texture)){
                System.err.printf("Unable to resolve texture due to reference chain %s->%s%n", Joiner.on("->").join(list), texture);
                return Pair.of(new ResourceLocation("missingno"), false);
            }

            list.add(texture);
        }
    }

    private Either<Pair<String,Boolean>,String> findTextureEntry(String texture, Function<ResourceLocation,IModel> modelGetter){
        if(this.textureMap.containsKey(texture)){
            if(this.textureMap.get(texture).first().charAt(0) == '#')
                return new Right<>(this.textureMap.get(texture).first().substring(1));
            return new Left<>(this.textureMap.get(texture));
        }

        IModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent != null){
            if(parent instanceof RechiseledModel)
                return ((RechiseledModel)parent).findTextureEntry(texture, modelGetter);
            else if(parent.asVanillaModel().isPresent()){
                for(ModelBlock blockmodel = parent.asVanillaModel().get(); blockmodel != null; blockmodel = blockmodel.parent){
                    String location = blockmodel.textures.get(texture);
                    if(location != null && !location.isEmpty() && !"missingno".equals(location)){
                        if(location.charAt(0) == '#')
                            return new Right<>(location.substring(1));
                        return new Left<>(Pair.of(location, false));
                    }
                }
            }
        }

        return null;
    }

    private List<BlockPart> getElements(Function<ResourceLocation,IModel> modelGetter){
        List<BlockPart> elements = this.elements;
        if(this.elements.isEmpty()){
            IModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
            if(parent instanceof RechiseledModel)
                elements = ((RechiseledModel)parent).getElements(modelGetter);
            else if(parent != null && parent.asVanillaModel().isPresent())
                elements = parent.asVanillaModel().get().getElements();
        }

        return elements;
    }

    public Boolean getAmbientOcclusion(){
        if(this.ambientOcclusion == null){
            IModel parent = this.parent == null ? null : getModel(this.parent);
            if(parent instanceof RechiseledModel)
                return ((RechiseledModel)parent).getAmbientOcclusion();
            if(parent != null && parent.asVanillaModel().isPresent())
                return parent.asVanillaModel().get().isAmbientOcclusion();
            return true;
        }
        return this.ambientOcclusion;
    }

    public Boolean getGui3d(){
        if(this.gui3d == null){
            IModel parent = this.parent == null ? null : getModel(this.parent);
            if(parent instanceof RechiseledModel)
                return ((RechiseledModel)parent).getGui3d();
            if(parent != null && parent.asVanillaModel().isPresent())
                return parent.asVanillaModel().get().isGui3d();
            return false;
        }
        return this.gui3d;
    }

    public ItemCameraTransforms getTransforms(Function<ResourceLocation,IModel> modelGetter){
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

    private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType transformType, Function<ResourceLocation,IModel> modelGetter){
        if(this.cameraTransforms.hasCustomTransform(transformType))
            return this.cameraTransforms.getTransform(transformType);

        IModel parent = this.parent == null ? null : modelGetter.apply(this.parent);
        if(parent instanceof RechiseledModel)
            return ((RechiseledModel)parent).getTransform(transformType, modelGetter);
        if(parent != null && parent.asVanillaModel().isPresent())
            return parent.asVanillaModel().get().getAllTransforms().getTransform(transformType);

        return this.cameraTransforms.getTransform(transformType);
    }

    public List<ItemOverride> getItemOverrides(){
        if(this.itemOverrides == null){
            IModel parent = this.parent == null ? null : getModel(this.parent);
            if(parent instanceof RechiseledModel)
                return ((RechiseledModel)parent).getItemOverrides();
            if(parent != null && parent.asVanillaModel().isPresent())
                return parent.asVanillaModel().get().getOverrides();
            return Collections.emptyList();
        }
        return this.itemOverrides;
    }

    private static IModel getModel(ResourceLocation location){
        return ModelLoaderRegistry.getModelOrLogError(location, "Could not load vanilla model parent '" + location + "'");
    }
}
