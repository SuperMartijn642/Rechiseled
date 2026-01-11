package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationLanguageGenerator extends LanguageGenerator {

    private final RechiseledRegistrationImpl registration;

    public RegistrationLanguageGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache, "en_us");
        this.registration = registration;
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        if(this.registration.getItemGroup() != null && this.registration.getItemGroupTranslation() != null)
            this.itemGroup(this.registration.getItemGroup(), this.registration.getItemGroupTranslation());
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.hasRegularVariant()){
                    if(builder.getTranslation() != null)
                        this.block(builder.getRegularBlock(), builder.getTranslation());
                    if(builder.hasStairs() && builder.getStairs().getTranslation() != null && builder.getStairs().hasRegularVariant())
                        this.block(builder.getStairs().getRegularBlock(), builder.getStairs().getTranslation());
                    if(builder.hasSlabs() && builder.getSlabs().getTranslation() != null && builder.getSlabs().hasRegularVariant())
                        this.block(builder.getSlabs().getRegularBlock(), builder.getSlabs().getTranslation());
                }
                if(builder.hasConnectingVariant()){
                    if(builder.getTranslation() != null)
                        this.block(builder.getConnectingBlock(), builder.getTranslation());
                    if(builder.hasStairs() && builder.getStairs().getTranslation() != null && builder.getStairs().hasConnectingVariant())
                        this.block(builder.getStairs().getConnectingBlock(), builder.getStairs().getTranslation());
                    if(builder.hasSlabs() && builder.getSlabs().getTranslation() != null && builder.getSlabs().hasConnectingVariant())
                        this.block(builder.getSlabs().getConnectingBlock(), builder.getSlabs().getTranslation());
                }
            }
        );
    }

    @Override
    public String getName(){
        return "Registration Language Generator: " + this.modName;
    }
}
