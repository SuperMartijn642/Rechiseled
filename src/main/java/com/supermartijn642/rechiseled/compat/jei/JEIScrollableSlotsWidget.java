package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.CursorTypes;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * Created 15/01/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsWidget {

    private static final ResourceLocation SCROLLBAR_BACKGROUND = Rechiseled.identifier("textures/screen/jei_scrollbar_background.png");
    private static final ResourceLocation SCROLLER = Rechiseled.identifier("textures/screen/scroller.png");

    private static final int WIDTH = 178, HEIGHT = 72;
    private static final int BOX_WIDTH = 162, SCROLLBAR_WIDTH = 14, SCROLLER_HEIGHT = 15;
    private static final int COLUMNS = 9, ROWS = 4;
    private static final int SLOT_WIDTH = 18, SLOT_HEIGHT = 18;

    private final int x, y;
    private final List<JEIScrollableSlotsSlot> slots;
    private final IDrawable slotBackground;
    private final int rows;
    private final boolean scrollable;
    private float scrollerPosition = 0, targetPosition;
    private final float scrollerSpeed;
    private boolean dragging;

    public JEIScrollableSlotsWidget(int x, int y, List<JEIScrollableSlotsSlot> slots, IDrawable slotBackground){
        this.x = x;
        this.y = y;
        this.slots = List.copyOf(slots);
        this.slotBackground = slotBackground;
        this.rows = (int)Math.ceil((float)slots.size() / COLUMNS);
        this.scrollable = this.rows > ROWS;
        this.scrollerSpeed = 0.1f;
        this.updateSlots();
    }

    private void setScrollTarget(float targetPosition){
        this.targetPosition = Mth.clamp(targetPosition, 0, 1);
    }

    private void tryScrollToTarget(){
        float scrollerPosition = Mth.clamp(this.targetPosition, this.scrollerPosition - this.scrollerSpeed, this.scrollerPosition + this.scrollerSpeed);
        if(scrollerPosition == this.scrollerPosition)
            return;
        this.scrollerPosition = scrollerPosition;
        // Update slot positions
        this.updateSlots();
    }

    private void updateSlots(){
        float rowOffset = this.getRowOffset();
        loop:
        for(int row = 0; row < this.rows; row++){
            for(int column = 0; column < COLUMNS; column++){
                int index = row * COLUMNS + column;
                if(index >= this.slots.size())
                    break loop;
                this.slots.get(index).updatePosition(
                    this.x + column * SLOT_WIDTH,
                    this.y + row * SLOT_HEIGHT - (int)(rowOffset * SLOT_HEIGHT),
                    this.x, this.y,
                    BOX_WIDTH, HEIGHT
                );
            }
        }
    }

    private void updateDrag(int mouseY){
        this.setScrollTarget((mouseY - this.y - 1 - SCROLLER_HEIGHT / 2f) / (HEIGHT - SCROLLER_HEIGHT - 2));
    }

    private float getRowOffset(){
        return this.scrollerPosition * (this.rows - ROWS);
    }

    public void draw(PoseStack poseStack, double mouseX, double mouseY){
        poseStack.pushPose();
        poseStack.translate(this.x, this.y, 0);

        // Update dragging
        if(this.scrollable){
            if(this.dragging){
                this.updateDrag((int)mouseY);
                ScreenUtils.requestCursor(CursorTypes.resizeVertical());
            }else if(mouseX > this.x + BOX_WIDTH + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT)
                ScreenUtils.requestCursor(CursorTypes.pointingHand());
            this.tryScrollToTarget();
        }

        // Render scrollbar
        ScreenUtils.bindTexture(SCROLLBAR_BACKGROUND);
        ScreenUtils.drawTexture(poseStack, BOX_WIDTH + 2, 0, SCROLLBAR_WIDTH, HEIGHT);
        // Render cursor
        if(this.scrollable){
            float y = 1 + this.scrollerPosition * (HEIGHT - SCROLLER_HEIGHT - 2);
            boolean highlighted = this.dragging || (mouseX > this.x + BOX_WIDTH + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT);
            ScreenUtils.bindTexture(SCROLLER);
            ScreenUtils.drawTexture(poseStack, BOX_WIDTH + 3, y, 12, SCROLLER_HEIGHT, 0, highlighted ? 1 / 3f : 0, 1, 1 / 3f);
        }else{
            ScreenUtils.bindTexture(SCROLLER);
            ScreenUtils.drawTexture(poseStack, BOX_WIDTH + 3, 1, SCROLLBAR_WIDTH - 2, SCROLLER_HEIGHT, 0, 2 / 3f, 1, 1 / 3f);
        }

        // Render missing slots
        ScreenUtils.withScissor(poseStack, 0, 0, BOX_WIDTH, HEIGHT, () -> {
            float rowOffset = this.getRowOffset();
            for(int row = 0; row < ROWS + 1; row++){
                for(int column = 0; column < COLUMNS; column++){
                    int index = (row + (int)rowOffset) * COLUMNS + column;
                    if(index >= this.slots.size())
                        this.slotBackground.draw(poseStack, column * SLOT_WIDTH, row * SLOT_HEIGHT - (int)((rowOffset % 1) * SLOT_HEIGHT));
                }
            }
        });

        poseStack.popPose();
    }

    public boolean mousePressed(double mouseX, double mouseY, int button){
        if(this.scrollable && button == 0 && mouseX > this.x + BOX_WIDTH + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT){
            this.dragging = true;
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button){
        if(button == 0)
            this.dragging = false;
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amountY){
        if(amountY == 0 || !this.scrollable)
            return false;
        if(mouseX > this.x + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT){
            this.setScrollTarget(this.targetPosition - 0.8f / (this.rows - ROWS) * (float)amountY);
            return true;
        }
        return false;
    }
}
