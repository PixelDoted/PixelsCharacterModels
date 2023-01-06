package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget.PressAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class OffsetFlatButtonWidget extends DrawableWidget {

    public Text message;
    public PressAction onPress;
    
    public OffsetFlatButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
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
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        renderModifyedButton(matrices, mouseX, mouseY, delta, minecraftClient);

        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        ClickableWidget.drawTextWithShadow(matrices, minecraftClient.textRenderer, this.message, this.x + 4, this.y + (this.height - 8) / 2, j | 255 << 24);

        //if (this.isHovered()) this.renderTooltip(matrices, mouseX, mouseY);
    }

    public void renderModifyedButton(MatrixStack matrices, int mouseX, int mouseY, float delta, MinecraftClient minecraftClient) {
        int argb = 255;
        if (this.isMouseOver(mouseX, mouseY)) { 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132; 
            argb = (argb << 8) + 132;
        } else { 
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
            argb = (argb << 8) + 66;
        }
        DrawableHelper.fill(matrices, x, y, x+width, y+height, 0xFF000000);
        DrawableHelper.fill(matrices, x+1, y+1, x+width-1, y+height-1, argb);
    }

    @Override
    public void onClick() {
        this.onPress.onPress(this);
    }
    
}
