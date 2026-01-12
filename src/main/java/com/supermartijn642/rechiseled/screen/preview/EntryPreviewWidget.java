package com.supermartijn642.rechiseled.screen.preview;

import com.supermartijn642.core.gui.CursorType;
import com.supermartijn642.core.gui.CursorTypes;
import com.supermartijn642.core.gui.widget.BaseWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.rechiseled.screen.ToggleRotationButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class EntryPreviewWidget extends BaseWidget {

    private static final int ROTATION_TIME = 10000;

    private static boolean rotatePreview = true;
    private static float yaw = 0.35f, pitch = 30;

    private final Supplier<Item> item;
    private final Supplier<PreviewMode> previewMode;

    private long lastRotationTime;
    private boolean dragging = false;
    private int mouseStartX, mouseStartY;

    public EntryPreviewWidget(int x, int y, int width, int height,
                              Supplier<Item> item,
                              Supplier<PreviewMode> previewMode){
        super(x, y, width, height);
        this.item = item;
        this.previewMode = previewMode;
        this.lastRotationTime = System.currentTimeMillis();
    }

    @Override
    protected void addWidgets(){
        this.addWidget(new ToggleRotationButton(this.x, this.y, 11, 11, () -> rotatePreview, () -> rotatePreview = !rotatePreview, () -> this.item.get() != null));
        super.addWidgets();
    }

    @Override
    public Component getNarrationMessage(){
        return null;
    }

    @Override
    public CursorType curser(int mouseX, int mouseY){
        CursorType curser = super.curser(mouseX, mouseY);
        return curser == null && this.item.get() != null ? CursorTypes.resizeAll() : curser;
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        long now = System.currentTimeMillis();

        Item item = this.item.get();
        PreviewMode previewMode = this.previewMode.get();
        if(item != null){
            // Update the rotation
            if(this.dragging){
                yaw += (float)((mouseX - this.mouseStartX) / 100d * 360);
                pitch += (float)((mouseY - this.mouseStartY) / 100d * 360);
                this.mouseStartX = mouseX;
                this.mouseStartY = mouseY;
            }else if(rotatePreview)
                yaw += (float)(now - this.lastRotationTime) / ROTATION_TIME * 360;

            // Render the item or block
            if(item instanceof BlockItem){
                // Render block
                Block block = ((BlockItem)item).getBlock();
                BlockCapture capture = new BlockCapture();
                int xRange = previewMode == PreviewMode.SINGLE ? 1 : 2;
                int yRange = previewMode == PreviewMode.PANEL ? 2 : 1;
                for(int x = -xRange; x <= xRange; x++){
                    for(int y = -yRange; y <= yRange; y++){
                        BlockPos pos = new BlockPos(x, y, 0);
                        if(Math.abs(x) == xRange || Math.abs(y) == yRange)
                            capture.putBlock(pos, Blocks.COBBLESTONE.defaultBlockState());
                        else
                            capture.putBlock(pos, block);
                    }
                }
                capture.updateShapes();
                for(int x = -xRange; x <= xRange; x++){
                    for(int y = -yRange; y <= yRange; y++){
                        if(Math.abs(x) == xRange || Math.abs(y) == yRange)
                            capture.putBlock(new BlockPos(x, y, 0), (BlockState)null);
                    }
                }
                ScreenBlockRenderer.drawBlock(context.poseStack(), capture, this.x + this.width / 2d, this.y + this.height / 2d, this.width, yaw, pitch, true);
            }else{
                // Render item
                ScreenItemRenderer.drawItem(context.poseStack(), item, this.x + this.width / 2d, this.y + this.height / 2d, this.width, yaw, pitch, true);
            }
        }

        this.lastRotationTime = now;

        super.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled){
        hasBeenHandled |= super.mousePressed(mouseX, mouseY, button, hasBeenHandled);
        if(!hasBeenHandled && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
            this.dragging = true;
            this.mouseStartX = mouseX;
            this.mouseStartY = mouseY;
            return true;
        }
        return hasBeenHandled;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button, boolean hasBeenHandled){
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button, hasBeenHandled);
    }
}
