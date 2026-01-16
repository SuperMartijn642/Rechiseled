package com.supermartijn642.rechiseled.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.CursorTypes;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.client.gui.widgets.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 15/01/2026 by SuperMartijn642
 */
public class REIScrollableSlotsWidget extends WidgetWithBounds implements DraggableStackProviderWidget {

    private static final ResourceLocation SCROLLBAR_BACKGROUND = Rechiseled.identifier("textures/screen/rei_scrollbar_background.png");
    private static final ResourceLocation SCROLLER = Rechiseled.identifier("textures/screen/scroller.png");

    private static final int WIDTH = 178, HEIGHT = 72;
    private static final int BOX_WIDTH = 162, SCROLLBAR_WIDTH = 14, SCROLLER_HEIGHT = 15;
    private static final int COLUMNS = 9, ROWS = 4;
    private static final int SLOT_WIDTH = 18, SLOT_HEIGHT = 18;

    private static final Widget SLOT_BACKGROUND = Widgets.createSlotBackground(new Point(0, 0));

    private final int x, y;
    private final Rectangle bounds;
    private final List<Slot> slots;
    private final int rows;
    private final boolean scrollable;
    private float scrollerPosition = 0, targetPosition;
    private final float scrollerSpeed;
    private boolean dragging;

    public REIScrollableSlotsWidget(int x, int y, List<Slot> slots){
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(this.x, this.y, WIDTH, HEIGHT);
        this.slots = List.copyOf(slots);
        this.rows = (int)Math.ceil((float)slots.size() / COLUMNS);
        this.scrollable = this.rows > ROWS;
        this.scrollerSpeed = 0.1f;
    }

    private void setScrollTarget(float targetPosition){
        this.targetPosition = Mth.clamp(targetPosition, 0, 1);
    }

    private void tryScrollToTarget(){
        this.scrollerPosition = Mth.clamp(this.targetPosition, this.scrollerPosition - this.scrollerSpeed, this.scrollerPosition + this.scrollerSpeed);
    }

    private void updateDrag(int mouseY){
        this.setScrollTarget((mouseY - this.y - 1 - SCROLLER_HEIGHT / 2f) / (HEIGHT - SCROLLER_HEIGHT - 2));
    }

    private float getRowOffset(){
        return this.scrollerPosition * (this.rows - ROWS);
    }

    private <T> T getSlotMouseInteraction(int mouseX, int mouseY, SlotInteraction<T> interaction, T defaultValue){
        mouseX -= this.x;
        mouseY -= this.y;
        if(mouseX < 0 || mouseX > BOX_WIDTH || mouseY < 0 || mouseY > HEIGHT)
            return defaultValue;
        float rowOffset = this.getRowOffset();
        int column = mouseX / SLOT_WIDTH;
        int row = (int)((float)mouseY / SLOT_HEIGHT + rowOffset);
        int index = column + row * COLUMNS;
        if(index >= this.slots.size())
            return defaultValue;
        //noinspection IntegerDivisionInFloatingPointContext
        return interaction.perform(
            this.slots.get(index),
            mouseX - column * SLOT_WIDTH - 1,
            (int)(mouseY - (mouseY / SLOT_HEIGHT + rowOffset % 1) * SLOT_HEIGHT) - 1
        );
    }

    @Override
    public Rectangle getBounds(){
        return this.bounds;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks){
        poseStack.pushPose();
        poseStack.translate(this.x, this.y, 0);

        // Update dragging
        if(this.scrollable){
            if(this.dragging){
                this.updateDrag(mouseY);
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

        // Render slots
        ScreenUtils.withScissor(poseStack, 0, 0, BOX_WIDTH, HEIGHT, () -> {
            float rowOffset = this.getRowOffset();
            for(int row = 0; row < ROWS + 1; row++){
                for(int column = 0; column < COLUMNS; column++){
                    poseStack.pushPose();
                    poseStack.translate(column * SLOT_WIDTH + 1, row * SLOT_HEIGHT - (rowOffset % 1) * SLOT_HEIGHT + 1, 0);
                    int index = (row + (int)rowOffset) * COLUMNS + column;
                    if(index < this.slots.size()){
                        Slot slot = this.slots.get(index);
                        slot.render(poseStack, mouseX - this.x - column * SLOT_WIDTH - 1, (int)(mouseY - this.y - (row - (rowOffset % 1)) * SLOT_HEIGHT) - 1, partialTicks);
                    }else
                        SLOT_BACKGROUND.render(poseStack, 0, 0, partialTicks);
                    poseStack.popPose();
                }
            }
        });

        poseStack.popPose();
    }

    @Nullable
    @Override
    public Tooltip getTooltip(Point mouse){
        return this.getSlotMouseInteraction(
            mouse.x, mouse.y,
            (slot, relativeMouseX, relativeMouseY) ->
                slot.getTooltip(new Point(relativeMouseX, relativeMouseY)),
            null
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(this.scrollable && button == 0 && mouseX > this.x + BOX_WIDTH + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT){
            this.dragging = true;
            return true;
        }
        return this.getSlotMouseInteraction(
            (int)mouseX, (int)mouseY,
            (slot, relativeMouseX, relativeMouseY) -> slot.mouseClicked(relativeMouseX, relativeMouseY, button),
            false
        );
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        if(button == 0)
            this.dragging = false;
        return this.getSlotMouseInteraction(
            (int)mouseX, (int)mouseY,
            (slot, relativeMouseX, relativeMouseY) -> slot.mouseReleased(relativeMouseX, relativeMouseY, button),
            false
        );
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY){
        if(this.dragging)
            return true;
        return this.getSlotMouseInteraction(
            (int)mouseX, (int)mouseY,
            (slot, relativeMouseX, relativeMouseY) -> slot.mouseDragged(relativeMouseX, relativeMouseY, button, deltaX, deltaY),
            false
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amountY){
        if(amountY == 0 || !this.scrollable)
            return false;
        if(mouseX > this.x + 2 && mouseX < this.x + WIDTH && mouseY > this.y && mouseY < this.y + HEIGHT){
            this.setScrollTarget(this.targetPosition - 0.8f / (this.rows - ROWS) * (float)amountY);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable DraggableStack getHoveredStack(DraggingContext<Screen> context, double mouseX, double mouseY){
        return this.getSlotMouseInteraction(
            (int)mouseX, (int)mouseY,
            (slot, relativeMouseX, relativeMouseY) ->
                slot instanceof DraggableStackProviderWidget ? ((DraggableStackProviderWidget)slot).getHoveredStack(context, relativeMouseX, relativeMouseY) : null,
            null
        );
    }

    @Override
    public List<? extends GuiEventListener> children(){
        return List.of();
    }

    private interface SlotInteraction<T> {
        T perform(Slot slot, int relativeMouseX, int relativeMouseY);
    }
}
