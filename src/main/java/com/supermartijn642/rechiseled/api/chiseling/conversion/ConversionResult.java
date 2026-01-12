package com.supermartijn642.rechiseled.api.chiseling.conversion;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public interface ConversionResult {

    /**
     * The number of input items that are converted.
     */
    int numberOfConversions();

    /**
     * The number of input items that could not be converted.
     */
    int leftover();

    /**
     * The number of output items from the conversion.
     */
    int result();
}
