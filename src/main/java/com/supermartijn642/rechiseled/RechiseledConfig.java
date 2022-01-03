package com.supermartijn642.rechiseled;

import com.supermartijn642.configlib.ModConfigBuilder;

/**
 * Created 1/25/2021 by SuperMartijn642
 */
public class RechiseledConfig {

    static{
        ModConfigBuilder builder = new ModConfigBuilder("rechiseled");

        builder.push("General");

        builder.pop();

        builder.build();
    }

}
