package com.supermartijn642.rechiseled.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.supermartijn642.core.registry.RegistryUtil;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class ConnectingModelBuilder {

    protected final String modid;
    protected final ResourceLocation identifier;
    private final Map<String,String> textures = new HashMap<>();
    // whether a texture is a texture sheet or a regular texture
    protected final Map<String,Boolean> connectingTextures = new LinkedHashMap<>();
    private boolean shouldConnect = false;
    private final Map<ItemTransforms.TransformType,TransformBuilder> transforms = new HashMap<>();
    private final List<ElementBuilder> elements = new ArrayList<>();
    private ResourceLocation parent;
    private boolean ambientOcclusion = true;
    private BlockModel.GuiLight lighting = null;

    protected ConnectingModelBuilder(String modid, ResourceLocation identifier){
        this.modid = modid;
        this.identifier = identifier;
    }

    /**
     * Sets the parent model.
     * @param model the parent model location
     */
    public ConnectingModelBuilder parent(ResourceLocation model){
        if(this.identifier.equals(model))
            throw new IllegalArgumentException("Cannot add self as parent model '" + model + "'!");

        this.parent = model;
        return this;
    }

    /**
     * Sets the parent model.
     * @param namespace namespace of the parent model location
     * @param path      path of the parent model location
     */
    public ConnectingModelBuilder parent(String namespace, String path){
        return this.parent(new ResourceLocation(namespace, path));
    }

    /**
     * Sets the parent model.
     * @param model the parent model location
     */
    public ConnectingModelBuilder parent(String model){
        return this.parent(this.modid, model);
    }

    /**
     * Indicate that any connecting textures should connect to touching blocks.
     */
    public ConnectingModelBuilder connectToOtherBlocks(){
        this.shouldConnect = true;
        return this;
    }

    /**
     * Indicate whether any connecting textures should connect to touching blocks.
     * If false, any connecting textures will show their top left texture (the fully
     * bordered one).
     */
    public ConnectingModelBuilder connectToOtherBlocks(boolean connect){
        this.shouldConnect = connect;
        return this;
    }

    /**
     * Sets whether ambient occlusion should be applied when rendering this model.
     */
    public ConnectingModelBuilder ambientOcclusion(boolean useAmbientOcclusion){
        this.ambientOcclusion = useAmbientOcclusion;
        return this;
    }

    /**
     * Sets no ambient occlusion to be applied when rendering this model.
     */
    public ConnectingModelBuilder noAmbientOcclusion(){
        return this.ambientOcclusion(false);
    }

    /**
     * Sets the lighting used when rendering this model in a gui to FRONT.
     */
    public ConnectingModelBuilder frontLit(){
        this.lighting = BlockModel.GuiLight.FRONT;
        return this;
    }

    /**
     * Sets the lighting used when rendering this model in a gui to SIDE.
     */
    public ConnectingModelBuilder sideLit(){
        this.lighting = BlockModel.GuiLight.SIDE;
        return this;
    }

    /**
     * Puts the given texture under the given key. These keys may be used when on faces for elements of this model.
     * @param key     key to be assigned
     * @param texture texture to be assigned to the given key
     */
    public ConnectingModelBuilder texture(String key, ResourceLocation texture){
        this.textures.put(key, texture.toString());
        this.connectingTextures.put(key, false);
        return this;
    }

    /**
     * Puts the given texture or reference under the given key. These keys may be used when on faces for elements of this model.
     * @param key     key to be assigned
     * @param texture texture or reference to another key to be assigned to the given key
     */
    public ConnectingModelBuilder texture(String key, String texture){
        if(texture.charAt(0) != '#' && !RegistryUtil.isValidIdentifier(texture))
            throw new IllegalArgumentException("Texture entry must either start with '#' or be a valid resource location, not '" + texture + "'!");

        if(texture.charAt(0) != '#')
            return this.texture(key, texture.contains(":") ? new ResourceLocation(texture) : new ResourceLocation(this.modid, texture));
        this.textures.put(key, texture);
        this.connectingTextures.put(key, false);
        return this;
    }

    /**
     * Puts the given texture or reference under the given key. These keys may be used when on faces for elements of this model.
     * @param key        key to be assigned
     * @param namespace  namespace of the texture
     * @param identifier path of the texture
     */
    public ConnectingModelBuilder texture(String key, String namespace, String identifier){
        if(!RegistryUtil.isValidNamespace(namespace))
            throw new IllegalArgumentException("Namespace '" + namespace + "' must only contain characters [a-z0-9_.-]!");
        if(!RegistryUtil.isValidPath(identifier))
            throw new IllegalArgumentException("Identifier '" + identifier + "' must only contain characters [a-z0-9_./-]!");

        this.texture(key, new ResourceLocation(namespace, identifier));
        return this;
    }

    /**
     * Set the texture for a given dictionary key.
     * @param key                 the texture key
     * @param texture             the texture, can be another reference e.g. '#all'
     * @param isConnectingTexture whether the texture is a connecting texture
     * @throws NullPointerException     when {@code key} is {@code null}
     * @throws NullPointerException     when {@code texture} is {@code null}
     * @throws IllegalStateException    when {@code texture} is not a reference (does not start
     *                                  with '#') and does not exist
     * @throws IllegalArgumentException when {@code texture} is a reference (starts
     *                                  with '#') and {@code isConnectingTexture} is {@code true}
     */
    public ConnectingModelBuilder texture(String key, String texture, boolean isConnectingTexture){
        if(isConnectingTexture && texture.charAt(0) == '#')
            throw new IllegalArgumentException("Only textures can be 'connecting', references (starts with '#') cannot: " + texture);

        this.texture(key, texture);
        this.connectingTextures.put(key, isConnectingTexture);
        return this;
    }

    /**
     * Set the texture for a given dictionary key.
     * @param key                 the texture key
     * @param texture             the texture
     * @param isConnectingTexture whether the texture is a connecting texture
     * @throws NullPointerException  when {@code key} is {@code null}
     * @throws NullPointerException  when {@code texture} is {@code null}
     * @throws IllegalStateException when {@code texture} does not exist
     */
    public ConnectingModelBuilder texture(String key, ResourceLocation texture, boolean isConnectingTexture){
        this.texture(key, texture);
        this.connectingTextures.put(key, isConnectingTexture);
        return this;
    }

    /**
     * Sets the given texture to be used for the particles from this model.
     * @param texture texture for the particles
     */
    public ConnectingModelBuilder particleTexture(ResourceLocation texture){
        return this.texture("particle", texture);
    }

    /**
     * Sets the given texture or reference to be used for the particles from this model.
     * @param texture texture or reference to a key for the particles
     */
    public ConnectingModelBuilder particleTexture(String texture){
        return this.texture("particle", texture);
    }

    /**
     * Sets the given texture to be used for the particles from this model.
     * @param namespace  namespace of the texture
     * @param identifier path of the texture
     */
    public ConnectingModelBuilder particleTexture(String namespace, String identifier){
        return this.texture("particle", namespace, identifier);
    }

    /**
     * Constructs a transformation for the given transform type to be used when rendering this model.
     * @param transformType            transform type to be build
     * @param transformBuilderConsumer consumer to build the transformation
     */
    public ConnectingModelBuilder transform(ItemTransforms.TransformType transformType, Consumer<TransformBuilder> transformBuilderConsumer){
        transformBuilderConsumer.accept(this.transforms.computeIfAbsent(transformType, o -> new TransformBuilder()));
        return this;
    }

    /**
     * Constructs a new element for this model.
     * @param elementBuilderConsumer consumer to build the element
     */
    public ConnectingModelBuilder element(Consumer<ElementBuilder> elementBuilderConsumer){
        ElementBuilder builder = new ElementBuilder();
        elementBuilderConsumer.accept(builder);
        this.elements.add(builder);
        return this;
    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject();

        // Parent model
        if(this.parent != null)
            json.addProperty("parent", this.parent.toString());
        // Ambient occlusion
        if(!this.ambientOcclusion)
            json.addProperty("ambientocclusion", false);
        // Transforms
        if(!this.transforms.isEmpty()){
            JsonObject displayJson = new JsonObject();
            // Add each transform
            for(Map.Entry<ItemTransforms.TransformType,TransformBuilder> transform : this.transforms.entrySet()){
                JsonObject transformJson = new JsonObject();
                transformJson.add("rotation", createJsonArray(transform.getValue().rotation.x(), transform.getValue().rotation.y(), transform.getValue().rotation.z()));
                transformJson.add("translation", createJsonArray(transform.getValue().translation.x(), transform.getValue().translation.y(), transform.getValue().translation.z()));
                transformJson.add("scale", createJsonArray(transform.getValue().scale.x(), transform.getValue().scale.y(), transform.getValue().scale.z()));
                String transformName = "unknown";
                if(transform.getKey() == ItemTransforms.TransformType.NONE)
                    transformName = "none";
                else if(transform.getKey() == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
                    transformName = "thirdperson_lefthand";
                else if(transform.getKey() == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                    transformName = "thirdperson_righthand";
                else if(transform.getKey() == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                    transformName = "firstperson_lefthand";
                else if(transform.getKey() == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                    transformName = "firstperson_righthand";
                else if(transform.getKey() == ItemTransforms.TransformType.HEAD)
                    transformName = "head";
                else if(transform.getKey() == ItemTransforms.TransformType.GUI)
                    transformName = "gui";
                else if(transform.getKey() == ItemTransforms.TransformType.GROUND)
                    transformName = "ground";
                else if(transform.getKey() == ItemTransforms.TransformType.FIXED)
                    transformName = "fixed";
                displayJson.add(transformName, transformJson);
            }
            json.add("display", displayJson);
        }
        // Textures
        if(!this.textures.isEmpty()){
            JsonObject texturesJson = new JsonObject();
            for(Map.Entry<String,String> entry : this.textures.entrySet())
                texturesJson.addProperty(entry.getKey(), entry.getValue());
            json.add("textures", texturesJson);
        }
        // Gui lighting
        if(this.lighting != null)
            json.addProperty("gui_light", this.lighting == BlockModel.GuiLight.FRONT ? "front" : "side");
        // Elements
        if(!this.elements.isEmpty()){
            JsonArray elementsJson = new JsonArray();
            // Loop over the individual elements
            for(ElementBuilder element : this.elements){
                JsonObject elementJson = new JsonObject();
                // From & to
                elementJson.add("from", createJsonArray(element.from.x(), element.from.y(), element.from.z()));
                elementJson.add("to", createJsonArray(element.to.x(), element.to.y(), element.to.z()));
                // Rotation
                if(element.rotation != null){
                    JsonObject rotationJson = new JsonObject();
                    rotationJson.add("origin", createJsonArray(element.rotation.origin.x(), element.rotation.origin.y(), element.rotation.origin.z()));
                    rotationJson.addProperty("axis", element.rotation.axis.getSerializedName());
                    rotationJson.addProperty("angle", element.rotation.angle);
                    rotationJson.addProperty("rescale", element.rotation.rescale);
                    elementJson.add("rotation", rotationJson);
                }
                // Shade
                if(!element.shading)
                    elementJson.addProperty("shade", false);
                // Faces
                if(!element.faces.isEmpty()){
                    JsonObject facesJson = new JsonObject();
                    for(Map.Entry<Direction,FaceBuilder> entry : element.faces.entrySet()){
                        JsonObject faceJson = new JsonObject();
                        if(entry.getValue().texture == null)
                            throw new RuntimeException("Model '" + this.identifier + "' has face without a texture!");
                        faceJson.addProperty("texture", entry.getValue().texture);
                        if(entry.getValue().uv != null)
                            faceJson.add("uv", createJsonArray(entry.getValue().uv));
                        if(entry.getValue().cullface != null)
                            faceJson.addProperty("cullface", entry.getValue().cullface.getSerializedName());
                        if(entry.getValue().emissivity != 0)
                            faceJson.addProperty("emissivity", entry.getValue().emissivity);
                        if(entry.getValue().rotation != 0)
                            faceJson.addProperty("rotation", entry.getValue().rotation);
                        if(entry.getValue().tintIndex != -1)
                            faceJson.addProperty("tintindex", entry.getValue().tintIndex);
                        facesJson.add(entry.getKey().getSerializedName(), faceJson);
                    }
                    elementJson.add("faces", facesJson);
                }else
                    throw new RuntimeException("Element in model '" + this.identifier + "' has no faces!");
                elementsJson.add(elementJson);
            }
            json.add("elements", elementsJson);
        }

        // Set the 'loader' property
        json.addProperty("loader", "rechiseled:connecting_model");

        // Set the 'should_connect' property
        if(this.shouldConnect)
            json.addProperty("should_connect", true);

        // Set 'connecting' property for all textures
        if(!this.textures.isEmpty() && !this.connectingTextures.isEmpty() && json.has("textures")){
            JsonObject textures = json.remove("textures").getAsJsonObject();
            for(Map.Entry<String,Boolean> e : this.connectingTextures.entrySet()){
                textures.remove(e.getKey());
                JsonObject textureJson = new JsonObject();
                textureJson.addProperty("location", this.textures.get(e.getKey()));
                textureJson.addProperty("connecting", e.getValue());
                textures.add(e.getKey(), textureJson);
            }
            json.add("model_textures", textures);
        }

        return json;
    }

    private static JsonArray createJsonArray(float... elements){
        // Because they can't just make a proper json array constructor...
        JsonArray array = new JsonArray(elements.length);
        for(Number element : elements)
            array.add(element);
        return array;
    }

    public static class TransformBuilder {

        private Vector3f rotation = new Vector3f(ItemTransform.Deserializer.DEFAULT_ROTATION);
        private Vector3f translation = new Vector3f(ItemTransform.Deserializer.DEFAULT_TRANSLATION);
        private Vector3f scale = new Vector3f(ItemTransform.Deserializer.DEFAULT_SCALE);

        protected TransformBuilder(){
        }

        /**
         * Sets the rotation.
         * @param x rotation around the x-axis
         * @param y rotation around the y-axis
         * @param z rotation around the z-axis
         */
        public TransformBuilder rotation(float x, float y, float z){
            this.rotation = new Vector3f(x, y, z);
            return this;
        }

        /**
         * Sets the translation.
         * @param x translation on the x-axis
         * @param y translation on the y-axis
         * @param z translation on the z-axis
         */
        public TransformBuilder translation(float x, float y, float z){
            this.translation = new Vector3f(x, y, z);
            return this;
        }

        /**
         * Sets the scaling.
         * @param x scaling on the x-axis
         * @param y scaling on the y-axis
         * @param z scaling on the z-axis
         */
        public TransformBuilder scale(float x, float y, float z){
            this.scale = new Vector3f(x, y, z);
            return this;
        }

        /**
         * Sets the scaling for all axis.
         * @param scale scaling for all axis
         */
        public TransformBuilder scale(float scale){
            return this.scale(scale, scale, scale);
        }
    }

    public static class ElementBuilder {

        private final Map<Direction,FaceBuilder> faces = new HashMap<>();
        private Vector3f from = new Vector3f(), to = new Vector3f(16, 16, 16);
        private RotationBuilder rotation;
        private boolean shading = true;

        protected ElementBuilder(){
        }

        /**
         * Sets the from-position of this element.
         * @param from position which the element starts at
         */
        public ElementBuilder from(Vector3f from){
            this.from = from;
            return this;
        }

        /**
         * Sets the from-position of this element.
         * @param x x-position which the element starts at
         * @param y y-position which the element starts at
         * @param z z-position which the element starts at
         */
        public ElementBuilder from(float x, float y, float z){
            return this.from(new Vector3f(x, y, z));
        }

        /**
         * Sets the to-position of this element.
         * @param to position which the element ends at
         */
        public ElementBuilder to(Vector3f to){
            this.to = to;
            return this;
        }

        /**
         * Sets the to-position of this element.
         * @param x x-position which the element ends at
         * @param y y-position which the element ends at
         * @param z z-position which the element ends at
         */
        public ElementBuilder to(float x, float y, float z){
            return this.to(new Vector3f(x, y, z));
        }

        /**
         * Sets the start and end position of this element.
         * @param from the start position
         * @param to   the end position
         */
        public ElementBuilder shape(Vector3f from, Vector3f to){
            this.from(from);
            this.to(to);
            return this;
        }

        /**
         * Sets the start and end position of this element.
         */
        public ElementBuilder shape(float minX, float minY, int minZ, float maxX, float maxY, float maxZ){
            return this.shape(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
        }

        /**
         * Sets whether shading should be applied when rendering this element.
         * @param doShading whether shading should be applied
         */
        public ElementBuilder shading(boolean doShading){
            this.shading = doShading;
            return this;
        }

        /**
         * Sets no shading to be applied when rendering this element.
         */
        public ElementBuilder noShading(){
            return this.shading(false);
        }

        /**
         * Constructs a face for the given side of this element.
         * @param side                side to be constructed
         * @param faceBuilderConsumer consumer to build the face
         */
        public ElementBuilder face(Direction side, Consumer<FaceBuilder> faceBuilderConsumer){
            faceBuilderConsumer.accept(this.faces.computeIfAbsent(side, FaceBuilder::new));
            return this;
        }

        /**
         * Constructs faces for all sides of this element
         * @param faceBuilderFunction function to build the faces, returns whether to discard the face
         */
        public ElementBuilder allFaces(BiFunction<Direction,FaceBuilder,Boolean> faceBuilderFunction){
            for(Direction side : Direction.values()){
                if(!faceBuilderFunction.apply(side, this.faces.computeIfAbsent(side, FaceBuilder::new)))
                    this.faces.remove(side);
            }
            return this;
        }

        /**
         * Constructs faces for all sides of this element
         * @param faceBuilderConsumer consumer to build the faces
         */
        public ElementBuilder allFaces(BiConsumer<Direction,FaceBuilder> faceBuilderConsumer){
            return this.allFaces(((direction, faceBuilder) -> {
                faceBuilderConsumer.accept(direction, faceBuilder);
                return true;
            }));
        }

        /**
         * Constructs faces for all sides of this element
         * @param faceBuilderConsumer consumer to build the faces
         */
        public ElementBuilder allFaces(Consumer<FaceBuilder> faceBuilderConsumer){
            return this.allFaces(((direction, faceBuilder) -> {
                faceBuilderConsumer.accept(faceBuilder);
                return true;
            }));
        }

        /**
         * Constructs the rotation of this element.
         * @param rotationBuilderConsumer consumer to build the rotation
         */
        public ElementBuilder rotation(Consumer<RotationBuilder> rotationBuilderConsumer){
            if(this.rotation == null)
                this.rotation = new RotationBuilder();
            rotationBuilderConsumer.accept(this.rotation);
            return this;
        }
    }

    public static class RotationBuilder {

        private Vector3f origin;
        private Direction.Axis axis;
        private float angle;
        private boolean rescale;

        protected RotationBuilder(){
        }

        /**
         * Sets the origin for the rotation transformation.
         * @param origin position to be rotated around
         */
        public RotationBuilder origin(Vector3f origin){
            this.origin = origin;
            return this;
        }

        /**
         * Sets the origin for the rotation transformation.
         * @param x x-position to be rotated around
         * @param y y-position to be rotated around
         * @param z z-position to be rotated around
         */
        public RotationBuilder origin(float x, float y, float z){
            return this.origin(new Vector3f(x, y, z));
        }

        /**
         * Sets the axis which should rotated around.
         * @param axis axis of rotation
         */
        public RotationBuilder axis(Direction.Axis axis){
            this.axis = axis;
            return this;
        }

        /**
         * Sets the angle to be rotated by. Angle must be one of -45, -22.5, 0, 22.5, or 45.
         * @param angle angle to be rotated by
         */
        public RotationBuilder angle(float angle){
            if(angle != 0 && Math.abs(angle) != 22.5f && Math.abs(angle) != 45)
                throw new IllegalArgumentException("Angle must be one of -45, -22.5, 0, 22.5, or 45, not '" + angle + "'!");

            this.angle = angle;
            return this;
        }

        /**
         * Sets whether to rescale the faces to across the whole block.
         * @param rescale whether to rescale the faces
         */
        public RotationBuilder rescale(boolean rescale){
            this.rescale = rescale;
            return this;
        }

        /**
         * Sets the faces to be rescaled across the whole block.
         */
        public RotationBuilder rescale(){
            return this.rescale(true);
        }
    }

    public static class FaceBuilder {

        private final Direction side;
        private float[] uv;
        private String texture;
        private Direction cullface;
        private int rotation = 0;
        private int tintIndex = -1;
        private int emissivity = 0;

        protected FaceBuilder(Direction side){
            this.side = side;
        }

        /**
         * Sets the texture uv coordinates for this face. If no uv is set, the coordinates will be determined from the relevant element.
         */
        public FaceBuilder uv(float minX, float minY, float maxX, float maxY){
            this.uv = new float[]{minX, minY, maxX, maxY};
            return this;
        }

        /**
         * Sets the texture to be used on this face. Must be a reference to a key in the top level textures of the model.
         */
        public FaceBuilder texture(String reference){
            if(!(reference.charAt(0) == '#' ? reference.substring(1) : reference).matches("[a-zA-Z_-]*"))
                throw new IllegalArgumentException("Texture reference '" + reference + "' must only contain characters [a-zA-Z_-]!");

            this.texture = reference.charAt(0) == '#' ? reference : "#" + reference;
            return this;
        }

        /**
         * Sets the side which should be covered for this face to be culled, may be {@code null}.
         * @param side side which should be covered
         */
        public FaceBuilder cullface(Direction side){
            this.cullface = side;
            return this;
        }

        /**
         * Sets the side which should be covered for this face to be culled to the side which this face is on.
         */
        public FaceBuilder cullface(){
            this.cullface(this.side);
            return this;
        }

        /**
         * Sets the rotation of the texture. Must be a multiple of 90, default rotation is 0.
         * @param rotation rotation of the texture
         */
        public FaceBuilder rotation(int rotation){
            if(rotation % 90 != 0)
                throw new IllegalArgumentException("Rotation must be a multiple of 90, not '" + rotation + "'!");

            this.rotation = rotation;
            return this;
        }

        /**
         * Sets the tint index for this face.
         * The effect of the tint index depends on the {@link BlockColor} registered for the block which this model is used for.
         * A tint index of -1 means no tinting will be applied.
         */
        public FaceBuilder tintIndex(int index){
            this.tintIndex = index;
            return this;
        }

        /**
         * Sets the emissivity of this face. Must be in the range 0 to 15.
         */
        public FaceBuilder emissivity(int emissivity){
            if(emissivity < 0 || emissivity > 15)
                throw new IllegalArgumentException("Emissivity must be between 0 and 15, not '" + emissivity + "'!");

            this.emissivity = emissivity;
            return this;
        }
    }
}
