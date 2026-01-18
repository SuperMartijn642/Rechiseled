package com.supermartijn642.texturegenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created 27/12/2021 by SuperMartijn642
 */
public class TextureGenerator {

    public static void main(String[] args){
        generateFromBase("wood");
        generateFromBase("cobblestone");
    }

    private static void generateFromBase(String folder){
        File rootFolder = new File("./" + folder);
        File baseFolder = new File(rootFolder, "base");
        File patternFolder = new File(rootFolder, "patterns");
        File outputFolder = new File(rootFolder, "output");

        File baseImageFile = new File(baseFolder, "base.png");
        if(!baseImageFile.exists())
            throw new IllegalStateException("Could not find the base image file '" + baseImageFile + "'");
        BufferedImage baseImage = loadImage(baseImageFile);

        Map<String,Map<Integer,Integer>> paletteMap = new HashMap<>();

        for(File file : Objects.requireNonNull(baseFolder.listFiles())){
            if(!file.equals(baseImageFile) && file.getName().endsWith(".png"))
                paletteMap.put(file.getName().substring(0, file.getName().length() - 4), new HashMap<>());
        }

        for(Map.Entry<String,Map<Integer,Integer>> entry : paletteMap.entrySet()){
            BufferedImage image = loadImage(new File(baseFolder, entry.getKey() + ".png"));
            createPalette(baseImage, image, entry.getValue());
        }

        for(File file : Objects.requireNonNull(outputFolder.listFiles())){
            if(file.getName().endsWith(".png"))
                file.delete();
        }

        System.out.println("Loaded " + paletteMap.size() + " palettes");

        int images = 0;
        for(File file : Objects.requireNonNull(patternFolder.listFiles())){
            if(!file.getName().endsWith(".png"))
                continue;

            String patternName = file.getName().substring(0, file.getName().length() - 4);
            BufferedImage patternImage = loadImage(file);
            for(Map.Entry<String,Map<Integer,Integer>> entry : paletteMap.entrySet()){
                BufferedImage image = mapImage(patternImage, entry.getValue(), patternName);
                saveImage(new File(outputFolder, entry.getKey() + "_" + patternName + ".png"), image);
                System.out.println("Created: " + entry.getKey() + "_" + patternName + ".png");
                images++;
            }
        }

        System.out.println("Mapped " + images + " images");
    }

    private static void createPalette(BufferedImage base, BufferedImage image, Map<Integer,Integer> palette){
        if(base.getWidth() != image.getWidth() || base.getHeight() != image.getHeight())
            throw new IllegalArgumentException("Base image must be the same size as palette images!");

        for(int x = 0; x < base.getWidth(); x++){
            for(int y = 0; y < base.getHeight(); y++){
                palette.put(base.getRGB(x, y), image.getRGB(x, y));
            }
        }
    }

    private static BufferedImage mapImage(BufferedImage pattern, Map<Integer,Integer> palette, String patternName){
        BufferedImage image = new BufferedImage(pattern.getWidth(), pattern.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < pattern.getWidth(); x++){
            for(int y = 0; y < pattern.getHeight(); y++){
                if(pattern.getRGB(x, y) == 0)
                    continue;
                if(!palette.containsKey(pattern.getRGB(x, y)))
                    throw new IllegalStateException("Palette is missing color '" + new Color(pattern.getRGB(x, y)) + "' for pattern '" + patternName + "'");

                image.setRGB(x, y, palette.get(pattern.getRGB(x, y)));
            }
        }
        return image;
    }

    private static BufferedImage loadImage(File file){
        try{
            return ImageIO.read(file);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static void saveImage(File file, BufferedImage image){
        try{
            ImageIO.write(image, "png", file);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
