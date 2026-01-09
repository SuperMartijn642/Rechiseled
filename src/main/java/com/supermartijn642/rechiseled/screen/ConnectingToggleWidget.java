package com.supermartijn642.rechiseled.screen;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.GuiGraphicsHelper;
import com.supermartijn642.core.gui.widget.WidgetRenderContext;
import com.supermartijn642.core.gui.widget.premade.AbstractButtonWidget;
import com.supermartijn642.core.util.Holder;
import com.supermartijn642.rechiseled.Rechiseled;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 26/12/2021 by SuperMartijn642
 */
public class ConnectingToggleWidget extends AbstractButtonWidget {

    public static final Identifier SMALL_GREY_BUTTONS = Rechiseled.identifier("screen/grey_buttons");
    public static final Identifier ICON_CONNECTED_ON = Rechiseled.identifier("screen/icon_connecting_true");
    public static final Identifier ICON_CONNECTED_OFF = Rechiseled.identifier("screen/icon_connecting_false");

    private final Supplier<Boolean> connecting;
    private final Supplier<DisplayEntry> currentEntry;

    public ConnectingToggleWidget(int x, int y, int width, int height, Supplier<Boolean> connecting, Supplier<DisplayEntry> currentEntry, Runnable onPress){
        super(x, y, width, height, onPress);
        this.connecting = connecting;
        this.currentEntry = currentEntry;
    }

    @Override
    protected boolean isClickable(){
        DisplayEntry display = this.currentEntry.get();
        return display != null && display.hasItem(!this.connecting.get());
    }

    @Override
    public Component getNarrationMessage(){
        Holder<Component> message = new Holder<>();
        this.getTooltips(message::set);
        return message.get();
    }

    @Override
    protected void getTooltips(Consumer<Component> tooltips){
        if(this.isClickable())
            tooltips.accept(TextComponents.translation("rechiseled.chiseling.connecting", TextComponents.translation("rechiseled.chiseling.connecting." + (this.connecting.get() ? "on" : "off")).color(ChatFormatting.GOLD).get()).get());
    }

    @Override
    public void renderBackground(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        boolean canSwitch = this.isClickable();
        graphics.submitSprite(SMALL_GREY_BUTTONS, this.x, this.y, this.width, this.height, p -> p.uv(0, (canSwitch ? this.isFocused() ? 2 : 0 : 1) / 3f, 1, 1 / 3f));
    }

    @Override
    public void render(WidgetRenderContext context, GuiGraphicsHelper graphics, int mouseX, int mouseY){
        graphics.submitSprite(this.connecting.get() ? ICON_CONNECTED_ON : ICON_CONNECTED_OFF, this.x + 1, this.y + 2, this.width - 2, this.height - 4);
    }
}
