package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ItemInfoGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
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
            builder -> {
                if(builder.hasRegularVariant()){
                    this.addItemModel(builder.getRegularBlock(), "");
                    if(builder.hasStairs() && builder.getStairs().hasRegularVariant())
                        this.addItemModel(builder.getStairs().getRegularBlock(), "");
                    if(builder.hasSlabs() && builder.getSlabs().hasRegularVariant())
                        this.addItemModel(builder.getSlabs().getRegularBlock(), "_bottom");
                }
                if(builder.hasConnectingVariant()){
                    this.addItemModel(builder.getConnectingBlock(), "");
                    if(builder.hasStairs() && builder.getStairs().hasConnectingVariant())
                        this.addItemModel(builder.getStairs().getConnectingBlock(), "_bottom");
                    if(builder.hasSlabs() && builder.getSlabs().hasConnectingVariant())
                        this.addItemModel(builder.getSlabs().getConnectingBlock(), "_bottom");
                }
            }
        );
    }

    private void addItemModel(Block block, String modelPostFix){
        String namespace = Registries.BLOCKS.getIdentifier(block).getNamespace();
        String identifier = Registries.BLOCKS.getIdentifier(block).getPath();
        this.info(namespace, identifier).model(this.model(namespace, "block/" + identifier + modelPostFix));
    }

    @Override
    public String getName(){
        return "Registration Item Info Generator: " + this.modName;
    }
}
