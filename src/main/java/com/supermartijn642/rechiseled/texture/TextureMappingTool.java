package com.supermartijn642.rechiseled.texture;

import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class TextureMappingTool {

    /**
     * {@link ExistingFileHelper#clientResources}
     */
    @SuppressWarnings("JavadocReference")
    private static final Field clientResources;

    static{
        Field clientResources_ = null;
        try{
            clientResources_ = ExistingFileHelper.class.getDeclaredField("clientResources");
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }
        clientResources = clientResources_;
    }

    public static Map<Integer,Integer> createPaletteMap(BufferedImage from, BufferedImage to){
        if(from.getWidth() != to.getWidth() || from.getHeight() != to.getHeight())
            throw new IllegalArgumentException("Old palette image must be the same size as new palette image!");

        Map<Integer,Integer> palette = new HashMap<>();
        for(int x = 0; x < from.getWidth(); x++){
            for(int y = 0; y < from.getHeight(); y++){
                palette.put(from.getRGB(x, y), to.getRGB(x, y));
            }
        }
        return palette;
    }

    public static void applyPaletteMap(BufferedImage image, Map<Integer,Integer> palette, boolean ignoreMissingColors, String name){
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int color = image.getRGB(x, y);
                if(color == 0)
                    continue;
                if(!ignoreMissingColors && !palette.containsKey(color))
                    throw new IllegalStateException("Palette is missing color '" + new Color(color) + "' for pattern '" + name + "'");

                image.setRGB(x, y, palette.getOrDefault(color, color));
            }
        }
    }

    /**
     * Finds the suffixes of all rechiseled textures whose names start with {@code name}
     */
    public static List<String> getSuffixes(String name, ExistingFileHelper existingFileHelper){
        IResourceManager resourceManager;
        try{
            clientResources.setAccessible(true);
            resourceManager = (IResourceManager)clientResources.get(existingFileHelper);
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

        List<String> suffixes = new ArrayList<>();
        resourceManager.listResources("textures/block", s -> s.startsWith(name) && s.endsWith(".png"))
            .stream()
            .filter(s -> s.getNamespace().equals("rechiseled"))
            .map(
                s -> {
                    int beginOffset = s.getPath().indexOf(name) + name.length();
                    int end = s.getPath().length() - ".png".length();
                    return s.getPath().substring(beginOffset, end);
                }
            )
            .forEach(suffixes::add);
        return suffixes;
    }
}
