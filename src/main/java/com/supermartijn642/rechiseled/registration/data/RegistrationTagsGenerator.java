package com.supermartijn642.rechiseled.registration.data;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.rechiseled.registration.RechiseledCommonBlockBuilderImpl;
import com.supermartijn642.rechiseled.registration.RechiseledRegistrationImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.resource.ResourcePackLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 04/05/2023 by SuperMartijn642
 */
public class RegistrationTagsGenerator extends TagGenerator {

    private static final Gson GSON = new GsonBuilder().create();

    private final RechiseledRegistrationImpl registration;
    private final ResourceManager resources;

    public RegistrationTagsGenerator(RechiseledRegistrationImpl registration, ResourceCache cache){
        super(registration.getModid(), cache);
        this.registration = registration;

        List<PackResources> packs = new ArrayList<>();
        packs.add(ServerPacksSource.createVanillaPackSource());
        // include existing packs
//        existingPacks.forEach(path -> {
//            var packInfo = new PackLocationInfo(path.getFileName().toString(), Component.empty(), PackSource.BUILT_IN, Optional.empty());
//            packs.add(new PathPackResources(packInfo, path));
//        });
        // include mod resources last
        ModList.get().getSortedMods().stream()
            .filter(Predicate.not(mod -> mod.getModId().equals("minecraft")))
            .filter(Predicate.not(mod -> mod.getModId().equals(registration.getModid())))
            .map(mod -> {
                var owningFile = mod.getModInfo().getOwningFile();
                var packInfo = new PackLocationInfo("mod/" + mod.getModId(), Component.empty(), PackSource.BUILT_IN, Optional.empty());
                return ResourcePackLoader.createPackForMod(owningFile).openPrimary(packInfo);
            })
            .forEach(packs::add);
        this.resources = new MultiPackResourceManager(
            PackType.SERVER_DATA,
            packs
        );
    }

    @Override
    public void generate(){
        if(!this.registration.providersRegistered)
            return;
        this.registration.getBlockBuilders().forEach(
            builder -> {
                if(builder.hasRegularVariant()){
                    this.addTags(builder, builder.getRegularBlock());
                    if(builder.hasStairs() && builder.getStairs().hasRegularVariant())
                        this.addTags(builder.getStairs(), builder.getStairs().getRegularBlock());
                    if(builder.hasSlabs() && builder.getSlabs().hasRegularVariant())
                        this.addTags(builder.getSlabs(), builder.getSlabs().getRegularBlock());
                }
                if(builder.hasConnectingVariant()){
                    this.addTags(builder, builder.getConnectingBlock());
                    if(builder.hasStairs() && builder.getStairs().hasConnectingVariant())
                        this.addTags(builder.getStairs(), builder.getStairs().getConnectingBlock());
                    if(builder.hasSlabs() && builder.getSlabs().hasConnectingVariant())
                        this.addTags(builder.getSlabs(), builder.getSlabs().getConnectingBlock());
                }
            }
        );
    }

    private void addTags(RechiseledCommonBlockBuilderImpl<?> builder, Block block){
        builder.getBlockTags().stream().map(this::blockTag).forEach(tag -> tag.add(block));
        builder.getItemTags().stream().map(this::itemTag).forEach(tag -> tag.add(block.asItem()));
        Block miningTagsBlock = builder.getMiningTagsBlock();
        if(miningTagsBlock != null)
            this.getTagsForBlock(miningTagsBlock).stream().map(this::blockTag).forEach(tag -> tag.add(block));
    }

    private Set<Identifier> getTagsForBlock(Block block){
        return Stream.of(
                BlockTags.MINEABLE_WITH_AXE,
                BlockTags.MINEABLE_WITH_HOE,
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.NEEDS_STONE_TOOL,
                BlockTags.NEEDS_IRON_TOOL,
                BlockTags.NEEDS_DIAMOND_TOOL
            )
            .map(TagKey::location)
            .filter(tag -> this.loadVanillaTag(tag).contains(block))
            .collect(Collectors.toSet());
    }

    private final Map<Identifier,List<Block>> loadedTags = Maps.newHashMap();

    private List<Block> loadVanillaTag(Identifier location){
        if(this.loadedTags.containsKey(location))
            return this.loadedTags.get(location);

        List<Block> blocks = new ArrayList<>();

        Identifier tagLocation = Identifier.fromNamespaceAndPath(location.getNamespace(), "tags/block/" + location.getPath() + ".json");
        for(Resource resource : this.resources.getResourceStack(tagLocation)){
            try(InputStream stream = resource.open()){
                JsonObject json = GSON.fromJson(new InputStreamReader(stream), JsonObject.class);
                JsonArray array = json.getAsJsonArray("values");
                for(JsonElement element : array){
                    String name = element.getAsString();
                    if(name.charAt(0) == '#'){
                        blocks.addAll(this.loadVanillaTag(Identifier.parse(name.substring(1))));
                        continue;
                    }
                    Identifier registryName = Identifier.parse(name);
                    Block block = Registries.BLOCKS.getValue(registryName);
                    if(block == null)
                        throw new JsonParseException("Unknown block '" + registryName + "' in '" + location + "'");
                    blocks.add(block);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        this.loadedTags.put(location, blocks);
        return blocks;
    }

    @Override
    public String getName(){
        return "Registration Tag Generator: " + this.modName;
    }
}
