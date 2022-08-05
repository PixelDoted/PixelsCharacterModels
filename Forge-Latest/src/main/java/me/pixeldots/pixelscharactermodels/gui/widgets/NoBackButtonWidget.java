package me.pixeldots.pixelscharactermodels.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class NoBackButtonWidget extends Button {

    public NoBackButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderModifyedButton(matrices, mouseX, mouseY, delta);
        if (this.isHovered) {
            this.renderToolTip(matrices, mouseX, mouseY);
        }
    }

    public void renderModifyedButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Font textRenderer = minecraftClient.font;

        if (!this.active) {
            int argb = 255;
            argb = (argb << 8) + 0;
            argb = (argb << 8) + 4;
            argb = (argb << 8) + 17;
            fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, argb);
        } else if (this.isHovered) {
            int argb = 255;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, argb);
        }
        
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        drawCenteredString(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | (int)Math.ceil(this.alpha * 255.0f) << 24);
    }
    
}
