package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.supermartijn642.core.gui.widget.Widget;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

/**
 * Created 25/12/2021 by SuperMartijn642
 */
public class BlockPreviewWidget extends Widget {

    private static final int ROTATION_TIME = 10000;

    private final Supplier<Block> block;
    private final Supplier<Integer> previewMode;
    private final Supplier<Integer> guiLeft, guiTop;

    private float yaw = 0.35f, pitch = 30;
    private long lastRotationTime;
    private boolean dragging = false;
    private int mouseStartX, mouseStartY;

    public BlockPreviewWidget(int x, int y, int width, int height,
                              Supplier<Block> block,
                              Supplier<Integer> previewMode,
                              Supplier<Integer> guiLeft,
                              Supplier<Integer> guiTop){
        super(x, y, width, height);
        this.block = block;
        this.previewMode = previewMode;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        this.lastRotationTime = System.currentTimeMillis();
    }

    @Override
    protected ITextComponent getNarrationMessage(){
        return null;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        long now = System.currentTimeMillis();

        Block block = this.block.get();
        int previewMode = this.previewMode.get();
        if(block != null && previewMode >= 0 && previewMode <= 2){
            if(this.dragging){
                this.yaw += (mouseX - this.mouseStartX) / 100d * 360;
                this.pitch += (mouseY - this.mouseStartY) / 100d * 360;
                this.mouseStartX = mouseX;
                this.mouseStartY = mouseY;
            }else
                this.yaw += (double)(now - this.lastRotationTime) / ROTATION_TIME * 360;

            BlockCapture capture;
            if(previewMode == 0)
                capture = new BlockCapture(block);
            else if(previewMode == 1){
                capture = new BlockCapture(block);
                capture.putBlock(new BlockPos(-1, 0, 0), block);
                capture.putBlock(new BlockPos(1, 0, 0), block);
            }else{
                capture = new BlockCapture();
                for(int i = 0; i < 9; i++)
                    capture.putBlock(new BlockPos(i / 3 - 1, i % 3 - 1, 0), block);
            }
            ScreenBlockRenderer.drawBlock(capture, this.guiLeft.get() + this.x + this.width / 2d, this.guiTop.get() + this.y + this.height / 2d, this.width, this.yaw, this.pitch, false);
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
