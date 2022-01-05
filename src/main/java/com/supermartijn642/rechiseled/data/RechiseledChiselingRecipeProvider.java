package com.supermartijn642.rechiseled.data;

import com.google.common.collect.Maps;
import com.supermartijn642.rechiseled.RechiseledBlockType;
import com.supermartijn642.rechiseled.RechiseledTagGroups;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.item.Items;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Map;

/**
 * Created 24/12/2021 by SuperMartijn642
 */
public class RechiseledChiselingRecipeProvider extends ChiselingRecipeProvider {

    public RechiseledChiselingRecipeProvider(GatherDataEvent e){
        super("rechiseled", e.getGenerator());
    }

    @Override
    protected void buildRecipes(){
        Map<String,ChiselingRecipeBuilder> builders = Maps.newHashMap();
        builders.computeIfAbsent(RechiseledTagGroups.ACACIA_PLANKS, this::beginRecipe)
            .addRegularItem(Items.ACACIA_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.ANDESITE, this::beginRecipe)
            .addRegularItem(Items.ANDESITE);
        builders.computeIfAbsent(RechiseledTagGroups.BIRCH_PLANKS, this::beginRecipe)
            .addRegularItem(Items.BIRCH_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.COBBLESTONE, this::beginRecipe)
            .addRegularItem(Items.COBBLESTONE)
            .addRegularItem(Items.MOSSY_COBBLESTONE);
        builders.computeIfAbsent(RechiseledTagGroups.DARK_OAK_PLANKS, this::beginRecipe)
            .addRegularItem(Items.DARK_OAK_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.DIORITE, this::beginRecipe)
            .addRegularItem(Items.DIORITE);
        builders.computeIfAbsent(RechiseledTagGroups.END_STONE, this::beginRecipe)
            .addRegularItem(Items.END_STONE)
            .addRegularItem(Items.END_STONE_BRICKS);
        builders.computeIfAbsent(RechiseledTagGroups.GLOWSTONE, this::beginRecipe)
            .addRegularItem(Items.GLOWSTONE);
        builders.computeIfAbsent(RechiseledTagGroups.GRANITE, this::beginRecipe)
            .addRegularItem(Items.GRANITE);
        builders.computeIfAbsent(RechiseledTagGroups.JUNGLE_PLANKS, this::beginRecipe)
            .addRegularItem(Items.JUNGLE_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.NETHERRACK, this::beginRecipe)
            .addRegularItem(Items.NETHERRACK);
        builders.computeIfAbsent(RechiseledTagGroups.NETHER_BRICKS, this::beginRecipe)
            .addRegularItem(Items.NETHER_BRICKS);
        builders.computeIfAbsent(RechiseledTagGroups.OAK_PLANKS, this::beginRecipe)
            .addRegularItem(Items.OAK_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.PRISMARINE_BRICKS, this::beginRecipe)
            .addRegularItem(Items.PRISMARINE_BRICKS);
        builders.computeIfAbsent(RechiseledTagGroups.RED_NETHER_BRICKS, this::beginRecipe)
            .addRegularItem(Items.RED_NETHER_BRICKS);
        builders.computeIfAbsent(RechiseledTagGroups.RED_SANDSTONE, this::beginRecipe)
            .addRegularItem(Items.RED_SANDSTONE)
            .addRegularItem(Items.CHISELED_RED_SANDSTONE)
            .addRegularItem(Items.CUT_RED_SANDSTONE)
            .addRegularItem(Items.SMOOTH_RED_SANDSTONE);
        builders.computeIfAbsent(RechiseledTagGroups.SANDSTONE, this::beginRecipe)
            .addRegularItem(Items.SANDSTONE)
            .addRegularItem(Items.CHISELED_SANDSTONE)
            .addRegularItem(Items.CUT_SANDSTONE)
            .addRegularItem(Items.SMOOTH_SANDSTONE);
        builders.computeIfAbsent(RechiseledTagGroups.SPRUCE_PLANKS, this::beginRecipe)
            .addRegularItem(Items.SPRUCE_PLANKS);
        builders.computeIfAbsent(RechiseledTagGroups.STONE, this::beginRecipe)
            .addRegularItem(Items.STONE)
            .addRegularItem(Items.STONE_BRICKS)
            .addRegularItem(Items.MOSSY_STONE_BRICKS)
            .addRegularItem(Items.CRACKED_STONE_BRICKS);

        for(RechiseledBlockType type : RechiseledBlockType.values()){
            for(String tag : type.tags){
                ChiselingRecipeBuilder builder = builders.computeIfAbsent(tag, this::beginRecipe);
                builder.add(type.useParent ? type.parentBlock.get().asItem() : type.getRegularItem(), type.getConnectingItem());
            }
        }
    }
}
