package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.BaseChiselingRecipes;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class RechiseledChiselingRecipeProvider extends ChiselingRecipeProvider {

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

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            for(ResourceLocation recipe : type.recipes){
                if(recipe.getResourceDomain().equals("rechiseled"))
                    this.beginRecipe(recipe.getResourcePath()).add(type.getRegularItem(), type.getRegularItemDamage(), type.getConnectingItem(), type.getConnectingItemDamage());
                else
                    this.beginRecipe(recipe.getResourceDomain() + "/" + recipe.getResourcePath()).parent(recipe).add(type.getRegularItem(), type.getConnectingItem());
            }
        }
    }
}
