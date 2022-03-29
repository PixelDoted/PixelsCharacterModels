package me.pixeldots.pixelscharactermodels.GUI.Handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
		textRendererGUI = PixelsCharacterModels.client.minecraft.textRenderer;
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
		for (int i = 0; i < TextFieldWidgets.size(); i++) {
			TextFieldWidgets.get(i).render(matrices, mouseX, mouseY, delta);
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (int i = 0; i < TextFieldWidgets.size(); i++) {
			TextFieldWidgets.get(i).mouseClicked(mouseX, mouseY, button);
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (int i = 0; i < TextFieldWidgets.size(); i++) {
			TextFieldWidgets.get(i).charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (int i = 0; i < TextFieldWidgets.size(); i++) {
			TextFieldWidgets.get(i).keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (int i = 0; i < TextFieldWidgets.size(); i++) {
			TextFieldWidgets.get(i).keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean isPauseScreen() {
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
	      float f = (float)Math.atan((double)(mouseX / 40.0F));
	      float g = (float)Math.atan((double)(mouseY / 40.0F));
	      MatrixStack matrixStack = RenderSystem.getModelViewStack();
	      matrixStack.push();
	      matrixStack.translate((double)x, (double)y, 1050.0D);
	      matrixStack.scale(1.0F, 1.0F, -1.0F);
	      RenderSystem.applyModelViewMatrix();
	      MatrixStack matrixStack2 = new MatrixStack();
	      matrixStack2.translate(0.0D, 0.0D, 1000.0D);
	      matrixStack2.scale((float)size, (float)size, (float)size);
	      Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
	      Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
	      quaternion.hamiltonProduct(quaternion2);
	      matrixStack2.multiply(quaternion);
	      float h = entity.bodyYaw;
	      float i = entity.getYaw();
	      float j = entity.getPitch();
	      float k = entity.prevHeadYaw;
	      float l = entity.headYaw;
	      entity.bodyYaw = 180.0F + f * 20.0F;
	      entity.setYaw(180.0F + f * 40.0F);
	      entity.setPitch(-g * 20.0F);
	      entity.headYaw = entity.getYaw();
	      entity.prevHeadYaw = entity.getYaw();
	      DiffuseLighting.method_34742();
	      EntityRenderDispatcher entityRenderDispatcher = PixelsCharacterModels.client.minecraft.getEntityRenderDispatcher();
	      quaternion2.conjugate();
	      entityRenderDispatcher.setRotation(quaternion2);
	      entityRenderDispatcher.setRenderShadows(false);
	      VertexConsumerProvider.Immediate immediate = PixelsCharacterModels.client.minecraft.getBufferBuilders().getEntityVertexConsumers();
	      RenderSystem.runAsFancy(() -> {
	         entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
	      });
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

}
