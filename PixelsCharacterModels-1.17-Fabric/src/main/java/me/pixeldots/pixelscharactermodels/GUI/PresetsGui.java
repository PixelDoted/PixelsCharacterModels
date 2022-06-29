package me.pixeldots.pixelscharactermodels.GUI;

import java.io.File;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.Animation.AnimationGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.FramesGui;
import me.pixeldots.pixelscharactermodels.GUI.Editor.EditorGui;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class PresetsGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget Options;
	
	public TextFieldWidget CreatePresetName;
	public ButtonWidget CreatePreset;
	public ButtonWidget RenamePreset;
	public ButtonWidget DeletePreset;
	
	public String path = "";
	
	public PresetsGui() { this(true); }
	public PresetsGui(boolean checkForUpdates) {
		super("Presets");
	}

	public PresetsGui(String _path) { this(_path, true); }
	public PresetsGui(String _path, boolean checkForUpdates) {
		this(checkForUpdates);
		this.path = _path;
	}
	
	@Override
	public void init() {
		super.init();
		
		File[] presets = PixelsCharacterModels.PresetsData.getPresets(path);
		Presets = addButton(new ButtonWidget(5,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Presets), (button) -> {
			PixelsCharacterModels.client.openScreen(new PresetsGui(false));
		}));
		Editor = addButton(new ButtonWidget(60,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Editor), (button) -> {
			PixelsCharacterModels.client.openScreen(new EditorGui());
		}));
		Animation = addButton(new ButtonWidget(5,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Animation), (button) -> {
			PixelsCharacterModels.client.openScreen(new AnimationGui());
		}));
		Frames = addButton(new ButtonWidget(60,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Frames), (button) -> {
			PixelsCharacterModels.client.openScreen(new FramesGui());
		}));
		Presets.active = false;
		
		Options = addButton(new ButtonWidget(5,55,50,20, Text.of(PixelsCharacterModels.TranslatedText.Options), (button) -> {
			PixelsCharacterModels.client.openScreen(new OptionsGui());
		}));
		
		CreatePresetName = addTextField(new TextFieldWidget(textRendererGUI, 5, 90, 50, 20, Text.of("Preset Name")));
		CreatePreset = addButton(new ButtonWidget(5, 115, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
			createPreset(CreatePresetName.getText());
		}));
		RenamePreset = addButton(new ButtonWidget(5, 140, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Rename), (button) -> {
			renamePreset(CreatePresetName.getText());
		}));
		DeletePreset = addButton(new ButtonWidget(5, 165, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Remove), (button) -> {
			deletePreset();
		}));
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < presets.length; i++) { //6 per col, max 24
			int num = i;

			String fileName = presets[i].getName();
			ButtonWidget b = addButton( new ButtonWidget(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, Text.of(presets[i].getName().replace(".json", "")), (value) -> {
				if (presets[num].isDirectory()) {
					PixelsCharacterModels.client.openScreen(new PresetsGui(fileName, false));
					return;
				}

				SelectPreset(fileName, value.getMessage().asString());
			}));
			if (PixelsCharacterModels.GuiData.SelectedPresetPath.endsWith(".json")) if (fileName.equals(PixelsCharacterModels.GuiData.SelectedPresetPath)) b.active = false;
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
		}
	}
	
	public void SelectPreset(String path, String name) {
		PixelsCharacterModels.client.LoadPreset(path, client.player, PixelsCharacterModels.PlayerDataList.get(client.player.getUuid()).model);
		PixelsCharacterModels.GuiData.SelectedPresetPath = path;
		PixelsCharacterModels.GuiData.SelectedPresetName = name;
		client.openScreen(new PresetsGui(false));
	}
	
	public void createPreset(String s) {
		PixelsCharacterModels.client.writePreset(path+File.separator+s, client.player, PixelsCharacterModels.PlayerDataList.get(client.player.getUuid()).model);
		client.openScreen(new PresetsGui(false));
	}
	
	public void renamePreset(String s) {
		if (PixelsCharacterModels.GuiData.SelectedPresetPath.endsWith(".json") && !s.replace(" ", "").equalsIgnoreCase(""))
			PixelsCharacterModels.client.RenamePreset(PixelsCharacterModels.GuiData.SelectedPresetPath, s);
		client.openScreen(new PresetsGui(false));
	}

	public void deletePreset() {
		if (PixelsCharacterModels.GuiData.SelectedPresetPath.endsWith(".json"))
			PixelsCharacterModels.client.DeletePreset(PixelsCharacterModels.GuiData.SelectedPresetPath);
		client.openScreen(new PresetsGui(false));
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public boolean isNumeric(String s) {
		return NumberUtils.isCreatable(s);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		drawString(matrices, "Pixel's Character Models - Fabric, Version: " + PixelsCharacterModels.modVersion, 5, this.height-20);
		drawEntity(50, this.height/2+150, 75, (float)(50) - mouseX, (float)(this.height/2+150-125) - mouseY, PixelsCharacterModels.GuiData.entity);
		if (PixelsCharacterModels.GuiData.model == null) 
			PixelsCharacterModels.GuiData.model = PixelsCharacterModels.PlayerDataList.get(PixelsCharacterModels.GuiData.entity.getUuid()).model;
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
