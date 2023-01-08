package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget.PressAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class NoBackButtonWidget extends DrawableWidget {

    public Text message;
    public PressAction onPress;

    public NoBackButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height);
        this.message = message;
        this.onPress = onPress;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        if (!this.visible) return;
        renderButton(matrixStack, mouseX, mouseY, delta);
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderModifyedButton(matrices, mouseX, mouseY, delta);
        /*if (this.isHovered()) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }*/
    }

    public void renderModifyedButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;

        if (!this.active) {
            int argb = 255;
            argb = (argb << 8) + 0;
            argb = (argb << 8) + 4;
            argb = (argb << 8) + 17;
            DrawableHelper.fill(matrices, x, y, x+width, y+height, argb);
        } else if (this.isMouseOver(mouseX, mouseY)) {
            int argb = 255;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            argb = (argb << 8) + 122;
            DrawableHelper.fill(matrices, x, y, x+width, y+height, argb);
        }
        
        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        DrawableHelper.drawCenteredText(matrices, textRenderer, this.message, x + width / 2, y + (height - 8) / 2, j | 255 << 24);
    }

    @Override
    public void onClick() {
        this.onPress.onPress(this);
    }
    
}
