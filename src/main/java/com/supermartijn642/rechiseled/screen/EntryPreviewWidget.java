package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.widget.BaseWidget;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class EntryPreviewWidget extends BaseWidget {

    private static final int ROTATION_TIME = 10000;

    private final Supplier<ItemStack> item;
    private final Supplier<Integer> previewMode;

    private float yaw = 0.35f, pitch = 30;
    private long lastRotationTime;
    private boolean dragging = false;
    private int mouseStartX, mouseStartY;

    public EntryPreviewWidget(int x, int y, int width, int height,
                              Supplier<ItemStack> item,
                              Supplier<Integer> previewMode){
        super(x, y, width, height);
        this.item = item;
        this.previewMode = previewMode;
        this.lastRotationTime = System.currentTimeMillis();
    }

    @Override
    protected void addWidgets(){
        this.addWidget(new ToggleRotationButton(this.x, this.y, 11, 11));
        super.addWidgets();
    }

    @Override
    public ITextComponent getNarrationMessage(){
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY){
        long now = System.currentTimeMillis();

        ItemStack itemStack = this.item.get();
        int previewMode = this.previewMode.get();
        if(itemStack != null && previewMode >= 0 && previewMode <= 2){
            Item item = itemStack.getItem();
            int data = itemStack.getMetadata();
            // Update the rotation
            if(this.dragging){
                this.yaw += (mouseX - this.mouseStartX) / 100d * 360;
                this.pitch += (mouseY - this.mouseStartY) / 100d * 360;
                this.mouseStartX = mouseX;
                this.mouseStartY = mouseY;
            }else if(ToggleRotationButton.rotate)
                this.yaw += (double)(now - this.lastRotationTime) / ROTATION_TIME * 360;

            // Render the item or block
            if(item instanceof ItemBlock){
                // Render block
                Block block = ((ItemBlock)item).getBlock();
                IBlockState state = item.getHasSubtypes() ? block.getStateFromMeta(data) : block.getDefaultState();
                BlockCapture capture;
                if(previewMode == 0)
                    capture = new BlockCapture(state);
                else if(previewMode == 1){
                    capture = new BlockCapture(state);
                    capture.putBlock(new BlockPos(-1, 0, 0), state);
                    capture.putBlock(new BlockPos(1, 0, 0), state);
                }else{
                    capture = new BlockCapture();
                    for(int i = 0; i < 9; i++)
                        capture.putBlock(new BlockPos(i / 3 - 1, i % 3 - 1, 0), state);
                }
                ScreenBlockRenderer.drawBlock(capture, this.x + this.width / 2d, this.y + this.height / 2d, this.width, this.yaw, this.pitch, false);
            }else{
                // Render item
                ScreenItemRender.drawItem(itemStack, this.x + this.width / 2d, this.y + this.height / 2d, this.width, this.yaw, this.pitch, true);
            }
        }

        this.lastRotationTime = now;

        super.render(mouseX, mouseY);
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
