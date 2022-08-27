package me.pixeldots.pixelscharactermodels.gui.widgets;

import java.util.List;

import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class FlatButtonWidget extends ButtonWidget {

    private List<Text> m_tooltip = null;
    private GuiHandler m_gui = null;

    public FlatButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    public FlatButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, GuiHandler gui, List<Text> tooltip) {
        super(x, y, width, height, message, onPress);
        this.m_tooltip = tooltip;
        this.m_gui = gui;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (m_gui != null && m_tooltip != null && mouseX >= x && mouseY >= y && mouseX < x+width && mouseY < y+height)
            m_gui.renderTooltip(matrices, m_tooltip, mouseX, mouseY);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        renderModifyedButton(matrices, mouseX, mouseY, delta, minecraftClient);

        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        ClickableWidget.drawCenteredText(matrices, minecraftClient.textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);

        if (this.isHovered()) this.renderTooltip(matrices, mouseX, mouseY);
    }

    public void renderModifyedButton(MatrixStack matrices, int mouseX, int mouseY, float delta, MinecraftClient minecraftClient) {
        int argb = 255;
        if (this.isHovered()) { 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132;
        } else { 
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
        }
        DrawableHelper.fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, 0xFF000000);
        DrawableHelper.fill(matrices, this.x+1, this.y+1, this.x+this.width-1, this.y+this.height-1, argb);
    }
    
}
