package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BaseWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ScrollWidget extends BaseWidget {

    private static final ResourceLocation HANDLES = new ResourceLocation("rechiseled", "textures/screen/handles.png");

    public ScrollWidget(int x, int y, int width, int height, Runnable onScrollChanged) {
        super(x, y, width, height);
        this.onScrollChanged = onScrollChanged;
    }

    private final Runnable onScrollChanged;

    private final int handleWidth = 14;

    private final int handleHeight = 17;

    private boolean isDragging = false;

    private float scrollRatio = 0f;

    private int intSnapMax = 0;

    private boolean isActive = true;

    public void setScrollRatio(float scrollRatio){
        if(!this.isActive)
            return;

        if (scrollRatio > 1f)
            scrollRatio = 1f;

        if (scrollRatio < 0f)
            scrollRatio = 0f;

        this.scrollRatio = scrollRatio;
    }

    public float getScrollRatio(){
        return this.scrollRatio;
    }

    public void setIntSnapMax(int intSnapMax){
        this.intSnapMax = intSnapMax;
    }

    public int getIntSnapMax(){
        return this.intSnapMax;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
        if(!isActive)
            this.scrollRatio = 0f;
    }

    public boolean getIsActive(){
        return this.isActive;
    }

    public float getHandleY(){
        return this.y + ((this.height - this.handleHeight) * this.scrollRatio);
    }

    public float getHandleHalfHeight(){
        return this.handleHeight * 0.5f;
    }

    @Override
    public Component getNarrationMessage() { return null; }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY) {
        if(isActive && isDragging){
            float handleHalfHeight = getHandleHalfHeight();
            float mouseYRelative = (mouseY - (float) this.y - handleHalfHeight) / Math.max(1, this.height - this.handleHeight);

            if(intSnapMax > 0)
                mouseYRelative = (float) Math.round(mouseYRelative * intSnapMax) / intSnapMax;

            setScrollRatio(mouseYRelative);
            if (onScrollChanged != null)
                onScrollChanged.run();
        }

        ScreenUtils.bindTexture(HANDLES);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.getHandleY(), handleWidth, handleHeight, 0, isActive ? 0 : 0.5f, 1, 0.5f);

        super.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled) {
        isDragging = mouseX >= this.x && mouseX <= this.x + width && mouseY >= this.y && mouseY <= this.y + this.height;

        return super.mousePressed(mouseX, mouseY, button, hasBeenHandled);
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button, boolean hasBeenHandled) {
        isDragging = false;

        return super.mouseReleased(mouseX, mouseY, button, hasBeenHandled);
    }
}
