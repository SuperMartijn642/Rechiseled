package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import com.supermartijn642.rechiseled.api.chiseling.data.ChiselingEntryBuilder;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created 05/05/2023 by SuperMartijn642
 */
public class RegistrationChiselingRecipeProvider extends ChiselingRecipeProvider {

    private final RechiseledRegistrationImpl registration;

    public RegistrationChiselingRecipeProvider(RechiseledRegistrationImpl registration, DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(registration.getModid(), generator, existingFileHelper);
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
                    if(builder.getRegularBlock() != null){
                        entry.regularBlock(builder.getRegularBlock());
                        IItemProvider regularStairs = builder.hasStairs() ? builder.getStairs().getRegularBlock() : null;
                        if(regularStairs != null)
                            entry.regularStairs(regularStairs);
                        IItemProvider regularSlab = builder.hasSlabs() ? builder.getSlabs().getRegularBlock() : null;
                        if(regularSlab != null)
                            entry.regularSlab(regularSlab);
                    }
                    if(builder.getConnectingBlock() != null){
                        entry.connectingBlock(builder.getConnectingBlock());
                        IItemProvider connectingStairs = builder.hasStairs() ? builder.getStairs().getConnectingBlock() : null;
                        if(connectingStairs != null)
                            entry.connectingStairs(connectingStairs);
                        IItemProvider connectingSlab = builder.hasSlabs() ? builder.getSlabs().getConnectingBlock() : null;
                        if(connectingSlab != null)
                            entry.connectingSlab(connectingSlab);
                    }
                }
            }
        );
    }
}
