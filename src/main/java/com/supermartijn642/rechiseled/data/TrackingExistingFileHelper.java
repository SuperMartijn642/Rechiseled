package com.supermartijn642.rechiseled.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.IResource;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.io.IOException;
import java.util.Collections;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public class TrackingExistingFileHelper extends ExistingFileHelper {

    private final ExistingFileHelper parent;
    private final Multimap<ResourcePackType,ResourceLocation> generated = HashMultimap.create();

    public TrackingExistingFileHelper(ExistingFileHelper parent){
        super(Collections.emptyList(), false);
        this.parent = parent;
    }

    public ExistingFileHelper getParent(){
        return this.parent;
    }

    @Override
    public boolean exists(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix){
        return this.generated.get(type).contains(this.getLocation(loc, pathSuffix, pathPrefix)) || this.parent.exists(loc, type, pathSuffix, pathPrefix);
    }

    public void trackGenerated(ResourceLocation loc, ResourcePackType packType, String pathSuffix, String pathPrefix){
        this.generated.put(packType, this.getLocation(loc, pathSuffix, pathPrefix));
    }

    @Override
    public IResource getResource(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix) throws IOException{
        return this.parent.getResource(loc, type, pathSuffix, pathPrefix);
    }

    @Override
    public boolean isEnabled(){
        return this.parent.isEnabled();
    }

    private ResourceLocation getLocation(ResourceLocation base, String suffix, String prefix){
        return new ResourceLocation(base.getNamespace(), prefix + "/" + base.getPath() + suffix);
    }
}
