package com.supermartijn642.rechiseled.texture;

import com.supermartijn642.core.ClientUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class TextureMappingTool {

    private static final ResourceManager RESOURCE_MANAGER;

    static{
        PackRepository packRepository = ClientUtils.getMinecraft().resourcePackRepository;
        packRepository.reload();
        RESOURCE_MANAGER = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, packRepository.openAllSelected());
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

    public static void applyPaletteMap(BufferedImage image, Map<Integer,Integer> palette, String name){
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int color = image.getRGB(x, y);
                if(color == 0)
                    continue;
                if(!palette.containsKey(color))
                    throw new IllegalStateException("Palette is missing color '" + new Color(color) + "' for pattern '" + name + "'");

                image.setRGB(x, y, palette.get(color));
            }
        }
    }

    /**
     * Finds the suffixes of all rechiseled textures whose names start with {@code name}
     */
    public static List<String> getSuffixes(String name){
        List<String> suffixes = new ArrayList<>();
        RESOURCE_MANAGER.listPacks().filter(
            pack -> pack.getNamespaces(PackType.CLIENT_RESOURCES).contains("rechiseled")
        ).forEach(
            pack -> {
                pack.getResources(PackType.CLIENT_RESOURCES, "rechiseled", "textures/block", Integer.MAX_VALUE, s -> s.startsWith(name) && s.endsWith(".png"))
                    .stream()
                    .map(
                        s -> {
                            int beginOffset = s.getPath().indexOf(name) + name.length();
                            int end = s.getPath().length() - ".png".length();
                            return s.getPath().substring(beginOffset, end);
                        }
                    )
                    .forEach(suffixes::add);
            }
        );
        return suffixes;
    }

    public static boolean exists(ResourceLocation location){
        return RESOURCE_MANAGER.hasResource(location);
    }

    public static Resource getResource(ResourceLocation location) throws IOException{
        return RESOURCE_MANAGER.getResource(location);
    }
}
