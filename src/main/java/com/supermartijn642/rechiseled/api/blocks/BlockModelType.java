package com.supermartijn642.rechiseled.api.blocks;

/**
 * Created 10/05/2023 by SuperMartijn642
 */
public enum BlockModelType {

    /**
     * A model with a different texture for each face.
     * <p>
     * Textures:<br>
     * - &lt;identifier&gt;_up<br>
     * - &lt;identifier&gt;_down<br>
     * - &lt;identifier&gt;_north<br>
     * - &lt;identifier&gt;_east<br>
     * - &lt;identifier&gt;_south<br>
     * - &lt;identifier&gt;_west
     */
    CUBE,
    /**
     * A model with the same texture for all faces.
     * <p>
     * Textures:<br>
     * - &lt;identifier&gt;
     */
    CUBE_ALL,
    /**
     * A model with the one texture for the sides and one texture for the top and bottom.
     * <p>
     * Textures:<br>
     * - &lt;identifier&gt;_side<br>
     * - &lt;identifier&gt;_end
     */
    PILLAR
}
