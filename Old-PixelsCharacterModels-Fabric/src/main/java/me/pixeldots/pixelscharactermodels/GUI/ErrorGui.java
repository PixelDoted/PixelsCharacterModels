package me.pixeldots.pixelscharactermodels.GUI;

import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.util.math.MatrixStack;

public class ErrorGui extends GuiHandler {
	
	public String errorMessage = "Unknown error";
	
	public ErrorGui(String errorMessage) {
		super("Error");
		this.errorMessage = errorMessage;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		String error = "Pixel's Character Models incountered an error";
		drawString(matrices, error, 5, 5);
		drawString(matrices, errorMessage, 5, 13);
		super.render(matrices, mouseX, mouseY, delta);
	}

}
