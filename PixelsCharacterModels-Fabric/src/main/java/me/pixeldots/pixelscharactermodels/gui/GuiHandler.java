package me.pixeldots.pixelscharactermodels.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class GuiHandler extends Screen {
	
	public List<TextFieldWidget> TextFieldWidgets = Lists.newArrayList();
	public List<ButtonWidget> buttons = Lists.newArrayList();
	public TextRenderer textRendererGUI;
	
	public GuiHandler(String title) {
		super(Text.of(title));
		textRendererGUI = PixelsCharacterModels.minecraft.textRenderer;
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void init() {
		buttons.clear();
		TextFieldWidgets.clear();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		for (ButtonWidget widget : buttons) {
			widget.render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		for (ButtonWidget widget : buttons) {
			widget.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (TextFieldWidget widget : TextFieldWidgets) {
			widget.keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
	
	@Override
	public void tick() {
	}
	
	public TextFieldWidget addTextField(TextFieldWidget TextField) {	
		this.TextFieldWidgets.add(TextField);
		return TextField;
	}
	
	public void drawString(MatrixStack matrices, String text, int x, int y, int color) {
		drawCenteredText(matrices, textRendererGUI, text, x+textRendererGUI.getWidth(text)/2, y+5, color);
	}
	
	public void drawString(MatrixStack matrices, String text, int x, int y) {
		drawCenteredText(matrices, textRendererGUI, text, x+textRendererGUI.getWidth(text)/2, y+5, 16777215);
	}
	
	public ButtonWidget addButton(ButtonWidget button) {
		buttons.add(button);
		return button;
	}
	
	public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
		float f = (float)Math.atan(mouseX / 40.0f);
        float g = (float)Math.atan(mouseY / 40.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 1050.0);
        matrixStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale(size, size, size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0f);
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = PixelsCharacterModels.minecraft.getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = PixelsCharacterModels.minecraft.getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
	}

	public void drawColor(MatrixStack matrices, int x, int y, int width, int height, int r, int g, int b, int a) {
		int x0 = x, x1 = x + width;
		int y0 = y, y1 = y + height;

		int rgba = r;
		rgba = (rgba << 8) + g;
		rgba = (rgba << 8) + b;
		rgba = (rgba << 8) + a;

		DrawableHelper.fill(matrices, x0, y0, x1, y1, rgba);
    }

}
