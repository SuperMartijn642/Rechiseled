package com.supermartijn642.rechiseled;

import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

/**
 * Created 1/25/2021 by SuperMartijn642
 */
public class RechiseledConfig {

    static{
        IConfigBuilder builder = ConfigBuilders.newTomlConfig("rechiseled", null, false);

        builder.push("General");

        builder.pop();

        builder.build();
    }

}
