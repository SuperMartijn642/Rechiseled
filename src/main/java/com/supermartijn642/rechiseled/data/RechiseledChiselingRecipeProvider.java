package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.util.Triple;
import com.supermartijn642.rechiseled.api.BaseChiselingRecipes;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class RechiseledChiselingRecipeProvider extends ChiselingRecipeProvider {

    private static final List<Triple<ResourceLocation,Supplier<Item>,Supplier<Item>>> ENTRIES = new ArrayList<>();

    public static void addRecipeEntry(ResourceLocation recipe, Supplier<Item> regular, Supplier<Item> connecting){
        ENTRIES.add(Triple.of(recipe, regular, connecting));
    }

    public RechiseledChiselingRecipeProvider(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super("rechiseled", generator, existingFileHelper);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe(BaseChiselingRecipes.ACACIA_PLANKS.getPath())
            .addRegularItem(Items.ACACIA_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.ANDESITE.getPath())
            .addRegularItem(Items.ANDESITE);
        this.beginRecipe(BaseChiselingRecipes.BIRCH_PLANKS.getPath())
            .addRegularItem(Items.BIRCH_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.COBBLESTONE.getPath())
            .addRegularItem(Items.COBBLESTONE)
            .addRegularItem(Items.MOSSY_COBBLESTONE);
        this.beginRecipe(BaseChiselingRecipes.DARK_OAK_PLANKS.getPath())
            .addRegularItem(Items.DARK_OAK_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.DARK_PRISMARINE.getPath())
            .addRegularItem(Items.DARK_PRISMARINE);
        this.beginRecipe(BaseChiselingRecipes.DIORITE.getPath())
            .addRegularItem(Items.DIORITE);
        this.beginRecipe(BaseChiselingRecipes.DIRT.getPath())
            .addRegularItem(Items.DIRT);
        this.beginRecipe(BaseChiselingRecipes.END_STONE.getPath())
            .addRegularItem(Items.END_STONE)
            .addRegularItem(Items.END_STONE_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.GLOWSTONE.getPath())
            .addRegularItem(Items.GLOWSTONE);
        this.beginRecipe(BaseChiselingRecipes.GRANITE.getPath())
            .addRegularItem(Items.GRANITE);
        this.beginRecipe(BaseChiselingRecipes.JUNGLE_PLANKS.getPath())
            .addRegularItem(Items.JUNGLE_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.NETHERRACK.getPath())
            .addRegularItem(Items.NETHERRACK);
        this.beginRecipe(BaseChiselingRecipes.NETHER_BRICKS.getPath())
            .addRegularItem(Items.NETHER_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.OAK_PLANKS.getPath())
            .addRegularItem(Items.OAK_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.OBSIDIAN.getPath())
            .addRegularItem(Items.OBSIDIAN);
        this.beginRecipe(BaseChiselingRecipes.PRISMARINE_BRICKS.getPath())
            .addRegularItem(Items.PRISMARINE_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.PURPUR_BLOCK.getPath())
            .addRegularItem(Items.PURPUR_PILLAR);
        this.beginRecipe(BaseChiselingRecipes.QUARTZ_BLOCK.getPath())
            .addRegularItem(Items.QUARTZ_PILLAR)
            .addRegularItem(Items.CHISELED_QUARTZ_BLOCK)
            .addRegularItem(Items.SMOOTH_QUARTZ);
        this.beginRecipe(BaseChiselingRecipes.RED_NETHER_BRICKS.getPath())
            .addRegularItem(Items.RED_NETHER_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.RED_SANDSTONE.getPath())
            .addRegularItem(Items.RED_SANDSTONE)
            .addRegularItem(Items.CHISELED_RED_SANDSTONE)
            .addRegularItem(Items.CUT_RED_SANDSTONE)
            .addRegularItem(Items.SMOOTH_RED_SANDSTONE);
        this.beginRecipe(BaseChiselingRecipes.SANDSTONE.getPath())
            .addRegularItem(Items.SANDSTONE)
            .addRegularItem(Items.CHISELED_SANDSTONE)
            .addRegularItem(Items.CUT_SANDSTONE)
            .addRegularItem(Items.SMOOTH_SANDSTONE);
        this.beginRecipe(BaseChiselingRecipes.SPRUCE_PLANKS.getPath())
            .addRegularItem(Items.SPRUCE_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.STONE.getPath())
            .addRegularItem(Items.STONE)
            .addRegularItem(Items.STONE_BRICKS)
            .addRegularItem(Items.MOSSY_STONE_BRICKS)
            .addRegularItem(Items.CRACKED_STONE_BRICKS);

        for(Triple<ResourceLocation,Supplier<Item>,Supplier<Item>> recipe : ENTRIES){
            String path = recipe.left().getNamespace().equals("rechiseled") ? recipe.left().getPath() : recipe.left().getNamespace() + "/" + recipe.left().getPath();
            this.beginRecipe(path).add(recipe.middle().get(), recipe.right().get()); // TODO Make this work for other namespaces
        }
    }
}
