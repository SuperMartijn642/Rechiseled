package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.loot_table.BlockStatePropertyLootCondition;
import com.supermartijn642.core.util.Pair;
import com.supermartijn642.rechiseled.blocks.impl.SlabBlock;
import com.supermartijn642.rechiseled.blocks.impl.SlabType;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.block.Block;

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
        this.dropSelf(block);
    }

    private void addSlabLootTable(Block slab){
        //noinspection SuspiciousNameCombination
        this.lootTable(slab)
            .pool(pool ->
                pool.survivesExplosionCondition()
                    .condition(new BlockStatePropertyLootCondition(slab, Pair.of(SlabBlock.TYPE, SlabType.BOTTOM), Pair.of(SlabBlock.TYPE, SlabType.TOP)))
                    .survivesExplosionCondition()
                    .itemEntry(slab)
            )
            .pool(pool ->
                pool.survivesExplosionCondition()
                    .condition(new BlockStatePropertyLootCondition(slab, Pair.of(SlabBlock.TYPE, SlabType.DOUBLE)))
                    .survivesExplosionCondition()
                    .itemEntry(slab, 2, 1)
            );
    }
}
