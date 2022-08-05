package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class NoBackButtonWidget extends ButtonWidget {

    public NoBackButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderModifyedButton(matrices, mouseX, mouseY, delta);
        if (this.isHovered()) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    public void renderModifyedButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;

        if (!this.active) {
            int argb = 255;
            argb = (argb << 8) + 0;
            argb = (argb << 8) + 4;
            argb = (argb << 8) + 17;
            DrawableHelper.fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, argb);
        } else if (this.isHovered()) {
            int argb = 255;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            DrawableHelper.fill(matrices, this.x, this.y, this.x+this.width, this.y+this.height, argb);
        }
        
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        DrawableHelper.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }
    
}
