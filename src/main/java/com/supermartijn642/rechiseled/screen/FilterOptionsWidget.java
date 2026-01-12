package com.supermartijn642.rechiseled.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.CursorType;
import com.supermartijn642.core.gui.CursorTypes;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BaseWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Created 08/01/2026 by SuperMartijn642
 */
public class FilterOptionsWidget extends BaseWidget {

    private static final ResourceLocation BUTTONS = Rechiseled.identifier("textures/screen/filter_buttons.png");
    private static final ResourceLocation MARKER = Rechiseled.identifier("textures/screen/filter_marker.png");
    private static final ResourceLocation CHECKMARK = Rechiseled.identifier("textures/screen/checkmark.png");
    private static final int PADDING = 20;

    private final BooleanSupplier showBlocks, showStairs, showSlabs, showNonConnecting;
    private final Runnable toggleShowBlocks, toggleShowStairs, toggleShowSlabs, toggleShowConnecting;
    private final Runnable resetFilters;
    private final int collapsedWidth, collapsedHeight;
    private final Dropdown dropdown;
    private boolean active = true;

    public FilterOptionsWidget(int x, int y, BooleanSupplier showBlocks, BooleanSupplier showStairs, BooleanSupplier showSlabs, BooleanSupplier showNonConnecting, Runnable toggleShowBlocks, Runnable toggleShowStairs, Runnable toggleShowSlabs, Runnable toggleShowConnecting, Runnable resetFilters){
        super(x, y, 12, 12);
        this.showBlocks = showBlocks;
        this.showStairs = showStairs;
        this.showSlabs = showSlabs;
        this.showNonConnecting = showNonConnecting;
        this.toggleShowBlocks = toggleShowBlocks;
        this.toggleShowStairs = toggleShowStairs;
        this.toggleShowSlabs = toggleShowSlabs;
        this.toggleShowConnecting = toggleShowConnecting;
        this.resetFilters = resetFilters;
        this.collapsedWidth = this.width;
        this.collapsedHeight = this.height;
        this.dropdown = this.addWidget(new Dropdown(this.x, this.y + 11));
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled){
        if(!this.active)
            return hasBeenHandled;
        boolean canClickDropdown = this.dropdown.expanded && !hasBeenHandled;
        hasBeenHandled |= super.mousePressed(mouseX, mouseY, button, hasBeenHandled);
        if(canClickDropdown && hasBeenHandled)
            return true;
        if(!hasBeenHandled && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
            if(button == 0)
                this.setExpanded(!this.dropdown.expanded);
            else if(button == 1)
                this.resetFilters.run();
            hasBeenHandled = true;
            AbstractButtonWidget.playClickSound();
        }else
            this.setExpanded(false);
        return hasBeenHandled;
    }

    private void setExpanded(boolean expanded){
        this.dropdown.setExpanded(expanded);
        this.width = Math.max(this.collapsedWidth, this.dropdown.width());
        this.height = this.collapsedHeight + this.dropdown.height();
    }

    public void setActive(boolean active){
        this.active = active;
        if(!active)
            this.setExpanded(false);
    }

    @Override
    public CursorType curser(int mouseX, int mouseY){
        if(this.active && mouseX >= this.x && mouseX < this.x + this.collapsedWidth && mouseY >= this.y && mouseY < this.y + this.collapsedHeight)
            return CursorTypes.pointingHand();
        return this.dropdown.curser(mouseX, mouseY);
    }

    @Override
    public Component getNarrationMessage(){
        return TextComponents.translation("rechiseled.chiseling.filter").get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        tooltips.accept(TextComponents.translation("rechiseled.chiseling.filter").get());
        tooltips.accept(TextComponents.translation("rechiseled.chiseling.filter.clear").color(ChatFormatting.GRAY).italic().get());
    }

    @Override
    public void renderTooltips(WidgetRenderContext context, int mouseX, int mouseY){
        if(this.active && this.isFocused() && mouseX >= this.x && mouseX < this.x + this.collapsedWidth && mouseY >= this.y && mouseY < this.y + this.collapsedHeight)
            super.renderTooltips(context, mouseX, mouseY);
    }

    @Override
    public void renderBackground(WidgetRenderContext context, int mouseX, int mouseY){
        if(this.dropdown.expanded && (mouseX < this.x - PADDING || mouseX > this.x + this.width + PADDING || mouseY < this.y - PADDING || mouseY > this.y + this.height + PADDING))
            this.setExpanded(false);

        ScreenUtils.bindTexture(BUTTONS);
        ScreenUtils.drawTexture(context.poseStack(), this.x, this.y, this.collapsedWidth, this.collapsedHeight, 0, (this.active ? this.isFocused() || this.dropdown.expanded ? 1 : 0 : 2) / 3f, 1, 1 / 3f);
    }

