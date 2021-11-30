package me.pixeldots.pixelscharactermodels.GUI;

import java.io.File;

import org.apache.commons.lang3.math.NumberUtils;

import com.mojang.brigadier.arguments.FloatArgumentType;

import lain.mods.skins.init.fabric.FabricOfflineSkins;
import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;

public class EditorGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget Parts;
	
	public TextFieldWidget GlobalScaleField;
	public TextFieldWidget GlobalSkinField;
	public ButtonWidget ShowSkinList;
	
	public TextFieldWidget PosXField;
	public TextFieldWidget PosYField;
	public TextFieldWidget PosZField;
	public TextFieldWidget ScaleXField;
	public TextFieldWidget ScaleYField;
	public TextFieldWidget ScaleZField;
	public ButtonWidget ShowButton;
	
	public EditorGui() {
		super("Editor");
	}
	
	@Override
	public void init() {
		super.init();
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
		Editor.active = false;
		
		int selectedPart = PixelsCharacterModels.GuiData.SelectedPartID;
		Parts = addButton(new ButtonWidget(this.width-108, 5, 100, 20, Text.of(PixelsCharacterModels.TranslatedText.Parts), (button) -> {
			client.openScreen(new PartsGui(this));
		}));
		if (selectedPart > -1) Parts.setMessage(Text.of(PixelsCharacterModels.GuiData.SelectedPart)); 
		
		GlobalScaleField = addTextField(new TextFieldWidget(textRendererGUI, 70, 60, 100, 25, Text.of("Scale")));
		GlobalScaleField.setText(String.valueOf(getPlayerScale()));
		
		GlobalSkinField = addTextField(new TextFieldWidget(textRendererGUI, 70, 90, 100, 25, Text.of("Skin")));
		GlobalSkinField.setText(FabricOfflineSkins.skinSuffix.get(PixelsCharacterModels.thisPlayer.getGameProfile().getId()));
		ShowSkinList = addButton(new ButtonWidget(70, 120, 100, 20, Text.of(PixelsCharacterModels.TranslatedText.ListSkins), (button) -> {
			ListSkins();
		}));
		
		PosXField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 45, 100, 20, Text.of("PositionX")));
		PosYField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 70, 100, 20, Text.of("PositionY")));
		PosZField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 95, 100, 20, Text.of("PositionZ")));
		
		ScaleXField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 135, 100, 20, Text.of("ScaleX")));
		ScaleYField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 160, 100, 20, Text.of("ScaleY")));
		ScaleZField = addTextField(new TextFieldWidget(textRendererGUI, this.width-108, 185, 100, 20, Text.of("ScaleZ")));
		
		ShowButton = addButton(new ButtonWidget(this.width-108, 210, 100, 20, Text.of("Showing"), (button) -> {
			if (ShowButton.getMessage().asString() == "Showing") ShowButton.setMessage(Text.of("Hiding"));
			else ShowButton.setMessage(Text.of("Showing"));
		}));
		
		if (PixelsCharacterModels.dataPackets.containsKey(PixelsCharacterModels.GuiData.SelectedPartModel)) {
			PosXField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).pos.X));
			PosYField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).pos.Y));
			PosZField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).pos.Z));
			
			ScaleXField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).scale.X));
			ScaleYField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).scale.Y));
			ScaleZField.setText(String.valueOf(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).scale.Z));
			ShowButton.setMessage(Text.of(PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).Show ? "Showing" : "Hiding"));
		}
		//drawEntity(575/2, 200, 30, 0f, 0f, PixelsCharacterModels.GuiData.entity);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		drawString(matrices, "Global Scale", 5, 63, 16777215);
		drawString(matrices, "Skin Suffix", 5, 93, 16777215);
		
		drawString(matrices, "Position", this.width-118, 25, 16777215);
		drawString(matrices, "X", this.width-118, 45, 16777215);
		drawString(matrices, "Y", this.width-118, 70, 16777215);
		drawString(matrices, "Z", this.width-118, 95, 16777215);
		
		drawString(matrices, "Scale", this.width-118, 115, 16777215);
		drawString(matrices, "X", this.width-118, 135, 16777215);
		drawString(matrices, "Y", this.width-118, 160, 16777215);
		drawString(matrices, "Z", this.width-118, 185, 16777215);
		drawString(matrices, "Preset: " + PixelsCharacterModels.GuiData.SelectedPresetName, 120, 5);
		
		drawEntity(this.width/2, this.height/2, 75, (float)(this.width/2) - mouseX, (float)(this.height/2-125) - mouseY, PixelsCharacterModels.GuiData.entity);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void tick() {
		if (isNumeric(GlobalScaleField.getText())) {
			setPlayerScale();
		}
		setPlayerSkin();
		setPlayerLimbData();
		//PixelsCharacterModels.GuiData.setPlayerSkin();
		super.tick();
	}
	
	public void setPlayerScale() {
		if (PixelsCharacterModels.client.isInSingleplayer()) {
			PixelsCharacterModels.client.getServer().getCommandManager().execute(
					PixelsCharacterModels.client.getServer().getCommandSource(), 
					"/scale set " + GlobalScaleField.getText() + " " + PixelsCharacterModels.thisPlayer.getDisplayName().asString());
		}
		
		if (PixelsCharacterModels.GuiData.SelectedPresetID != -1) {
			PixelsCharacterModels.PCMClient.writePreset(PixelsCharacterModels.GuiData.SelectedPresetName, client.player, PixelsCharacterModels.EntityModelList.get(client.player));
		}
	}
	
	public float getPlayerScale() {
		ScaleData data = ScaleType.BASE.getScaleData(PixelsCharacterModels.thisPlayer);
		return data.getBaseScale();
	}
	
	public void setPlayerLimbData() {
		if (PixelsCharacterModels.dataPackets.containsKey(PixelsCharacterModels.GuiData.SelectedPartModel)) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel);
			setModelPartScale(data);
			setModelPartPos(data);
			setModelPartVisible(data);
		}
	}
	
	public void setPlayerSkin() {
		if (FabricOfflineSkins.skinSuffix.get(PixelsCharacterModels.GuiData.entity.getGameProfile().getId()) == GlobalSkinField.getText()) return;
		FabricOfflineSkins.skinSuffix.put(PixelsCharacterModels.GuiData.entity.getGameProfile().getId(), GlobalSkinField.getText());
		FabricOfflineSkins.ReloadSkins(client);
		savePreset();
	}
	
	public void ListSkins() {
		File[] files = new File(PixelsCharacterModels.client.runDirectory+"/cachedImages/skins").listFiles();
		PixelsCharacterModels.client.player.sendMessage(new LiteralText("local skins >"), false);
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) continue;
			PixelsCharacterModels.client.player.sendMessage(new LiteralText(files[i].getName()), false);
		}
		PixelsCharacterModels.client.player.sendMessage(new LiteralText("< local skins"), false);
	}
	
	public boolean isNumeric(String s) {
		return NumberUtils.isCreatable(s);
	}
	
	public boolean setModelPartScale(ModelPartData data) {
		boolean updated = false;
		if (isNumeric(ScaleXField.getText())) {
			if (data.scale.X != Float.parseFloat(ScaleXField.getText()))
				data.scale.X = Float.parseFloat(ScaleXField.getText()); updated = true;
		}
		if (isNumeric(ScaleYField.getText())) { 
			if (data.scale.Y != Float.parseFloat(ScaleYField.getText()))
				data.scale.Y = Float.parseFloat(ScaleYField.getText()); updated = true;
		}
		if (isNumeric(ScaleZField.getText())) { 
			if (data.scale.Z != Float.parseFloat(ScaleZField.getText()))
				data.scale.Z = Float.parseFloat(ScaleZField.getText()); updated = true;
		}
		if (updated) savePreset();
		return updated;
	}
	
	public boolean setModelPartPos(ModelPartData data) {
		boolean updated = false;
		if (isNumeric(PosXField.getText())) {
			if (data.pos.X != Float.parseFloat(PosXField.getText()))
				data.pos.X = Float.parseFloat(PosXField.getText()); updated = true;
		}
		if (isNumeric(PosYField.getText())) { 
			if (data.pos.Y != Float.parseFloat(PosYField.getText()))
				data.pos.Y = Float.parseFloat(PosYField.getText()); updated = true;
		}
		if (isNumeric(PosZField.getText())) { 
			if (data.pos.Z != Float.parseFloat(PosZField.getText()))
				data.pos.Z = Float.parseFloat(PosZField.getText()); updated = true;
		}
		if (updated) savePreset();
		return updated;
	}
	
	public boolean setModelPartVisible(ModelPartData data) {
		if (data.Show == (ShowButton.getMessage().asString() == "Showing" ? true : false)) return false;
		data.Show = ShowButton.getMessage().asString() == "Showing" ? true : false;
		savePreset();
		return true;
	}

	public void savePreset() {
		System.out.println("saving");
		if (PixelsCharacterModels.GuiData.SelectedPresetID != -1) {
			PixelsCharacterModels.PCMClient.writePreset(PixelsCharacterModels.GuiData.SelectedPresetName.replace(".json", ""), client.player, PixelsCharacterModels.EntityModelList.get(client.player));
		}
	}
	
}
