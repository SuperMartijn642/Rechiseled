package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.BaseWidget;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.rechiseled.Rechiseled;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingBlockShape;
import com.supermartijn642.rechiseled.api.chiseling.ChiselingEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Created 2/3/2021 by SuperMartijn642
 */
public class EntryButtonWidget extends BaseWidget {

    public static final ResourceLocation TEXTURE = Rechiseled.identifier("screen/buttons");

    private final Supplier<ChiselingEntry> entry;
    private final Supplier<ChiselingEntry> selectedEntry;
    private final Runnable onClick;
    private final Supplier<Boolean> connecting;

    public EntryButtonWidget(int x, int y, int width, int height,
                             Supplier<ChiselingEntry> entrySupplier,
                             Supplier<ChiselingEntry> selectedEntrySupplier,
                             Runnable onClick,
                             Supplier<Boolean> connecting){
        super(x, y, width, height);
        this.entry = entrySupplier;
        this.selectedEntry = selectedEntrySupplier;
        this.onClick = onClick;
        this.connecting = connecting;
    }

    @Override
    public Component getNarrationMessage(){
        ChiselingEntry entry = this.entry.get();
        if(entry == null)
            return null;
        Item item = (this.connecting.get() && entry.hasConnectingItem(ChiselingBlockShape.BLOCK)) || !entry.hasRegularItem(ChiselingBlockShape.BLOCK) ? entry.getConnectingItem(ChiselingBlockShape.BLOCK) : entry.getRegularItem(ChiselingBlockShape.BLOCK);
        return TextComponents.translation("rechiseled.chiseling.select_block", TextComponents.item(item).get()).get();
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        ChiselingEntry entry = this.entry.get();

        boolean hasEntry = entry != null;
        boolean selected = hasEntry && this.selectedEntry.get() == entry;
        boolean hasCorrectItem = hasEntry && (this.connecting.get() ? entry.hasConnectingItem(ChiselingBlockShape.BLOCK) : entry.hasRegularItem(ChiselingBlockShape.BLOCK));

        graphics.submitSprite(TEXTURE, this.x, this.y, this.width, this.height, p -> p.uv(0, (selected ? 1 : hasEntry ? hasCorrectItem ? this.isFocused() ? 2 : 0 : this.isFocused() ? 4 : 3 : 0) / 5f, 1, 1 / 5f));
    }

    @Override
    public void renderForeground(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        ChiselingEntry entry = this.entry.get();
        if(entry != null){
            Item item = (this.connecting.get() && entry.hasConnectingItem(ChiselingBlockShape.BLOCK)) || !entry.hasRegularItem(ChiselingBlockShape.BLOCK) ? entry.getConnectingItem(ChiselingBlockShape.BLOCK) : entry.getRegularItem(ChiselingBlockShape.BLOCK);
            graphics.submitCustomRendering(
                this.x, this.y, this.width, this.height,
                (poseStack, bufferSource) -> ScreenItemRender.drawItem(poseStack, bufferSource, item, this.width / 2d, this.height / 2d, this.width - 4, 0, 0, false)
            );
        }
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button, boolean hasBeenHandled){
        if(!hasBeenHandled && mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height){
            ChiselingEntry entry = this.entry.get();
            if(entry != null)
                this.onClick.run();
            return true;
        }
        return super.mousePressed(mouseX, mouseY, button, hasBeenHandled);
    }
}
