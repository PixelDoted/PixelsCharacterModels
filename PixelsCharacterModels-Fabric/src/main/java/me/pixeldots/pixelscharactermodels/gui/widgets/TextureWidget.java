package me.pixeldots.pixelscharactermodels.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class TextureWidget implements Drawable {

    public Identifier texture;
    public int textureWidth = 110, textureHeight = 110; // minecraft is weird

    public float selectX, selectY, selectWidth, selectHeight;

    public int x, y;
    public int width, height;

    public TextureWidget(int x, int y, int width, int height, LivingEntity entity) {
        this.texture = PCMUtils.getEntityTexture(entity);
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public void setSelectX(FloatFieldWidget field) {
        selectX = (int)Math.floor(field.getNumber()/64f*textureWidth);
        if (selectX > textureWidth) selectX = textureWidth;
    }
    public void setSelectY(FloatFieldWidget field) {
        selectY = (int)Math.floor(field.getNumber()/64f*textureHeight);
        if (selectY > textureHeight) selectY = textureHeight;
    }
    public void setSelectWidth(FloatFieldWidget field) {
        selectWidth = (int)Math.floor(field.getNumber()/64f*textureWidth);
        if (selectWidth > textureWidth) selectWidth = textureWidth;
    }
    public void setSelectHeight(FloatFieldWidget field) {
        selectHeight = (int)Math.floor(field.getNumber()/64f*textureHeight);
        if (selectHeight > textureHeight) selectHeight = textureHeight;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableDepthTest();
        DrawableHelper.fill(matrices, x, y, x+width, y+height, 0xFF000000);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, textureWidth, textureHeight);
        drawSelection(matrices, mouseX, mouseY, delta);
    }

    public void drawSelection(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawHorizontalLine(matrices, (int)(x+selectX), (int)(x+selectWidth), (int)(y+selectY), 0xFFFFFFFF); //top
        this.drawVerticalLine(matrices, (int)(x+selectX), (int)(y+selectY), (int)(y+selectHeight), 0xFFFFFFFF); //left
        this.drawVerticalLine(matrices, (int)(x+selectWidth), (int)(y+selectY), (int)(y+selectHeight), 0xFFFFFFFF); //right
        this.drawHorizontalLine(matrices, (int)(x+selectX), (int)(x+selectWidth), (int)(y+selectHeight), 0xFFFFFFFF); //bottom
    }

    private void drawVerticalLine(MatrixStack matrices, int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        DrawableHelper.fill(matrices, x, y1 + 1, x + 1, y2, color);
    }
    
    private void drawHorizontalLine(MatrixStack matrices, int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        DrawableHelper.fill(matrices, x1, y, x2 + 1, y + 1, color);
    }

}
