package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.util.Pair;
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
        this.registration.getBlockBuilders().stream().map(Pair::right).forEach(
            type -> {
                if(type.hasRegularVariant())
                    this.addLootTable(type.getRegularBlock());
                if(type.hasConnectingVariant())
                    this.addLootTable(type.getConnectingBlock());
            }
        );
    }

    private void addLootTable(Block block){
        this.dropSelf(block);
    }
}
