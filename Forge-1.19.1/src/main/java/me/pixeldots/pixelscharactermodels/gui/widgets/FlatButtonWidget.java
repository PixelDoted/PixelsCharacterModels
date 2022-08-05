package me.pixeldots.pixelscharactermodels.gui.widgets;

import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;

import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class FlatButtonWidget extends Button {

    private List<Component> m_tooltip = null;
    private GuiHandler m_gui = null;

    public FlatButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    public FlatButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, GuiHandler gui, List<Component> tooltip) {
        super(x, y, width, height, message, onPress);
        this.m_tooltip = tooltip;
        this.m_gui = gui;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (m_gui != null && m_tooltip != null && mouseX >= x && mouseY >= y && mouseX < x+width && mouseY < y+height)
            m_gui.renderTooltip(matrices, m_tooltip, Optional.empty(), mouseX, mouseY);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        renderModifyedButton(matrices, mouseX, mouseY, delta, minecraftClient);

        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        AbstractButton.drawCenteredString(matrices, minecraftClient.font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | (int)Math.ceil(this.alpha * 255.0f) << 24);
        
        if (this.isHovered) this.renderToolTip(matrices, mouseX, mouseY);
    }

    public void renderModifyedButton(PoseStack matrices, int mouseX, int mouseY, float delta, Minecraft minecraftClient) {
        int argb = 255;
        if (this.isHovered) { 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132;
        } else { 
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
        }
        fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, 0xFF000000);
        fill(matrices, this.x+1, this.y+1, this.x+this.width-1, this.y+this.height-1, argb);
    }
    
}
