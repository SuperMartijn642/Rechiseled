package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.core.gui.widget.BaseWidget;
import com.supermartijn642.rechiseled.chiseling.ChiselingEntry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

/**
 * Created 2/3/2021 by SuperMartijn642
 */
public class EntryButtonWidget extends BaseWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rechiseled", "textures/screen/buttons.png");

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
    public ITextComponent getNarrationMessage(){
        ChiselingEntry entry = this.entry.get();
        if(entry == null)
            return null;
        Item item = (this.connecting.get() && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
        return TextComponents.translation("rechiseled.chiseling.select_block", TextComponents.item(item).get()).get();
    }

    @Override
    public void render(int mouseX, int mouseY){
        ChiselingEntry entry = this.entry.get();

        boolean hasEntry = entry != null;
        boolean selected = hasEntry && this.selectedEntry.get() == entry;
        boolean hasCorrectItem = hasEntry && (this.connecting.get() ? entry.hasConnectingItem() : entry.hasRegularItem());

        ScreenUtils.bindTexture(TEXTURE);
        ScreenUtils.drawTexture(this.x, this.y, this.width, this.height, 0, (selected ? 1 : hasEntry ? hasCorrectItem ? this.isFocused() ? 2 : 0 : this.isFocused() ? 4 : 3 : 0) / 5f, 1, 1 / 5f);

        if(hasEntry){
            Item item = (this.connecting.get() && entry.hasConnectingItem()) || !entry.hasRegularItem() ? entry.getConnectingItem() : entry.getRegularItem();
            if(item instanceof BlockItem){
                BlockCapture capture = new BlockCapture(((BlockItem)item).getBlock());
                ScreenBlockRenderer.drawBlock(capture, this.x + this.width / 2d, this.y + this.height / 2d, this.width, 135, 40, true);
            }else
                ScreenItemRender.drawItem(item, this.x + this.width / 2d, this.y + this.height / 2d, (this.width - 4) * 1.416, 0, 0, false);
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
