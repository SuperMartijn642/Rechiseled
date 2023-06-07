package com.supermartijn642.rechiseled.registration.data;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockBuilderImpl;
import com.supermartijn642.rechiseled.blocks.RechiseledBlockTypeImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.item.Item;

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
        this.registration.getChiselingEntries().forEach(triple -> this.beginRecipe(triple.left()).add(triple.middle().left() == null ? null : triple.middle().left().get(), triple.middle().left() == null ? 0 : triple.middle().right(), triple.right().left() == null ? null : triple.right().left().get(), triple.right().left() == null ? 0 : triple.right().right()));
        this.registration.getBlockBuilders().forEach(
            pair -> {
                RechiseledBlockBuilderImpl builder = pair.left();
                RechiseledBlockTypeImpl type = pair.right();
                if(builder.recipe != null){
                    Item regularItem = type.hasRegularVariant() ? type.getRegularItem() : builder.customRegularVariant != null ? Item.getItemFromBlock(builder.customRegularVariant.get()) : null;
                    int regularItemData = !type.hasRegularVariant() && builder.customRegularVariant != null ? builder.customRegularVariantData : 0;
                    Item connectingItem = type.hasConnectingVariant() ? type.getConnectingItem() : builder.customConnectingVariant != null ? Item.getItemFromBlock(builder.customConnectingVariant.get()) : null;
                    int connectingItemData = !type.hasRegularVariant() && builder.customConnectingVariant != null ? builder.customConnectingVariantData : 0;
                    this.beginRecipe(builder.recipe).add(regularItem, regularItemData, connectingItem, connectingItemData);
                }
            }
        );
    }
}