    @Override
    public void render(WidgetRenderContext context, int mouseX, int mouseY){
        if(!this.showBlocks.getAsBoolean() || !this.showStairs.getAsBoolean() || !this.showSlabs.getAsBoolean() || !this.showNonConnecting.getAsBoolean()){
            ScreenUtils.bindTexture(MARKER);
            ScreenUtils.drawTexture(context.poseStack(), this.x + this.collapsedWidth - 4, this.y, 4, 4);
        }
    }

    private class Dropdown extends BaseWidget {

        private final Component showBlocksText = TextComponents.translation("rechiseled.chiseling.filter.show_blocks").get();
        private final Component showStairsText = TextComponents.translation("rechiseled.chiseling.filter.show_stairs").get();
        private final Component showSlabsText = TextComponents.translation("rechiseled.chiseling.filter.show_slabs").get();
        private final Component showNonConnectingText = TextComponents.translation("rechiseled.chiseling.filter.show_non_connecting").get();
        private final int expandedWidth, expandedHeight;
        private boolean expanded;

        public Dropdown(int x, int y){
            super(x, y, 0, 0);
            int width = Minecraft.getInstance().font.width(this.showBlocksText);
            width = Math.max(width, Minecraft.getInstance().font.width(this.showStairsText));
            width = Math.max(width, Minecraft.getInstance().font.width(this.showSlabsText));
            width = Math.max(width, Minecraft.getInstance().font.width(this.showNonConnectingText));
            this.expandedWidth = width + 16;
            this.expandedHeight = 4 * 11 + 2;
        }

        void setExpanded(boolean expanded){
            this.expanded = expanded;
            if(expanded){
                this.width = this.expandedWidth;
                this.height = this.expandedHeight;
            }else
                this.width = this.height = 0;
        }

        @Override
        public Component getNarrationMessage(){
            return null;
        }

        @Override
        public CursorType curser(int mouseX, int mouseY){
            return this.expanded && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height ? CursorTypes.pointingHand() : null;
        }

        @Override
        public void renderForeground(WidgetRenderContext context, int mouseX, int mouseY){
            if(!this.expanded)
                return;
            PoseStack poseStack = context.poseStack();
            poseStack.pushPose();
            poseStack.translate(0, 0, 500);
            ScreenUtils.fillRect(poseStack, this.x, this.y, this.width, this.height, 139 / 255f, 139 / 255f, 139 / 255f, 1);
            int hoveredLine;
            if(this.expanded && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
                hoveredLine = Math.max((int)Math.floor((float)(mouseY - this.y) / this.height * 4), 0);
                ScreenUtils.fillRect(poseStack, this.x, this.y + 1 + 11 * hoveredLine, this.width, 11, 150 / 255f, 150 / 255f, 150 / 255f, 1);
            }else
                hoveredLine = -1;
            this.renderLine(poseStack, 0, hoveredLine, FilterOptionsWidget.this.showBlocks, this.showBlocksText);
            this.renderLine(poseStack, 1, hoveredLine, FilterOptionsWidget.this.showStairs, this.showStairsText);
            this.renderLine(poseStack, 2, hoveredLine, FilterOptionsWidget.this.showSlabs, this.showSlabsText);
            this.renderLine(poseStack, 3, hoveredLine, FilterOptionsWidget.this.showNonConnecting, this.showNonConnectingText);
            poseStack.popPose();
        }

        private void renderLine(PoseStack poseStack, int line, int hoveredLine, BooleanSupplier isEnabled, Component text){
            if(isEnabled.getAsBoolean()){
                ScreenUtils.bindTexture(CHECKMARK);
                ScreenUtils.drawTexture(poseStack, this.x + 3, this.y + 2 + line * 11, 8, 8);
            }
            ScreenUtils.drawString(
                poseStack,
                hoveredLine == line ? TextComponents.fromTextComponent(text).italic().get() : text,
                isEnabled.getAsBoolean() ? this.x + 12 : this.x + 3, this.y + 3 + line * 11
            );
        }

        @Override
        public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled){
            if(this.expanded && !hasBeenHandled && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
                int line = Math.max((int)Math.floor((float)(mouseY - this.y) / this.height * 4), 0);
                if(line == 0)
                    FilterOptionsWidget.this.toggleShowBlocks.run();
                else if(line == 1)
                    FilterOptionsWidget.this.toggleShowStairs.run();
                else if(line == 2)
                    FilterOptionsWidget.this.toggleShowSlabs.run();
                else if(line == 3)
                    FilterOptionsWidget.this.toggleShowConnecting.run();
                hasBeenHandled = true;
            }
            return hasBeenHandled;
        }
    }
}
