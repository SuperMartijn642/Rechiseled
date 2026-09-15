package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.ResourceGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.blocks.RechiseledSlabBlock;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

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
        Identifier identifier = Registries.BLOCKS.getIdentifier(block);
        Identifier lootTable = block.getLootTable().map(ResourceKey::identifier).orElse(null);
        if(lootTable != null && lootTable.getNamespace().equals(identifier.getNamespace()) && lootTable.getPath().equals("block/" + identifier.getPath()))
            return;
        this.dropSelf(block);
    }

    private void addSlabLootTable(Block slab){
        HolderLookup.RegistryLookup<Block> blockRegistry = ResourceGenerator.registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK);
        this.lootTable(slab)
            .pool(pool ->
                pool.survivesExplosionCondition()
                    .function(SetItemCountFunction.setCount(ContextIntProviders.exactly(2))
                        .when(MatchBlock.blockMatches(blockRegistry, slab, StatePropertiesPredicate.Builder.properties().hasProperty(RechiseledSlabBlock.TYPE, SlabType.DOUBLE))).build()
                    )
                    .itemEntry(slab)
            );
    }

    @Override
    public String getName(){
        return "Registration Loot Table Generator: " + this.modName;
    }
}
