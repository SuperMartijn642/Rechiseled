package com.supermartijn642.rechiseled.api;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created 24/01/2022 by SuperMartijn642
 */
public abstract class ConnectingBlockModelProvider extends ModelProvider<ConnectingModelBuilder> {

    public ConnectingBlockModelProvider(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(generator, modid, BLOCK_FOLDER, ConnectingModelBuilder::new, existingFileHelper);
    }

    @Override
    public String getName(){
        return "Connecting Block Models: " + this.modid;
    }

    @Override
    protected final void registerModels(){
        this.createModels();
    }

    /**
     * All functions in {@link net.minecraftforge.client.model.generators.ModelBuilder}
     * have been adapted to also have an 'isConnectingTexture' argument. The
     * 'isConnectingTexture' argument can be used for connecting textures.
     *
     * <p>{@link ConnectingModelBuilder#connectToOtherBlocks(boolean)} can be used
     * to set whether connecting textures is the model should connect. If false,
     * connecting textures will simply use their top left texture (the fully
     * bordered one).
     */
    protected abstract void createModels();

    public ConnectingModelBuilder cube(String name, ResourceLocation down, boolean downConnecting, ResourceLocation up, boolean upConnecting, ResourceLocation north, boolean northConnecting, ResourceLocation south, boolean southConnecting, ResourceLocation east, boolean eastConnecting, ResourceLocation west, boolean westConnecting){
        return this.withExistingParent(name, "cube")
            .texture("down", down, downConnecting)
            .texture("up", up, upConnecting)
            .texture("north", north, northConnecting)
            .texture("south", south, southConnecting)
            .texture("east", east, eastConnecting)
            .texture("west", west, westConnecting);
    }

    private ConnectingModelBuilder singleTexture(String name, String parent, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, this.mcLoc(parent), texture, isConnectingTexture);
    }

    public ConnectingModelBuilder singleTexture(String name, ResourceLocation parent, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, parent, "texture", texture, isConnectingTexture);
    }

    private ConnectingModelBuilder singleTexture(String name, String parent, String textureKey, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, this.mcLoc(parent), textureKey, texture, isConnectingTexture);
    }

    public ConnectingModelBuilder singleTexture(String name, ResourceLocation parent, String textureKey, ResourceLocation texture, boolean isConnectingTexture){
        return this.withExistingParent(name, parent)
            .texture(textureKey, texture, isConnectingTexture);
    }

    public ConnectingModelBuilder cubeAll(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/cube_all", "all", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder cubeTop(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation top, boolean topConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/cube_top")
            .texture("side", side, sideConnecting)
            .texture("top", top, topConnecting);
    }

    private ConnectingModelBuilder sideBottomTop(String name, String parent, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.withExistingParent(name, parent)
            .texture("side", side, sideConnecting)
            .texture("bottom", bottom, bottomConnecting)
            .texture("top", top, topConnecting);
    }

    public ConnectingModelBuilder cubeBottomTop(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/cube_bottom_top", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder cubeColumn(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation end, boolean endConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/cube_column")
            .texture("side", side, sideConnecting)
            .texture("end", end, endConnecting);
    }

    public ConnectingModelBuilder cubeColumnHorizontal(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation end, boolean endConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/cube_column_horizontal")
            .texture("side", side, sideConnecting)
            .texture("end", end, endConnecting);
    }

    public ConnectingModelBuilder orientableVertical(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation front, boolean frontConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/orientable_vertical")
            .texture("side", side, sideConnecting)
            .texture("front", front, frontConnecting);
    }

    public ConnectingModelBuilder orientableWithBottom(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation front, boolean frontConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/orientable_with_bottom")
            .texture("side", side, sideConnecting)
            .texture("front", front, frontConnecting)
            .texture("bottom", bottom, bottomConnecting)
            .texture("top", top, topConnecting);
    }

    public ConnectingModelBuilder orientable(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation front, boolean frontConnecting, ResourceLocation top, boolean topConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/orientable")
            .texture("side", side, sideConnecting)
            .texture("front", front, frontConnecting)
            .texture("top", top, topConnecting);
    }

    public ConnectingModelBuilder stairs(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/stairs", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder stairsOuter(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/outer_stairs", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder stairsInner(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/inner_stairs", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder slab(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/slab", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder slabTop(String name, ResourceLocation side, boolean sideConnecting, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.sideBottomTop(name, BLOCK_FOLDER + "/slab_top", side, sideConnecting, bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder fencePost(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/fence_post", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder fenceSide(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/fence_side", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder fenceGate(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder fenceGateOpen(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_open", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder fenceGateWall(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder fenceGateWallOpen(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall_open", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder wallPost(String name, ResourceLocation wall, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_post", "wall", wall, isConnectingTexture);
    }

    public ConnectingModelBuilder wallSide(String name, ResourceLocation wall, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_side", "wall", wall, isConnectingTexture);
    }

    public ConnectingModelBuilder wallSideTall(String name, ResourceLocation wall, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_wall_side_tall", "wall", wall, isConnectingTexture);
    }

    private ConnectingModelBuilder pane(String name, String parent, ResourceLocation pane, boolean paneConnecting, ResourceLocation edge, boolean edgeConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/" + parent)
            .texture("pane", pane, paneConnecting)
            .texture("edge", edge, edgeConnecting);
    }

    public ConnectingModelBuilder panePost(String name, ResourceLocation pane, boolean paneConnecting, ResourceLocation edge, boolean edgeConnecting){
        return this.pane(name, "template_glass_pane_post", pane, paneConnecting, edge, edgeConnecting);
    }

    public ConnectingModelBuilder paneSide(String name, ResourceLocation pane, boolean paneConnecting, ResourceLocation edge, boolean edgeConnecting){
        return this.pane(name, "template_glass_pane_side", pane, paneConnecting, edge, edgeConnecting);
    }

    public ConnectingModelBuilder paneSideAlt(String name, ResourceLocation pane, boolean paneConnecting, ResourceLocation edge, boolean edgeConnecting){
        return this.pane(name, "template_glass_pane_side_alt", pane, paneConnecting, edge, edgeConnecting);
    }

    public ConnectingModelBuilder paneNoSide(String name, ResourceLocation pane, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside", "pane", pane, isConnectingTexture);
    }

    public ConnectingModelBuilder paneNoSideAlt(String name, ResourceLocation pane, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside_alt", "pane", pane, isConnectingTexture);
    }

    private ConnectingModelBuilder door(String name, String model, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.withExistingParent(name, BLOCK_FOLDER + "/" + model)
            .texture("bottom", bottom, bottomConnecting)
            .texture("top", top, topConnecting);
    }

    public ConnectingModelBuilder doorBottomLeft(String name, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.door(name, "door_bottom", bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder doorBottomRight(String name, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.door(name, "door_bottom_rh", bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder doorTopLeft(String name, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.door(name, "door_top", bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder doorTopRight(String name, ResourceLocation bottom, boolean bottomConnecting, ResourceLocation top, boolean topConnecting){
        return this.door(name, "door_top_rh", bottom, bottomConnecting, top, topConnecting);
    }

    public ConnectingModelBuilder trapdoorBottom(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_bottom", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder trapdoorTop(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_top", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder trapdoorOpen(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_open", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder trapdoorOrientableBottom(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder trapdoorOrientableTop(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder trapdoorOrientableOpen(String name, ResourceLocation texture, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture, isConnectingTexture);
    }

    public ConnectingModelBuilder carpet(String name, ResourceLocation wool, boolean isConnectingTexture){
        return this.singleTexture(name, BLOCK_FOLDER + "/carpet", "wool", wool, isConnectingTexture);
    }
}
