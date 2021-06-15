package me.pixeldots.pixelscharactermodels.GUI;

import java.io.File;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
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
	public ButtonWidget DeletePreset;
	
	public PresetsGui() {
		super("Presets");
	}
	
	@Override
	public void init() {
		super.init();
		if (PixelsCharacterModels.GuiData.model == null) MinecraftClient.getInstance().openScreen(new ErrorGui("The player model wasn't found, this could be caused because the player is not being rendered"));
			
		File[] presets = PixelsCharacterModels.PresetsData.getPresets();
		Presets = addButton(new ButtonWidget(5,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Presets), (button) -> {
			MinecraftClient.getInstance().openScreen(new PresetsGui());
		}));
		Editor = addButton(new ButtonWidget(60,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Editor), (button) -> {
			MinecraftClient.getInstance().openScreen(new EditorGui());
		}));
		Animation = addButton(new ButtonWidget(5,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Animation), (button) -> {
			MinecraftClient.getInstance().openScreen(new AnimationGui());
		}));
		Frames = addButton(new ButtonWidget(60,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Frames), (button) -> {
			MinecraftClient.getInstance().openScreen(new FramesGui());
		}));
		Presets.active = false;
		
		Options = addButton(new ButtonWidget(5,55,50,20, Text.of(PixelsCharacterModels.TranslatedText.Options), (button) -> {
			MinecraftClient.getInstance().openScreen(new OptionsGui());
		}));
		
		CreatePresetName = addTextField(new TextFieldWidget(textRendererGUI, 5, 90, 50, 20, Text.of("Preset Name")));
		CreatePreset = addButton(new ButtonWidget(5, 115, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
			createPreset(CreatePresetName.getText());
		}));
		DeletePreset = addButton(new ButtonWidget(5, 140, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Remove), (button) -> {
			deletePreset();
		}));
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < presets.length; i++) { //6 per col, max 24
			int num = i;
			ButtonWidget b = addButton( new ButtonWidget(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, Text.of(presets[i].getName().replace(".json", "")), (value) -> {
				SelectPreset(num, value.getMessage().asString());
			}));
			if (PixelsCharacterModels.GuiData.SelectedPresetID != -1) if (i == PixelsCharacterModels.GuiData.SelectedPresetID) b.active = false;
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
		}
	}
	
	public void SelectPreset(int id, String name) {
		PixelsCharacterModels.PCMClient.LoadPreset(id, client.player, PixelsCharacterModels.EntityModelList.get(client.player));
		PixelsCharacterModels.GuiData.SelectedPresetID = id;
		PixelsCharacterModels.GuiData.SelectedPresetName = name;
		client.openScreen(new PresetsGui());
	}
	
	public void createPreset(String s) {
		PixelsCharacterModels.PCMClient.writePreset(s, client.player, PixelsCharacterModels.EntityModelList.get(client.player));
		client.openScreen(new PresetsGui());
	}
	
	public void deletePreset() {
		if (PixelsCharacterModels.GuiData.SelectedPresetID != -1)
			PixelsCharacterModels.PCMClient.DeletePreset(PixelsCharacterModels.GuiData.SelectedPresetID);
		client.openScreen(new PresetsGui());
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
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
