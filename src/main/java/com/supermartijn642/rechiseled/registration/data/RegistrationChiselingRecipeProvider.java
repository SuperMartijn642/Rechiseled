package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.api.util.ItemWithMeta;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationChiselingRecipeProvider extends ChiselingRecipeProvider {

    private final RechiseledRegistrationImpl registration;

    public RegistrationChiselingRecipeProvider(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;
    }

    @Override
    protected void buildRecipes(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getChiselingEntries().forEach(recipe -> recipe.right().accept(this.beginRecipe(recipe.left()).entry()));
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.getRecipe() != null){
                    ChiselingEntryBuilder entry = this.beginRecipe(builder.getRecipe()).entry();
                    if(builder.getRegularItem() != null){
                        entry.regularBlock(builder.getRegularItem());
                        ItemWithMeta regularStairs = builder.hasStairs() ? builder.getStairs().getRegularItem() : null;
                        if(regularStairs != null)
                            entry.regularStairs(regularStairs);
                        ItemWithMeta regularSlab = builder.hasSlabs() ? builder.getSlabs().getRegularItem() : null;
                        if(regularSlab != null)
                            entry.regularSlab(regularSlab);
                    }
                    if(builder.getConnectingItem() != null){
                        entry.connectingBlock(builder.getConnectingItem());
                        ItemWithMeta connectingStairs = builder.hasStairs() ? builder.getStairs().getConnectingItem() : null;
                        if(connectingStairs != null)
                            entry.connectingStairs(connectingStairs);
                        ItemWithMeta connectingSlab = builder.hasSlabs() ? builder.getSlabs().getConnectingItem() : null;
                        if(connectingSlab != null)
                            entry.connectingSlab(connectingSlab);
                    }
                }
            }
        );
    }
}
