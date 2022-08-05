package me.pixeldots.pixelscharactermodels.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class OffsetFlatButtonWidget extends Button {

    public OffsetFlatButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        renderModifyedButton(matrices, mouseX, mouseY, delta, minecraftClient);

        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        AbstractButton.drawString(matrices, minecraftClient.font, this.getMessage(), this.x + 4, this.y + (this.height - 8) / 2, j | (int)Math.ceil(this.alpha * 255.0f) << 24);

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
        GuiComponent.fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, 0xFF000000);
        GuiComponent.fill(matrices, this.x+1, this.y+1, this.x+this.width-1, this.y+this.height-1, argb);
    }
    
}
