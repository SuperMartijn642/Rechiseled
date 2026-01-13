package com.supermartijn642.rechiseled.api.chiseling.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies identifier and priority for chiseling recipe plugins.
 * <p>
 * On Forge and NeoForge, plugins with this annotation will automatically be registered.<br>
 * On Fabric, plugins should be listed as a 'rechiseled-chiseling-recipe-plugin' entry point in the <i>fabric.mod.json</i> properties.
 * <p>
 * Created 13/01/2026 by SuperMartijn642
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RechiseledChiselingRecipePlugin {

    String identifier() default "plugin";

    /**
     * Plugins with a lower priority value are applied before plugins with a higher priority value.
     * The default priority for plugins is {@link ChiselingRecipePlugin#DEFAULT_PLUGIN_PRIORITY}, the plugin for recipes from datapacks has priority {@code 0}.
     */
    int priority() default ChiselingRecipePlugin.DEFAULT_PLUGIN_PRIORITY;
}
