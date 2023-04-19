package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.core.util.Triple;
import com.supermartijn642.rechiseled.api.BaseChiselingRecipes;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class RechiseledChiselingRecipeProvider extends ChiselingRecipeProvider {

    private static final List<Triple<ResourceLocation,Pair<Supplier<Item>,Integer>,Pair<Supplier<Item>,Integer>>> ENTRIES = new ArrayList<>();

    public static void addRecipeEntry(ResourceLocation recipe, Supplier<Item> regular, int regularData, Supplier<Item> connecting, int connectingData){
        ENTRIES.add(Triple.of(recipe, Pair.of(regular, regularData), Pair.of(connecting, connectingData)));
    }

    public RechiseledChiselingRecipeProvider(ResourceCache cache){
        super("rechiseled", cache);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe(BaseChiselingRecipes.ACACIA_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 4);
        this.beginRecipe(BaseChiselingRecipes.ANDESITE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.STONE), 5);
        this.beginRecipe(BaseChiselingRecipes.BIRCH_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 2);
        this.beginRecipe(BaseChiselingRecipes.COBBLESTONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.COBBLESTONE))
            .addRegularItem(Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE));
        this.beginRecipe(BaseChiselingRecipes.DARK_OAK_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 5);
        this.beginRecipe(BaseChiselingRecipes.DARK_PRISMARINE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PRISMARINE), 2);
        this.beginRecipe(BaseChiselingRecipes.DIORITE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.STONE), 3);
        this.beginRecipe(BaseChiselingRecipes.DIRT.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.DIRT), 0);
        this.beginRecipe(BaseChiselingRecipes.END_STONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.END_STONE))
            .addRegularItem(Item.getItemFromBlock(Blocks.END_BRICKS));
        this.beginRecipe(BaseChiselingRecipes.GLOWSTONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.GLOWSTONE));
        this.beginRecipe(BaseChiselingRecipes.GRANITE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.STONE), 1);
        this.beginRecipe(BaseChiselingRecipes.JUNGLE_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 3);
        this.beginRecipe(BaseChiselingRecipes.NETHERRACK.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.NETHERRACK));
        this.beginRecipe(BaseChiselingRecipes.NETHER_BRICKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.NETHER_BRICK));
        this.beginRecipe(BaseChiselingRecipes.OAK_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 0);
        this.beginRecipe(BaseChiselingRecipes.OBSIDIAN.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.OBSIDIAN));
        this.beginRecipe(BaseChiselingRecipes.PRISMARINE_BRICKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PRISMARINE), 1);
        this.beginRecipe(BaseChiselingRecipes.PURPUR_BLOCK.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PURPUR_PILLAR));
        this.beginRecipe(BaseChiselingRecipes.QUARTZ_BLOCK.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1)
            .addRegularItem(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 2);
        this.beginRecipe(BaseChiselingRecipes.RED_NETHER_BRICKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.RED_NETHER_BRICK));
        this.beginRecipe(BaseChiselingRecipes.RED_SANDSTONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.RED_SANDSTONE), 0)
            .addRegularItem(Item.getItemFromBlock(Blocks.RED_SANDSTONE), 1)
            .addRegularItem(Item.getItemFromBlock(Blocks.RED_SANDSTONE), 2);
        this.beginRecipe(BaseChiselingRecipes.SANDSTONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.SANDSTONE), 0)
            .addRegularItem(Item.getItemFromBlock(Blocks.SANDSTONE), 1)
            .addRegularItem(Item.getItemFromBlock(Blocks.SANDSTONE), 2);
        this.beginRecipe(BaseChiselingRecipes.SPRUCE_PLANKS.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.PLANKS), 1);
        this.beginRecipe(BaseChiselingRecipes.STONE.getResourcePath())
            .addRegularItem(Item.getItemFromBlock(Blocks.STONE), 0)
            .addRegularItem(Item.getItemFromBlock(Blocks.STONEBRICK), 0)
            .addRegularItem(Item.getItemFromBlock(Blocks.STONEBRICK), 1)
            .addRegularItem(Item.getItemFromBlock(Blocks.STONEBRICK), 2);

        for(Triple<ResourceLocation,Pair<Supplier<Item>,Integer>,Pair<Supplier<Item>,Integer>> recipe : ENTRIES){
            String path = recipe.left().getResourceDomain().equals("rechiseled") ? recipe.left().getResourcePath() : recipe.left().getResourceDomain() + "/" + recipe.left().getResourcePath();
            this.beginRecipe(path).add(recipe.middle().left().get(), recipe.middle().right(), recipe.right().left().get(), recipe.right().right()); // TODO Make this work for other namespaces
        }
    }
}
