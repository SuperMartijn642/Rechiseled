package com.supermartijn642.rechiseled.data;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.core.Direction;

/**
 * Created 11/01/2026 by SuperMartijn642
 */
public class RechiseledBlockModelGenerator extends ModelGenerator {

    public RechiseledBlockModelGenerator(ResourceCache cache){
        super(Rechiseled.MODID, cache);
    }

    @Override
    public void generate(){
        // Create top stair models
        this.model("block/stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.DOWN, face -> face.texture("bottom"))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 8, 16, 16))
            );
        this.model("block/inner_stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.DOWN, face -> face.texture("bottom"))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 8, 16, 16))
            )
            .element(element ->
                element.from(0, 0, 0).to(8, 8, 8)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(0, 0, 8, 8))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").uv(0, 8, 8, 16))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
            );
        this.model("block/outer_stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.DOWN, face -> face.texture("bottom"))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 8)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 8))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").uv(8, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 8, 8, 16))
            );

        // Create glass stair models
        this.model("block/glass_stairs")
            .parent("minecraft", "block/stairs") // Use regular stairs as parent so we can inherit item transforms
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(0, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
            )
            .element(element ->
                element.from(0, 0, 0).to(8, 8, 16)
                    .face(Direction.UP, face -> face.texture("top").uv(0, 0, 8, 16))
            )
            .element(element ->
                element.from(8, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 8, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 0, 16, 8))
            );
        this.model("block/glass_inner_stairs")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(0, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
            )
            .element(element ->
                element.from(0, 0, 0).to(8, 8, 8)
                    .face(Direction.UP, face -> face.texture("top").uv(0, 0, 8, 8))
            )
            .element(element ->
                element.from(8, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 8, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 0, 16, 8))
            )
            .element(element ->
                element.from(8, 8, 0).to(16, 16, 8)
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 0, 8, 8))
            )
            .element(element ->
                element.from(0, 8, 8).to(8, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface().uv(0, 8, 8, 16))
                    .face(Direction.NORTH, face -> face.texture("side").uv(8, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 8, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(8, 0, 16, 8))
            );
        this.model("block/glass_outer_stairs")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(0, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
            )
            .element(element ->
                element.from(0, 0, 0).to(8, 8, 16)
                    .face(Direction.UP, face -> face.texture("top").uv(0, 0, 8, 16))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 8)
                    .face(Direction.UP, face -> face.texture("top").uv(8, 0, 16, 8))
            )
            .element(element ->
                element.from(8, 8, 8).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface().uv(8, 8, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").uv(0, 0, 8, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 8, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").uv(8, 0, 16, 8))
            );
        this.model("block/glass_stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(0, 8, 0).to(8, 16, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").uv(0, 0, 8, 16))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 8, 16, 16))
            );
        this.model("block/glass_inner_stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 8).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(0, 8, 0).to(8, 16, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").uv(0, 8, 8, 16))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 16))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
            )
            .element(element ->
                element.from(8, 0, 8).to(16, 8, 16)
                    .face(Direction.WEST, face -> face.texture("side").uv(8, 8, 16, 16))
            )
            .element(element ->
                element.from(0, 0, 0).to(8, 8, 8)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(0, 0, 8, 8))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").uv(0, 8, 8, 16))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
            );
        this.model("block/glass_outer_stairs_top")
            .texture("particle", "#side")
            .element(element ->
                element.from(0, 8, 0).to(16, 16, 16)
                    .face(Direction.UP, face -> face.texture("top").cullface())
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.SOUTH, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
                    .face(Direction.WEST, face -> face.texture("side").cullface().uv(0, 0, 16, 8))
            )
            .element(element ->
                element.from(0, 8, 0).to(8, 16, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").uv(0, 0, 8, 16))
            )
            .element(element ->
                element.from(8, 8, 8).to(16, 16, 16)
                    .face(Direction.DOWN, face -> face.texture("bottom").uv(0, 8, 8, 16))
            )
            .element(element ->
                element.from(8, 0, 0).to(16, 8, 8)
                    .face(Direction.DOWN, face -> face.texture("bottom").cullface().uv(8, 0, 16, 8))
                    .face(Direction.NORTH, face -> face.texture("side").cullface().uv(0, 8, 8, 16))
                    .face(Direction.EAST, face -> face.texture("side").cullface().uv(8, 8, 16, 16))
                    .face(Direction.SOUTH, face -> face.texture("side").uv(8, 8, 16, 16))
                    .face(Direction.WEST, face -> face.texture("side").uv(0, 8, 8, 16))
            );
    }

    @Override
    public String getName(){
        return this.modName + " Block Model Generator";
    }
}
