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
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class HandlerGui extends Screen
{
	public Minecraft minecraft = Minecraft.getInstance();
	
	public List<TextFieldWidget> TextFields = new ArrayList<TextFieldWidget>();
	public List<Slider> Sliders = new ArrayList<Slider>();
	public List<Button> Buttons = new ArrayList<Button>();
	
	public final FontRenderer fontRenderer;
	public int guiWidth = 1;
	public int guiHeight = 1;
	
	public ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/presets.png");
	public String title = "";
	
	protected HandlerGui(int width, int height, String title, String texture) {
		super(new StringTextComponent(title));
		this.texture = new ResourceLocation(Reference.MOD_ID, texture);
		this.field_230708_k_ = width;
		this.field_230709_l_ = height;
		this.title = title;
		this.fontRenderer = Minecraft.getInstance().fontRenderer;
	}
	
	@Override
	public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//this.minecraft.getTextureManager().bindTexture(this.texture);
		//this.renderBackground();
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
		}
		for (int i = 0; i < Buttons.size(); i++) {
			Buttons.get(i).func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
		}
		LoadString(matrixStack, 16777215 | (MathHelper.ceil(1.0 * 255.0F) << 24)); // 16777215 | l
		render(matrixStack, mouseX, mouseY, partialTicks);
		super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		
	}
	
	public void init() {
		LoadButtons();
		LoadTextFields();
		LoadSliders();
	}
	
	@Override
	protected void func_231160_c_() {
		init();
		super.func_231160_c_();
	}
	
	public void LoadButtons() {
		
	}
	
	public void LoadTextFields() {
		
	}
	
	public void LoadSliders() {
		
	}
	
	public void LoadString(MatrixStack stack, int clr) {
		drawText(stack, clr);
	}
	
	public void drawText(MatrixStack stack, int clr) {
		
	}
	
	@Override
	public boolean func_231177_au__() {
		return false;
	}
	
	public void onClose() {
	}
	
	@Override
	public void func_231164_f_() {
		UpdateAction();
		onClose();
		super.func_231164_f_();
	}
	
	@Override
	public boolean func_231042_a_(char Key, int KeyCode) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).func_231042_a_(Key, KeyCode);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).func_231042_a_(Key, KeyCode);
		}
		UpdateAction();
		return super.func_231042_a_(Key, KeyCode);
	}
	
	@Override
	public void func_231023_e_() {
		super.func_231023_e_();
	}
	
	public void UpdateTextFields() {
		
	}
	
	@Override
	public boolean func_231044_a_(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		}
		for (int i = 0; i < Buttons.size(); i++) {
			Buttons.get(i).func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		}
		UpdateAction();
		UpdateTextFields();
		return super.func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	@Override
	public boolean func_231046_a_(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		for (int i = 0; i < TextFields.size(); i++) {
			TextFields.get(i).func_231046_a_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		for (int i = 0; i < Sliders.size(); i++) {
			Sliders.get(i).func_231046_a_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		}
		UpdateAction();
		return super.func_231046_a_(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
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
	
	public Button addButton(Button b) {
		this.Buttons.add(b);
		return b;
	}
	
	//Updates every time an action happened
	public void UpdateAction() {
		
	}
	
	public void drawString(MatrixStack stack, FontRenderer fontRenderer, String string, int x, int y, int clr) {
		this.func_238471_a_(stack, fontRenderer, string, x+20, y, clr);
	}
	
	public void drawString(MatrixStack stack, FontRenderer fontRenderer, ITextComponent string, int x, int y, int clr) {
		this.func_238471_a_(stack, fontRenderer, string.getString(), x+20, y, clr);
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
