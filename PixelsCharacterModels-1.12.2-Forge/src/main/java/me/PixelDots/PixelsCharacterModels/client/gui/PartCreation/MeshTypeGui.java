package me.PixelDots.PixelsCharacterModels.client.gui.PartCreation;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.CreatePart;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import net.minecraft.client.Minecraft;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.util.text.StringTextComponent;

public class MeshTypeGui extends HandlerGui
{
	
	Button Presets;
	Button Editor;
	Button Animation;
	
	CreatePart create = null;
	TranslationText Translation = new TranslationText();
	
	public MeshTypeGui() {
		super(0, 0, "Mesh Type", "textures/gui/presets.png");
	}
	
	public MeshTypeGui(CreatePart create) {
		super(0, 0, "Mesh Type", "textures/gui/presets.png");
		this.create = create;
	}
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public void LoadButtons() 
	{
		Presets = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			minecraft.displayGuiScreen(new PresetsGui());
		})); Presets.field_230693_o_ = true;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			minecraft.displayGuiScreen(new EditorGui());
		})); Editor.field_230693_o_ = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); Animation.field_230693_o_ = true;
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < Main.OtherSaveData.Meshes.size() + 1; i++) { //6 per col, max 24
			int num = i - 1;
			String name = i == 0 ? "cube" : Main.OtherSaveData.MeshNames.get(i - 1);
			Button b = addButton(new Button(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, new StringTextComponent(name), (value) -> {
				if (num == -1) {
					Main.GuiSettings.SelectedMesh = "cube";
					Main.GuiSettings.SelectedMeshID = -1;
					Main.GuiSettings.SelectedMeshIsCube = true;
					Minecraft.getInstance().displayGuiScreen(new CreatePartGui(create));
				} else {
					Main.GuiSettings.SelectedMesh = Main.OtherSaveData.MeshNames.get(num);
					Main.GuiSettings.SelectedMeshID = num;
					Main.GuiSettings.SelectedMeshIsCube = false;
					Minecraft.getInstance().displayGuiScreen(new CreatePartGui(create));
				}
				Minecraft.getInstance().displayGuiScreen(new CreatePartGui(create));
			}));
			if (Main.GuiSettings.SelectedMeshID != -1) { if (i - 1 == Main.GuiSettings.SelectedMeshID) { b.field_230693_o_ = false; } }
			if (Main.GuiSettings.SelectedMeshIsCube && i == 0) { b.field_230693_o_ = false; }
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
		}
	}
}
