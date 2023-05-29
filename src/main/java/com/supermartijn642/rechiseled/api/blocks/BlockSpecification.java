package com.supermartijn642.rechiseled.api.blocks;

/**
 * Created 18/04/2023 by SuperMartijn642
 */
public enum BlockSpecification {

    /**
     * A basic cube, for example cobblestone
     */
    BASIC(BlockModelType.CUBE_ALL),
    /**
     * A pillar block which can be aligned along each axis, for example oak log
     */
    PILLAR(BlockModelType.PILLAR),
    /**
     * A glass block
     */
    GLASS(BlockModelType.CUBE_ALL),
    /**
     * A glass block which is also a pillar block
     * @see #PILLAR
     */
    GLASS_PILLAR(BlockModelType.PILLAR);

    private final BlockModelType modelType;

    BlockSpecification(BlockModelType defaultModelType){
        this.modelType = defaultModelType;
    }

    public BlockModelType getDefaultModelType(){
        return this.modelType;
    }
}
