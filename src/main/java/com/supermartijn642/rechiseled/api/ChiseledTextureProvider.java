package com.supermartijn642.rechiseled.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.supermartijn642.rechiseled.texture.TextureMappingTool;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public abstract class ChiseledTextureProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String modid;
    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;
    private final Map<Pair<ResourceLocation,ResourceLocation>,PaletteMap> textures = new HashMap<>();
    private final Set<String> outputLocations = new HashSet<>();
    private final List<String> oakPlankSuffixes;

    public ChiseledTextureProvider(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper){
        this.modid = modid;
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
        this.oakPlankSuffixes = TextureMappingTool.getSuffixes("oak_planks", existingFileHelper);
    }

    @Override
    public String getName(){
        return "Chiseled Textures: " + this.modid;
    }

    @Override
    public void run(HashCache cache){
        this.createTextures();

        Path path = this.generator.getOutputFolder();
        for(Map.Entry<Pair<ResourceLocation,ResourceLocation>,PaletteMap> entry : this.textures.entrySet()){
            if(entry.getValue().targets.isEmpty())
                continue;

            BufferedImage oldPalette = this.loadTexture(entry.getKey().getLeft());
            BufferedImage newPalette = this.loadTexture(entry.getKey().getRight());
            Map<String,ResourceLocation> targets = entry.getValue().targets;

            Map<Integer,Integer> colorMap = TextureMappingTool.createPaletteMap(oldPalette, newPalette);

            for(Map.Entry<String,ResourceLocation> target : targets.entrySet()){
                BufferedImage targetTexture = this.loadTexture(target.getValue());
                String outputLocation = target.getKey();

                TextureMappingTool.applyPaletteMap(targetTexture, colorMap, outputLocation);

                Path texturePath = path.resolve("assets/" + this.modid + "/textures/" + outputLocation + ".png");
                saveTexture(cache, targetTexture, texturePath);
            }
        }
    }

    private BufferedImage loadTexture(ResourceLocation location){
        if(!this.existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".png", "textures"))
            throw new IllegalStateException("Could not find existing texture: " + location);

        try(Resource resource = this.existingFileHelper.getResource(location, PackType.CLIENT_RESOURCES, ".png", "textures")){
            return ImageIO.read(resource.getInputStream());
        }catch(Exception e){
            throw new RuntimeException("Encountered an exception when trying to load texture: " + location, e);
        }
    }

    private static void saveTexture(HashCache cache, BufferedImage image, Path path){
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String hash = SHA1.hashBytes(bytes).toString();
            if(!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)){
                Files.createDirectories(path.getParent());

                try(OutputStream outputStream = Files.newOutputStream(path)){
                    outputStream.write(bytes);
                }
            }

            cache.putNew(path, hash);
        }catch(IOException exception){
            System.err.println("Couldn't save texture '" + path + "'");
            exception.printStackTrace();
        }
    }

    private boolean validateTexture(ResourceLocation texture){
        return this.existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures");
    }

    private void trackTexture(String outputLocation){
        this.existingFileHelper.trackGenerated(new ResourceLocation(this.modid, outputLocation), PackType.CLIENT_RESOURCES, ".png", "textures");
    }

    /**
     * A palette map generates a map of colors from one texture to another. The map
     * can then be applied to a variant of the 'from texture' to generate a similar
     * variant of the 'to texture'.<br>
     * The 'from' and 'to' texture need to have a similar pattern. For example,
     * a palette map can map variants of oak planks to variants of birch planks.
     *
     *
     * <p>A palette map can be obtained using {@link #createPaletteMap(ResourceLocation, ResourceLocation)}
     * and applied using {@link PaletteMap#applyToTexture(ResourceLocation, String)}.
     * All mapped textures will be saved and written to file automatically.
     *
     * <p>{@link #createPlankTextures(ResourceLocation, String)} will map all rechiseled's
     * oak plank variants to the given plank texture.
     */
    protected abstract void createTextures();

    /**
     * Creates a map from colors in {@code oldPalette} to {@code newPalette}.
     * The palette map can be applied to any texture using the old palette
     * to generate a texture with the new palette.
     * @param oldPalette colors to map from
     * @param newPalette colors to map to
     * @throws IllegalArgumentException when {@code oldPalette} or {@code newPalette} is {@code null}
     */
    protected PaletteMap createPaletteMap(ResourceLocation oldPalette, ResourceLocation newPalette){
        if(!this.validateTexture(oldPalette))
            throw new IllegalStateException("Could not find texture '" + oldPalette + "'!");
        if(!this.validateTexture(newPalette))
            throw new IllegalStateException("Could not find texture '" + newPalette + "'!");

        return this.textures.computeIfAbsent(Pair.of(oldPalette, newPalette), o -> new PaletteMap());
    }

    /**
     * Maps all oak plank variants in rechiseled to the given plank texture.
     * The textures will be saved with the same suffixes as rechiseled's oak plank
     * textures suffixes.<br>
     * For example, the mapped texture of <i>'assets/rechiseled/textures/oak_planks_bricks.png'</i>
     * will be saved at <i>'assets/<b>modid</b>/textures/<b>outputLocation</b>_bricks.png'</i>.
     */
    protected void createPlankTextures(ResourceLocation plankTexture, String outputLocation){
        if(!this.validateTexture(plankTexture))
            throw new IllegalStateException("Could not find texture '" + plankTexture + "'!");
        if(outputLocation == null || outputLocation.trim().isEmpty())
            throw new IllegalArgumentException("Output location must not be empty!");
        if(!ChiseledTextureProvider.this.outputLocations.add(outputLocation))
            throw new IllegalStateException("Two or more textures have the same output location: " + outputLocation);

        PaletteMap paletteMap = this.createPaletteMap(new ResourceLocation("minecraft", "block/oak_planks"), plankTexture);

        for(String suffix : this.oakPlankSuffixes){
            paletteMap.applyToTexture(new ResourceLocation("rechiseled", "block/oak_planks" + suffix), outputLocation + suffix);
        }
    }

    protected class PaletteMap {

        private final Map<String,ResourceLocation> targets = new HashMap<>();

        private PaletteMap(){
        }

        /**
         * Applies this palette map to the given texture, i.e. colors in the given
         * texture will be swapped for the corresponding color in this palette map.
         * The given texture should consist of exclusively colors used in the
         * 'from palette' of this palette map.
         * <p>
         * The mapped texture will be saved at <i>assets/<b>modid</b>/textures/<b>outputLocation</b>.png</i>.
         * @param texture        the texture to apply this palette map to
         * @param outputLocation location to save the mapped texture at
         * @throws IllegalArgumentException when {@code texture} is {@code null}, or when {@code outputLocation.trim()} is {@code null} or empty
         * @throws IllegalStateException    when there is already a texture to be generated for {@code outputLocation}
         */
        public PaletteMap applyToTexture(ResourceLocation texture, String outputLocation){
            if(!ChiseledTextureProvider.this.validateTexture(texture))
                throw new IllegalStateException("Could not find texture '" + texture + "'!");
            if(outputLocation == null || outputLocation.trim().isEmpty())
                throw new IllegalArgumentException("Output location must not be empty!");
            if(!ChiseledTextureProvider.this.outputLocations.add(outputLocation))
                throw new IllegalStateException("Two or more textures have the same output location: " + outputLocation);

            this.targets.put(outputLocation.toLowerCase(Locale.ROOT).trim(), texture);
            ChiseledTextureProvider.this.trackTexture(outputLocation);
            return this;
        }
    }
}
