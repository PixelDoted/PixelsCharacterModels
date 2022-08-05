package me.pixeldots.pixelscharactermodels.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;

public class TextWidget implements Widget {

    public Font textRenderer;
    public Component text;
    public int color;
    public int x, y;

    public TextWidget(Font text_renderer, int x, int y, Component text, int color) {
        this.textRenderer = text_renderer;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
    }
    public TextWidget(Font text_renderer, int x, int y, String text, int color) {
        this(text_renderer, x, y, Component.literal(text), color);
    }

    @Override
    public void render(PoseStack matrices, int var2, int var3, float var4) {
        GuiComponent.drawString(matrices, textRenderer, text, x, y, color);
    }
    
}
