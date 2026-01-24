package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationLootTableGenerator extends LootTableGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationLootTableGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.hasRegularVariant()){
                    this.addLootTable(builder.getRegularBlock());
                    if(builder.hasStairs() && builder.getStairs().hasRegularVariant())
                        this.addLootTable(builder.getStairs().getRegularBlock());
                    if(builder.hasSlabs() && builder.getSlabs().hasRegularVariant())
                        this.addSlabLootTable(builder.getSlabs().getRegularBlock());
                }
                if(builder.hasConnectingVariant()){
                    this.addLootTable(builder.getConnectingBlock());
                    if(builder.hasStairs() && builder.getStairs().hasConnectingVariant())
                        this.addLootTable(builder.getStairs().getConnectingBlock());
                    if(builder.hasSlabs() && builder.getSlabs().hasConnectingVariant())
                        this.addSlabLootTable(builder.getSlabs().getConnectingBlock());
                }
            }
        );
    }

    private void addLootTable(Block block){
        ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
        ResourceLocation lootTable = block.getLootTable();
        if(lootTable == null || (lootTable.getNamespace().equals(identifier.getNamespace()) && lootTable.getPath().equals("block/" + identifier.getPath())))
            return;
        this.dropSelf(block);
    }

    private void addSlabLootTable(Block slab){
        this.lootTable(slab)
            .pool(pool ->
                pool.survivesExplosionCondition()
                    .function(SetCount.setCount(ConstantRange.exactly(2))
                        .when(BlockStateProperty.hasBlockStateProperties(slab).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RechiseledSlabBlock.TYPE, SlabType.DOUBLE))).build()
                    )
                    .itemEntry(slab)
            );
    }
}
