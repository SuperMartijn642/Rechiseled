package com.supermartijn642.rechiseled.api.chiseling.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies identifier and priority for chiseling recipe plugins.
 * <p>
 * On Forge and NeoForge, plugins with this annotation will automatically be registered.<br>
 * On Fabric, plugins must be listed as a 'rechiseled-chiseling-recipe-plugin' entry point for the mod.
 * <p>
 * Created 13/01/2026 by SuperMartijn642
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RechiseledChiselingRecipePlugin {

    String identifier() default "main";

    int priority() default 0;
}
