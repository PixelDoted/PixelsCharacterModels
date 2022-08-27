package me.pixeldots.pixelscharactermodels.gui.handlers;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import me.pixeldots.pixelscharactermodels.PCMClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

public class GuiHandler extends Screen {
	
	public List<Widget> gui_drawables = new ArrayList<>();
	public List<GuiEventListener> gui_elements = new ArrayList<>();
	public Font textRenderer;
	
	public GuiHandler(String title) {
		super(Component.literal(title));
		textRenderer = PCMClient.minecraft.font;
		gui_drawables.clear();
		gui_elements.clear();
	}
	
	@Override
	public void init() {
		gui_drawables.clear();
		gui_elements.clear();
	}
	
	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		for (Widget drawable : gui_drawables) {
			drawable.render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (GuiEventListener element : gui_elements) {
			element.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		for (GuiEventListener element : gui_elements) {
			element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (GuiEventListener element : gui_elements) {
			element.mouseScrolled(mouseX, mouseY, amount);
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (GuiEventListener element : gui_elements) {
			element.mouseReleased(mouseX, mouseY, button);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (GuiEventListener element : gui_elements) {
			element.charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (GuiEventListener element : gui_elements) {
			if (element instanceof AbstractButton) continue;
			element.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (GuiEventListener element : gui_elements) {
			element.keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	public EditBox addTextField(EditBox TextField) {
		addDrawableElement(TextField);
		return TextField;
	}

	public Button addButton(Button button) {
		addDrawableElement(button);
		return button;
	}

	public <T extends GuiEventListener & Widget> T addDrawableElement(T element) {
		this.gui_drawables.add(element);
		this.gui_elements.add(element);
		return element;
	}

	public void drawString(PoseStack matrices, String text, int x, int y, int color) {
		drawCenteredString(matrices, textRenderer, text, x+textRenderer.width(text)/2, y+5, color);
	}
	
	public void drawString(PoseStack matrices, String text, int x, int y) {
		drawCenteredString(matrices, textRenderer, text, x+textRenderer.width(text)/2, y+5, 16777215);
	}

	public void drawString(PoseStack matrices, Component text, int x, int y) {
		drawCenteredString(matrices, textRenderer, text, x+textRenderer.width(text)/2, y+5, 16777215);
	}

	public static String String(String text) {
		return I18n.exists(text) ? I18n.get(text, "") : text;
	}
	public static Component Text(String text) {
		return Component.literal(String(text));
	}
	public static List<Component> TextArray(String... text) {
		List<Component> list = new ArrayList<>();
		for (int i = 0; i < text.length; i++) {
			list.add(Text(text[i]));
		}
		return list;
	}

	public void drawVerticalLine(PoseStack matrices, int x, int y0, int y1, int r, int g, int b, int a) {
		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		vLine(matrices, x, y0, y1, argb);
	}

	public void drawHorizontalLine(PoseStack matrices, int x0, int x1, int y, int r, int g, int b, int a) {
		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		hLine(matrices, x0, x1, y, argb);
	}

	public static void drawColor(PoseStack matrices, int x, int y, int width, int height, int r, int g, int b, int a) {
		int x0 = x, x1 = x + width;
		int y0 = y, y1 = y + height;

		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		fill(matrices, x0, y0, x1, y1, argb);
    }

	public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, Vector3f rotation, LivingEntity entity, boolean block) {
		/*float f = (float)Math.atan((double)(mouseX / 40.0F));
		float f1 = (float)Math.atan((double)(mouseY / 40.0F));*/
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate((double)x, (double)y, 1050.0D);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack matrixStack2 = new PoseStack();
		matrixStack2.translate(0.0D, 0.0D, 1000.0D);
		matrixStack2.scale((float)size, (float)size, (float)size);
		Vector3f quaternion_rotation = Vector3f.ZP.rotationDegrees(180.0F).toXYZ();
		quaternion_rotation.add(rotation);
		Quaternion quaternion = Quaternion.fromXYZ(quaternion_rotation);//Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Quaternion.ONE;//Vector3f.XP.rotationDegrees(f1 * 20.0F);
		quaternion.mul(quaternion1);
		matrixStack2.mulPose(quaternion);
		float f2 = entity.yBodyRot;
		float f3 = entity.getYRot();
		float f4 = entity.getXRot();
		float f5 = entity.yHeadRotO;
		float f6 = entity.yHeadRot;
		entity.yBodyRot = 180.0F; //entity.yBodyRot = 180.0F + f * 20.0F;
		entity.setYRot(180.0F); //entity.setYRot(180.0F + f * 40.0F);
		entity.setXRot(0); //entity.setXRot(-f1 * 20.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();
		Lighting.setupForEntityInInventory();
		BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion1.conj();
		entityRenderDispatcher.overrideCameraOrientation(quaternion1);
		entityRenderDispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
			if (block) {
				matrixStack2.translate(-.5, -1, -.5);
				blockRenderManager.renderSingleBlock(Blocks.DARK_OAK_PLANKS.defaultBlockState(), matrixStack2, multibuffersource$buffersource, 0xF000F0, OverlayTexture.NO_OVERLAY);
				matrixStack2.translate(.5, 1, .5);
			}

			entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, multibuffersource$buffersource, 0xF000F0);
		});
        multibuffersource$buffersource.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}
	
}
