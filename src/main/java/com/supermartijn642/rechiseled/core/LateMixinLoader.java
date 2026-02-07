package com.supermartijn642.rechiseled.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created 07/02/2026 by SuperMartijn642
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Rechiseled Late Mixins")
public class LateMixinLoader implements IFMLLoadingPlugin, ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs(){
        return Collections.singletonList("rechiseled.mixins-late.json");
    }

    @Override
    public String[] getASMTransformerClass(){
        return new String[0];
    }

    @Override
    public String getModContainerClass(){
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass(){
        return null;
    }

    @Override
    public void injectData(Map<String,Object> data){
    }

    @Override
    public String getAccessTransformerClass(){
        return null;
    }
}
