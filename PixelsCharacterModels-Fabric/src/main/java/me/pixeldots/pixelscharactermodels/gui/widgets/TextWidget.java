package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextWidget implements Drawable {

    public TextRenderer textRenderer;
    public Text text;
    public int color;
    public int x, y;

    public TextWidget(TextRenderer text_renderer, int x, int y, Text text, int color) {
        this.textRenderer = text_renderer;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
    }
    public TextWidget(TextRenderer text_renderer, int x, int y, String text, int color) {
        this(text_renderer, x, y, Text.of(text), color);
    }

    @Override
    public void render(MatrixStack matrices, int var2, int var3, float var4) {
        DrawableHelper.drawTextWithShadow(matrices, textRenderer, text, x, y, color);
    }
    
}
