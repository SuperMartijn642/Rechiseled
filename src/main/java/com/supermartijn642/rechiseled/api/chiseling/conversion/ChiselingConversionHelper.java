package com.supermartijn642.rechiseled.api.chiseling.conversion;

import com.supermartijn642.rechiseled.api.chiseling.ItemWithWorth;
import com.supermartijn642.rechiseled.chiseling.ConversionResultImpl;

/**
 * Helper for converting between items in a chiseling recipe.
 * <p>
 * Created 11/01/2026 by SuperMartijn642
 */
public final class ChiselingConversionHelper {

    /**
     * Gets the factor by which items should be multiplied when converted from {@code from} to {@code to}.
     */
    public static float getConversionFactor(ItemWithWorth from, ItemWithWorth to){
        return from.worth() / to.worth();
    }

    /**
     * Calculate the number of items that can be converted.
     * The result provides the number of conversions, the number of leftover items that weren't converted, and the number of items resulting from the conversion.
     * @param amount          the amount of input items to be converted
     * @param from            the item to convert from
     * @param to              the item to convert to
     * @param maxResultAmount the maximum number of items that should result from the conversion (excluding leftovers)
     */
    public static ConversionResult convert(int amount, ItemWithWorth from, ItemWithWorth to, int maxResultAmount){
        if(amount < 0)
            throw new IllegalArgumentException("Amount must be positive!");
        if(maxResultAmount < 0)
            return new ConversionResultImpl(0, amount, 0);
        double conversionFactor = from.worth() / to.worth();
        int conversions = Math.min(
            (int)Math.floor(maxResultAmount / conversionFactor),
            (int)Math.round(Math.floor(conversionFactor * amount) / conversionFactor)
        );
        int leftover = amount - conversions;
        int result = (int)Math.round(conversions * conversionFactor);
        return new ConversionResultImpl(conversions, leftover, result);
    }

    /**
     * Calculate the number of items that can be converted.
     * The result provides the number of conversions, the number of leftover items that weren't converted, and the number of items resulting from the conversion.
     * @param amount the amount of input items to be converted
     * @param from   the item to convert from
     * @param to     the item to convert to
     */
    public static ConversionResult convert(int amount, ItemWithWorth from, ItemWithWorth to){
        return convert(amount, from, to, Integer.MAX_VALUE);
    }
}
