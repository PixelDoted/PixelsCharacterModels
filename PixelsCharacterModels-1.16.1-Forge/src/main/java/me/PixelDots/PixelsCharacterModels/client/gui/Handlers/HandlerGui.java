package me.PixelDots.PixelsCharacterModels.client.gui.Handlers;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import me.PixelDots.PixelsCharacterModels.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class HandlerGui extends Screen
{
	public List<TextFieldWidget> TextFields = new ArrayList<TextFieldWidget>();
	public List<Slider> Sliders = new ArrayList<Slider>();
	public final FontRenderer fontRenderer;
	public int guiWidth = 1;
	public int guiHeight = 1;
	
	public ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/presets.png");
	public String title = "";
	
	protected HandlerGui(int width, int height, String title, String texture) {
		super(new StringTextComponent(title));
		this.texture = new ResourceLocation(Reference.MOD_ID, texture);
		this.width = width;
		this.height = height;
		this.title = title;
		this.fontRenderer = Minecraft.getInstance().fontRenderer;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//this.minecraft.getTextureManager().bindTexture(this.texture);
		//this.renderBackground();
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).render(mouseX, mouseY, partialTicks);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).render(mouseX, mouseY, partialTicks);
		}
		LoadString(16777215 | (MathHelper.ceil(1.0 * 255.0F) << 24)); // 16777215 | l
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void init() {
		LoadButtons();
		LoadTextFields();
		LoadSliders();
		super.init();
	}
	
	public void LoadButtons() {
		
	}
	
	public void LoadTextFields() {
		
	}
	
	public void LoadSliders() {
		
	}
	
	public void LoadString(int clr) {
		drawText(clr);
	}
	
	public void drawText(int clr) {
		
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void onClose() {
		UpdateAction();
		super.onClose();
	}
	@Override
	public boolean charTyped(char Key, int KeyCode) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).charTyped(Key, KeyCode);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).charTyped(Key, KeyCode);
		}
		UpdateAction();
		return super.charTyped(Key, KeyCode);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public void UpdateTextFields() {
		
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		}
		UpdateAction();
		UpdateTextFields();
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		UpdateAction();
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).keyReleased(keyCode, scanCode, modifiers);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).keyReleased(keyCode, scanCode, modifiers);
		}
		UpdateAction();
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	public void addTextField(TextFieldWidget tfw) {
		this.TextFields.add(tfw);
	}
	
	public void addSlider(Slider s) {
		this.Sliders.add(s);
	}
	
	//Updates every time an action happened
	public void UpdateAction() {
		
	}
	
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
	      float f = (float)Math.atan((double)(mouseX / 40.0F));
	      float f1 = (float)Math.atan((double)(mouseY / 40.0F));
	      RenderSystem.pushMatrix();
	      RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
	      RenderSystem.scalef(1.0F, 1.0F, -1.0F);
	      MatrixStack matrixstack = new MatrixStack();
	      matrixstack.translate(0.0D, 0.0D, 1000.0D);
	      matrixstack.scale((float)scale, (float)scale, (float)scale);
	      Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
	      Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
	      quaternion.multiply(quaternion1);
	      matrixstack.rotate(quaternion);
	      float f2 = p_228187_5_.renderYawOffset;
	      float f3 = p_228187_5_.rotationYaw;
	      float f4 = p_228187_5_.rotationPitch;
	      float f5 = p_228187_5_.prevRotationYawHead;
	      float f6 = p_228187_5_.rotationYawHead;
	      p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
	      p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
	      p_228187_5_.rotationPitch = -f1 * 20.0F;
	      p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
	      p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
	      EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
	      quaternion1.conjugate();
	      entityrenderermanager.setCameraOrientation(quaternion1);
	      entityrenderermanager.setRenderShadow(false);
	      IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
	      entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
	      irendertypebuffer$impl.finish();
	      entityrenderermanager.setRenderShadow(true);
	      p_228187_5_.renderYawOffset = f2;
	      p_228187_5_.rotationYaw = f3;
	      p_228187_5_.rotationPitch = f4;
	      p_228187_5_.prevRotationYawHead = f5;
	      p_228187_5_.rotationYawHead = f6;
	      RenderSystem.popMatrix();
	   }

}
