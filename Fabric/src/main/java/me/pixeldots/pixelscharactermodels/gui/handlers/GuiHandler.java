package me.pixeldots.pixelscharactermodels.gui.handlers;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.gui.widgets.DrawableWidget;
import net.minecraft.block.Blocks;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GuiHandler extends Screen {
	
	public List<Drawable> gui_drawables = new ArrayList<>();
	public List<Element> gui_elements = new ArrayList<>();
	public TextRenderer textRendererGUI;
	
	public GuiHandler(String title) {
		super(Text.of(title));
		textRendererGUI = PCMClient.minecraft.textRenderer;
		gui_drawables.clear();
		gui_elements.clear();
	}
	
	@Override
	public void init() {
		gui_drawables.clear();
		gui_elements.clear();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (Drawable drawable : gui_drawables) {
			drawable.render(matrices, mouseX, mouseY, delta);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Element element : gui_elements) {
			element.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		for (Element element : gui_elements) {
			element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (Element element : gui_elements) {
			element.mouseScrolled(mouseX, mouseY, amount);
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (Element element : gui_elements) {
			element.mouseReleased(mouseX, mouseY, button);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		for (Element element : gui_elements) {
			element.charTyped(chr, keyCode);
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (Element element : gui_elements) {
			if (element instanceof PressableWidget) continue;
			element.keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (Element element : gui_elements) {
			element.keyReleased(keyCode, scanCode, modifiers);
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
	
	public TextFieldWidget addTextField(TextFieldWidget TextField) {
		addDrawableElement(TextField);
		return TextField;
	}

	public DrawableWidget addButton(DrawableWidget button) {
		addDrawableElement(button);
		return button;
	}

	public <T extends Element & Drawable> T addDrawableElement(T element) {
		this.gui_drawables.add(element);
		this.gui_elements.add(element);
		return element;
	}

	public void drawString(MatrixStack matrices, String text, int x, int y, int color) {
		drawCenteredText(matrices, textRendererGUI, text, x+textRendererGUI.getWidth(text)/2, y+5, color);
	}
	
	public void drawString(MatrixStack matrices, String text, int x, int y) {
		drawCenteredText(matrices, textRendererGUI, text, x+textRendererGUI.getWidth(text)/2, y+5, 16777215);
	}

	public void drawString(MatrixStack matrices, Text text, int x, int y) {
		drawCenteredText(matrices, textRendererGUI, text, x+textRendererGUI.getWidth(text)/2, y+5, 16777215);
	}

	public static String String(String text) {
		return I18n.hasTranslation(text) ? I18n.translate(text, "") : text;
	}
	public static Text Text(String text) {
		return Text.of(String(text));
	}
	public static List<Text> TextArray(String... text) {
		List<Text> list = new ArrayList<>();
		for (int i = 0; i < text.length; i++) {
			list.add(Text(text[i]));
		}
		return list;
	}

	public void drawVerticalLine(MatrixStack matrices, int x, int y0, int y1, int r, int g, int b, int a) {
		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		this.drawVerticalLine(matrices, x, y0, y1, argb);
	}

	public void drawHorizontalLine(MatrixStack matrices, int x0, int x1, int y, int r, int g, int b, int a) {
		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		this.drawHorizontalLine(matrices, x0, x1, y, argb);
	}

	public static void drawColor(MatrixStack matrices, int x, int y, int width, int height, int r, int g, int b, int a) {
		int x0 = x, x1 = x + width;
		int y0 = y, y1 = y + height;

		int argb = a;
		argb = (argb << 8) + r;
		argb = (argb << 8) + g;
		argb = (argb << 8) + b;

		DrawableHelper.fill(matrices, x0, y0, x1, y1, argb);
    }

	public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, Vector3f rotation, LivingEntity entity, boolean block) {
		/*float f = (float)Math.atan(mouseX / 40.0f);
        float g = (float)Math.atan(mouseY / 40.0f);*/
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 1050.0);
        matrixStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale(size, size, size);
        Quaternionf quaternionf = new Quaternionf().rotateXYZ(rotation.x, rotation.y, rotation.z + (float)Math.PI);//.fromEulerXyz(quaternion_rotation);
        Quaternionf quaternionf2 = new Quaternionf();//Vector3f.POSITIVE_X.getDegreesQuaternion(g * 20.0f);
        quaternionf.mul(quaternionf2);
        matrixStack2.multiply(quaternionf);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f; //entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f); //entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(0); //entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
		BlockRenderManager blockRenderManager = PCMClient.minecraft.getBlockRenderManager();
        EntityRenderDispatcher entityRenderDispatcher = PCMClient.minecraft.getEntityRenderDispatcher();
        quaternionf2.conjugate();
        entityRenderDispatcher.setRotation(quaternionf2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = PCMClient.minecraft.getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
			if (block) {
				matrixStack2.translate(-.5, -1, -.5);
				blockRenderManager.renderBlockAsEntity(Blocks.DARK_OAK_PLANKS.getDefaultState(), matrixStack2, immediate, 0xF000F0, OverlayTexture.DEFAULT_UV);
				matrixStack2.translate(.5, 1, .5);
			}

			entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0);
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

	/*public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, boolean block) {
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
        Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0f);
        Quaternion quaternion2 = Vector3f.POSITIVE_X.getDegreesQuaternion(g * 20.0f);
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
		BlockRenderManager blockRenderManager = PCMClient.minecraft.getBlockRenderManager();
        EntityRenderDispatcher entityRenderDispatcher = PCMClient.minecraft.getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = PCMClient.minecraft.getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
			if (block) {
				matrixStack2.translate(-.5, -1, -.5);
				blockRenderManager.renderBlockAsEntity(Blocks.DARK_OAK_PLANKS.getDefaultState(), matrixStack2, immediate, 0xF000F0, OverlayTexture.DEFAULT_UV);
				matrixStack2.translate(.5, 1, .5);
			}

			entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0);
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
	}*/
	
}
