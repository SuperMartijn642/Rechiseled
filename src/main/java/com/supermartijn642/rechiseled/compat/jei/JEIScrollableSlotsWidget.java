package com.supermartijn642.rechiseled.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.gui.CursorTypes;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.rechiseled.Rechiseled;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Optional;

/**
 * Created 12/02/2026 by SuperMartijn642
 */
public class JEIScrollableSlotsWidget implements ISlottedRecipeWidget, IJeiInputHandler, JEIOutOfBoundsInputListener {

    private static final ResourceLocation BOX_BACKGROUND = Rechiseled.identifier("textures/screen/jei_box_background.png");
    private static final ResourceLocation SCROLLBAR_BACKGROUND = Rechiseled.identifier("textures/screen/jei_scrollbar_background.png");
    private static final ResourceLocation SCROLLER = Rechiseled.identifier("textures/screen/scroller.png");

    private static final int WIDTH = 178, HEIGHT = 72;
    private static final int BOX_WIDTH = 162, SCROLLBAR_WIDTH = 14, SCROLLER_HEIGHT = 15;
    private static final int COLUMNS = 9, ROWS = 4;
    private static final int SLOT_WIDTH = 18, SLOT_HEIGHT = 18;

    private final int x, y;
    private final ScreenRectangle bounds;
    private final List<IRecipeSlotDrawable> slots;
    private final int rows;
    private final boolean scrollable;
    private float scrollerPosition = 0, targetPosition;
    private final float scrollerSpeed;
    private boolean dragging;

    private final IDrawable slotBackground;

    public JEIScrollableSlotsWidget(int x, int y, List<IRecipeSlotDrawable> slots, IDrawable slotBackground){
        this.x = x;
        this.y = y;
        this.bounds = new ScreenRectangle(this.x, this.y, WIDTH, HEIGHT);
        this.slots = List.copyOf(slots);
        this.rows = Mth.positiveCeilDiv(slots.size(), COLUMNS);
        this.scrollable = this.rows > ROWS;
        this.scrollerSpeed = 0.1f;
        this.slotBackground = slotBackground;
    }

    private void setScrollTarget(float targetPosition){
        this.targetPosition = Mth.clamp(targetPosition, 0, 1);
    }

    private void tryScrollToTarget(){
        this.scrollerPosition = Mth.clamp(this.targetPosition, this.scrollerPosition - this.scrollerSpeed, this.scrollerPosition + this.scrollerSpeed);
    }

    private void updateDrag(float mouseY){
        this.setScrollTarget((mouseY - 1 - SCROLLER_HEIGHT / 2f) / (HEIGHT - SCROLLER_HEIGHT - 2));
    }

    private float getRowOffset(){
        return this.scrollerPosition * (this.rows - ROWS);
    }

    @Override
    public ScreenRectangle getArea(){
        return this.bounds;
    }

    @Override
    public void drawWidget(GuiGraphics graphics, double mouseX, double mouseY){
        PoseStack pose = graphics.pose();

        // Update dragging
        if(this.scrollable){
            if(this.dragging){
                this.updateDrag((float)mouseY);
                ScreenUtils.requestCursor(CursorTypes.resizeVertical());
            }else if(mouseX > BOX_WIDTH + 2 && mouseX < WIDTH && mouseY > 0 && mouseY < HEIGHT)
                ScreenUtils.requestCursor(CursorTypes.pointingHand());
            this.tryScrollToTarget();
        }

        // Render scrollbar
        ScreenUtils.bindTexture(SCROLLBAR_BACKGROUND);
        ScreenUtils.drawTexture(pose, BOX_WIDTH + 2, 0, SCROLLBAR_WIDTH, HEIGHT);
        // Render cursor
        if(this.scrollable){
            float y = 1 + this.scrollerPosition * (HEIGHT - SCROLLER_HEIGHT - 2);
            boolean highlighted = this.dragging || (mouseX > BOX_WIDTH + 2 && mouseX < WIDTH && mouseY > 0 && mouseY < HEIGHT);
            ScreenUtils.bindTexture(SCROLLER);
            ScreenUtils.drawTexture(pose, BOX_WIDTH + 3, y, 12, SCROLLER_HEIGHT, 0, highlighted ? 1 / 3f : 0, 1, 1 / 3f);
        }else{
            ScreenUtils.bindTexture(SCROLLER);
            ScreenUtils.drawTexture(pose, BOX_WIDTH + 3, 1, SCROLLBAR_WIDTH - 2, SCROLLER_HEIGHT, 0, 2 / 3f, 1, 1 / 3f);
        }

        // Render slots
        ScreenUtils.bindTexture(BOX_BACKGROUND);
        ScreenUtils.drawTexture(pose, 0, 0, BOX_WIDTH, HEIGHT);
        ScreenUtils.withScissor(pose, 0, 0, BOX_WIDTH, HEIGHT, () -> {
            float rowOffset = this.getRowOffset();
            for(int row = 0; row < ROWS + 1; row++){
                for(int column = 0; column < COLUMNS; column++){
                    pose.pushPose();
                    pose.translate(column * SLOT_WIDTH + 1, row * SLOT_HEIGHT - (rowOffset % 1) * SLOT_HEIGHT + 1, 0);
                    int index = (row + (int)rowOffset) * COLUMNS + column;
                    this.slotBackground.draw(graphics, 0, 0);
                    if(index < this.slots.size()){
                        IRecipeSlotDrawable slot = this.slots.get(index);
                        slot.draw(graphics);
                    }
                    pose.popPose();
                }
            }
        });
    }

    @Override
    public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input){
        if(input.getKey().getType() == InputConstants.Type.MOUSE && input.getKey().getValue() == 0){
            if(this.scrollable && input.isSimulate() && mouseX > BOX_WIDTH + 2 && mouseX < WIDTH && mouseY > 0 && mouseY < HEIGHT){
                this.dragging = true;
                return true;
            }
            if(!input.isSimulate())
                this.dragging = false;
        }
        return false;
    }

    @Override
    public void rechiseledOutOfBoundsInput(double mouseX, double mouseY, IJeiUserInput input){
        this.handleInput(mouseX, mouseY, input);
    }

    @Override
    public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDelta){
        if(scrollDelta == 0 || !this.scrollable)
            return false;
        this.setScrollTarget(this.targetPosition - 0.8f / (this.rows - ROWS) * (float)scrollDelta);
        return true;
    }

    @Override
    public boolean handleMouseDragged(double mouseX, double mouseY, InputConstants.Key mouseKey, double dragX, double dragY){
        return this.dragging;
    }

    @Override
    public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY){
        if(mouseX < 0 || mouseX > BOX_WIDTH || mouseY < 0 || mouseY > HEIGHT)
            return Optional.empty();
        float rowOffset = this.getRowOffset();
        int column = (int)(mouseX / SLOT_WIDTH);
        int row = (int)((float)mouseY / SLOT_HEIGHT + rowOffset);
        int index = column + row * COLUMNS;
        if(index >= this.slots.size())
            return Optional.empty();
        return Optional.of(new RecipeSlotUnderMouse(
            this.slots.get(index),
            this.x + column * SLOT_WIDTH + 1,
            this.y + (int)((row - rowOffset) * SLOT_HEIGHT) + 1
        ));
    }

    @Override
    public ScreenPosition getPosition(){
        return new ScreenPosition(this.x, this.y);
    }
}
