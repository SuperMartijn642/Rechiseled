package com.supermartijn642.rechiseled.data;

import com.supermartijn642.rechiseled.RechiseledBlockType;
import com.supermartijn642.rechiseled.api.BaseChiselingRecipes;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class RechiseledChiselingRecipeProvider extends ChiselingRecipeProvider {

    public RechiseledChiselingRecipeProvider(DataGenerator generator){
        super("rechiseled", generator);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe(BaseChiselingRecipes.ACACIA_PLANKS.getPath())
            .addRegularItem(Items.ACACIA_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.ANDESITE.getPath())
            .addRegularItem(Items.ANDESITE);
        this.beginRecipe(BaseChiselingRecipes.BIRCH_PLANKS.getPath())
            .addRegularItem(Items.BIRCH_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.BLACKSTONE.getPath())
            .addRegularItem(Items.BLACKSTONE)
            .addRegularItem(Items.POLISHED_BLACKSTONE_BRICKS)
            .addRegularItem(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .addRegularItem(Items.CHISELED_POLISHED_BLACKSTONE);
        this.beginRecipe(BaseChiselingRecipes.COBBLED_DEEPSLATE.getPath())
            .addRegularItem(Items.COBBLED_DEEPSLATE)
            .addRegularItem(Items.POLISHED_DEEPSLATE)
            .addRegularItem(Items.DEEPSLATE_BRICKS)
            .addRegularItem(Items.CRACKED_DEEPSLATE_BRICKS)
            .addRegularItem(Items.DEEPSLATE_TILES)
            .addRegularItem(Items.CRACKED_DEEPSLATE_TILES)
            .addRegularItem(Items.CHISELED_DEEPSLATE);
        this.beginRecipe(BaseChiselingRecipes.COBBLESTONE.getPath())
            .addRegularItem(Items.COBBLESTONE)
            .addRegularItem(Items.MOSSY_COBBLESTONE);
        this.beginRecipe(BaseChiselingRecipes.CRIMSON_PLANKS.getPath())
            .addRegularItem(Items.CRIMSON_PLANKS);
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
            .addRegularItem(Items.NETHER_BRICKS)
            .addRegularItem(Items.CHISELED_NETHER_BRICKS)
            .addRegularItem(Items.CRACKED_NETHER_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.OAK_PLANKS.getPath())
            .addRegularItem(Items.OAK_PLANKS);
        this.beginRecipe(BaseChiselingRecipes.OBSIDIAN.getPath())
            .addRegularItem(Items.OBSIDIAN);
        this.beginRecipe(BaseChiselingRecipes.PRISMARINE_BRICKS.getPath())
            .addRegularItem(Items.PRISMARINE_BRICKS);
        this.beginRecipe(BaseChiselingRecipes.PURPUR_BLOCK.getPath())
            .addRegularItem(Items.PURPUR_PILLAR);
        this.beginRecipe(BaseChiselingRecipes.QUARTZ_BLOCK.getPath())
            .addRegularItem(Items.QUARTZ_BRICKS)
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
        this.beginRecipe(BaseChiselingRecipes.WARPED_PLANKS.getPath())
            .addRegularItem(Items.WARPED_PLANKS);

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            for(ResourceLocation recipe : type.recipes){
                if(recipe.getNamespace().equals("rechiseled"))
                    this.beginRecipe(recipe.getPath()).add(type.getRegularItem(), type.getConnectingItem());
                else
                    this.beginRecipe(recipe.getNamespace() + "/" + recipe.getPath()).parent(recipe).add(type.getRegularItem(), type.getConnectingItem());
            }
        }
    }
}
