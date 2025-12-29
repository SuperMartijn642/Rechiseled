package com.supermartijn642.rechiseled.api;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.texture.TextureMappingTool;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public abstract class ChiseledTextureProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final String modid;
    private final DataGenerator generator;
    private final Map<Pair<Identifier,Identifier>,PaletteMap> textures = new HashMap<>();
    private final Set<String> outputLocations = new HashSet<>();
    private final List<String> oakPlankSuffixes;

    public ChiseledTextureProvider(String modid, DataGenerator generator){
        this.modid = modid;
        this.generator = generator;
        this.oakPlankSuffixes = TextureMappingTool.getSuffixes("oak_planks");
    }

    @Override
    public String getName(){
        return "Chiseled Textures: " + this.modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        this.createTextures();

        List<CompletableFuture<?>> tasks = new ArrayList<>();
        Path path = this.generator.getPackOutput().getOutputFolder();
        for(Map.Entry<Pair<Identifier,Identifier>,PaletteMap> entry : this.textures.entrySet()){
            if(entry.getValue().targets.isEmpty())
                continue;

            Pair<BufferedImage,JsonObject> oldPalette = this.loadTexture(entry.getKey().left());
            Pair<BufferedImage,JsonObject> newPalette = this.loadTexture(entry.getKey().right());
            Map<String,Identifier> targets = entry.getValue().targets;
            boolean ignoreMissingColors = entry.getValue().ignoreMissingColors;

            Map<Integer,Integer> colorMap = TextureMappingTool.createPaletteMap(oldPalette.left(), newPalette.left());

            for(Map.Entry<String,Identifier> target : targets.entrySet()){
                Pair<BufferedImage,JsonObject> targetTexture = this.loadTexture(target.getValue());
                String outputLocation = target.getKey();

                TextureMappingTool.applyPaletteMap(targetTexture.left(), colorMap, ignoreMissingColors, outputLocation);

                Path texturePath = path.resolve("assets/" + this.modid + "/textures/" + outputLocation + ".png");
                tasks.add(saveTexture(cache, targetTexture.left(), texturePath));
                if(targetTexture.right() != null){
                    Path textureMetadataPath = path.resolve("assets/" + this.modid + "/textures/" + outputLocation + ".png.mcmeta");
                    tasks.add(DataProvider.saveStable(cache, targetTexture.right(), textureMetadataPath));
                }
            }
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    private Pair<BufferedImage,JsonObject> loadTexture(Identifier location){
        Identifier fullLocation = Identifier.fromNamespaceAndPath(location.getNamespace(), "textures/" + location.getPath() + ".png");
        if(!TextureMappingTool.exists(fullLocation))
            throw new IllegalStateException("Could not find existing texture: " + location);

        // Get the metadata
        Identifier metadataLocation = Identifier.fromNamespaceAndPath(location.getNamespace(), "textures/" + location.getPath() + ".png.mcmeta");
        boolean hasMetadata = TextureMappingTool.exists(metadataLocation);
        JsonObject metadata = null;
        if(hasMetadata){
            try(BufferedReader reader = TextureMappingTool.getResource(metadataLocation).openAsReader()){
                metadata = GSON.fromJson(reader, JsonObject.class);
            }catch(Exception e){
                throw new RuntimeException("Encountered an exception when trying to load texture metadata: " + location, e);
            }
        }

        // Get the texture
        BufferedImage image;
        try(InputStream stream = TextureMappingTool.getResource(fullLocation).open()){
            image = ImageIO.read(stream);
        }catch(Exception e){
            throw new RuntimeException("Encountered an exception when trying to load texture: " + location, e);
        }

        // Convert image to ARGB
        if(image.getType() != BufferedImage.TYPE_INT_ARGB){
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = newImage.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            image = newImage;
        }

        return Pair.of(image, metadata);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static CompletableFuture<?> saveTexture(CachedOutput cache, BufferedImage image, Path path){
        return CompletableFuture.runAsync(() -> {
            try{
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                HashingOutputStream hashingStream = new HashingOutputStream(Hashing.sha1(), byteStream);
                ImageIO.write(image, "png", hashingStream);
                cache.writeIfNeeded(path, byteStream.toByteArray(), hashingStream.hash());
            }catch(IOException iOException){
                LOGGER.error("Couldn't save texture to {}", path, iOException);
            }
        }, Util.backgroundExecutor());
    }

    private boolean validateTexture(Identifier texture){
        Identifier fullLocation = Identifier.fromNamespaceAndPath(texture.getNamespace(), "textures/" + texture.getPath() + ".png");
        return TextureMappingTool.exists(fullLocation);
    }

    /**
     * A palette map generates a map of colors from one texture to another. The map
     * can then be applied to a variant of the 'from texture' to generate a similar
     * variant of the 'to texture'.<br>
     * The 'from' and 'to' texture need to have a similar pattern. For example,
     * a palette map can map variants of oak planks to variants of birch planks.
     *
     *
     * <p>A palette map can be obtained using {@link #createPaletteMap(Identifier, Identifier)}
     * and applied using {@link PaletteMap#applyToTexture(Identifier, String)}.
     * All mapped textures will be saved and written to file automatically.
     *
     * <p>{@link #createPlankTextures(Identifier, String)} will map all rechiseled's
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
    protected PaletteMap createPaletteMap(Identifier oldPalette, Identifier newPalette){
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
    protected void createPlankTextures(Identifier plankTexture, String outputLocation){
        if(!this.validateTexture(plankTexture))
            throw new IllegalStateException("Could not find texture '" + plankTexture + "'!");
        if(outputLocation == null || outputLocation.trim().isEmpty())
            throw new IllegalArgumentException("Output location must not be empty!");
        if(!ChiseledTextureProvider.this.outputLocations.add(outputLocation))
            throw new IllegalStateException("Two or more textures have the same output location: " + outputLocation);

        PaletteMap paletteMap = this.createPaletteMap(Identifier.fromNamespaceAndPath("minecraft", "block/oak_planks"), plankTexture);

        for(String suffix : this.oakPlankSuffixes){
            paletteMap.applyToTexture(Identifier.fromNamespaceAndPath("rechiseled", "block/oak_planks" + suffix), outputLocation + suffix);
        }
    }

    protected class PaletteMap {

        private final Map<String,Identifier> targets = new HashMap<>();
        private boolean ignoreMissingColors = false;

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
        public PaletteMap applyToTexture(Identifier texture, String outputLocation){
            if(!ChiseledTextureProvider.this.validateTexture(texture))
                throw new IllegalStateException("Could not find texture '" + texture + "'!");
            if(outputLocation == null || outputLocation.trim().isEmpty())
                throw new IllegalArgumentException("Output location must not be empty!");
            if(!ChiseledTextureProvider.this.outputLocations.add(outputLocation))
                throw new IllegalStateException("Two or more textures have the same output location: " + outputLocation);

            this.targets.put(outputLocation.toLowerCase(Locale.ROOT).trim(), texture);
            return this;
        }

        /**
         * When the palette map is applied, any pixels with missing colors will be kept.
         */
        public PaletteMap ignoreMissing(){
            this.ignoreMissingColors = true;
            return this;
        }
    }
}
