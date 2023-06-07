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
            pair -> {
                if(pair.right().hasRegularVariant())
                    this.block(pair.right().getRegularBlock(), pair.left().translation);
                if(pair.right().hasConnectingVariant())
                    this.block(pair.right().getConnectingBlock(), pair.left().translation);
            }
        );
    }
}
