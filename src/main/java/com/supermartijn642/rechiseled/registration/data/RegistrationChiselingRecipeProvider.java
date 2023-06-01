package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
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
        this.registration.getChiselingEntries().forEach(triple -> this.beginRecipe(triple.left()).add(triple.middle() == null ? null : triple.middle().get().asItem(), triple.right() == null ? null : triple.right().get().asItem()));
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                if(builder.recipe != null){
                    Item regularItem = type.hasRegularVariant() ? type.getRegularItem() : builder.customRegularVariant != null ? builder.customRegularVariant.get().asItem() : null;
                    Item connectingItem = type.hasConnectingVariant() ? type.getConnectingItem() : builder.customConnectingVariant != null ? builder.customConnectingVariant.get().asItem() : null;
                    this.beginRecipe(builder.recipe).add(regularItem, connectingItem);
                }
            }
        );
    }
}
