package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.conversion.ConversionResult;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public record ConversionResultImpl(int numberOfConversions, int leftover, int result) implements ConversionResult {
}
