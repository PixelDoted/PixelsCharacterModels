package me.PixelDots.PixelsCharacterModels.client.gui.Handlers;

import com.mojang.blaze3d.matrix.MatrixStack;

public class ErrorGui extends HandlerGui
{
	
	public String ErrorText = "No error found";
	public String ErrorCode = "";

	public ErrorGui() {
		super(0, 0, "Error", "textures/gui/presets.png");
	}
	
	public ErrorGui(String text, String code) {
		super(0, 0, "Error", "textures/gui/presets.png");
		this.ErrorText = text;
		this.ErrorCode = code;
	}
	
	@Override
	public void drawText(MatrixStack stack, int clr) {
		drawString(stack, fontRenderer, this.ErrorText
				, this.field_230708_k_/2-fontRenderer.getStringWidth(this.ErrorText)
				, this.field_230709_l_/2-15, clr);
		drawString(stack, fontRenderer, "ErrorCode: " + this.ErrorCode
				, this.field_230708_k_/2-fontRenderer.getStringWidth("ErrorCode: " + this.ErrorCode)
				, this.field_230709_l_/2+15, clr);
		super.drawText(stack, clr);
	}

}
