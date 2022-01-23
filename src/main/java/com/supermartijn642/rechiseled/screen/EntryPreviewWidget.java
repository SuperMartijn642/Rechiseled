package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.widget.Widget;
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
public class EntryPreviewWidget extends Widget {

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
    protected ITextComponent getNarrationMessage(){
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
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
            }else
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
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button){
        if(mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
            this.dragging = true;
            this.mouseStartX = mouseX;
            this.mouseStartY = mouseY;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button){
        this.dragging = false;
    }
}
