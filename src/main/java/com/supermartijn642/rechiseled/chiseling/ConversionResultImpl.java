package com.supermartijn642.rechiseled.chiseling;

import com.supermartijn642.rechiseled.api.chiseling.conversion.ConversionResult;

/**
 * Created 12/01/2026 by SuperMartijn642
 */
public class ConversionResultImpl implements ConversionResult {

    private final int numberOfConversions;
    private final int leftover;
    private final int result;

    public ConversionResultImpl(int numberOfConversions, int leftover, int result){
        this.numberOfConversions = numberOfConversions;
        this.leftover = leftover;
        this.result = result;
    }

    @Override
    public int numberOfConversions(){
        return this.numberOfConversions;
    }

    @Override
    public int leftover(){
        return this.leftover;
    }

    @Override
    public int result(){
        return this.result;
    }
}
