package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ItemInfoGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.api.blocks.BlockModelType;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.world.level.block.Block;

/**
 * Created 04/04/2025 by SuperMartijn642
 */
public class RegistrationItemInfoGenerator extends ItemInfoGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationItemInfoGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                BlockModelType modelType = builder.modelType == null ? type.getSpecification().getDefaultModelType() : builder.modelType;
                if(type.hasRegularVariant())
                    this.addItemModel(type.getRegularBlock());
                if(type.hasConnectingVariant())
                    this.addItemModel(type.getConnectingBlock());
            }
        );
    }

    private void addItemModel(Block block){
        String namespace = Registries.BLOCKS.getIdentifier(block).getNamespace();
        String identifier = Registries.BLOCKS.getIdentifier(block).getPath();
        this.info(namespace, identifier).model(this.model(namespace, "block/" + identifier));
    }

    @Override
    public String getName(){
        return "Registration Item Info Generator: " + this.modName;
    }
}
