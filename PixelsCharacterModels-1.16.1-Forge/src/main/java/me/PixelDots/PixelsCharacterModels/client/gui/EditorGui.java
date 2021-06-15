package me.PixelDots.PixelsCharacterModels.client.gui;

import java.net.MalformedURLException;
import java.net.URL;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.client.model.ModelParts;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import me.edoren.skin_changer.server.SkinsCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class EditorGui extends HandlerGui
{
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	//TextFields
	TextFieldWidget ScaleField;
	TextFieldWidget URLField;
	TextFieldWidget SizeField;
	TextFieldWidget PosXField;
	TextFieldWidget PosYField;
	TextFieldWidget PosZField;
	
	//Sliders
	Slider ScaleSlider;
	Slider URLSlider;
	Slider SizeSlider;
	Slider PosXSlider;
	Slider PosYSlider;
	Slider PosZSlider;
	
	//Buttons
	Button PresetsBTN;
	Button EditorBTN;
	Button AnimationBTN;
	Button Frame;
	
	Button SkinBTN;
	Button DefualtBTN;
	Button MinusBTN1;
	Button MinusBTN2;
	Button MinusBTN3;
	Button ShowBTN;
	Button ElytraBTN;
	Button PartBTN;
	
	private TranslationText Translation = new TranslationText();
	
	public EditorGui() {
		super(1, 1, "Editor", "textures/gui/presets.png");
	}
	
	@Override
	protected void init() {
		super.init();
		GetGlobalData();
		if (Main.GuiSettings.SelectedPartIsLimb) {
			GetLimbData();
		} else {
			GetPartData();
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int centerX = width / 2;
		int centerY = height / 2;
		drawEntityOnScreen(centerX, centerY + 125, 60, (float)(centerX) - mouseX, (float)(centerY + 125) - mouseY, Minecraft.getInstance().player);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean charTyped(char Key, int KeyCode) {
		return super.charTyped(Key, KeyCode);
	}
	
	@Override
	public void LoadButtons() {
		PresetsBTN = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			minecraft.displayGuiScreen(new PresetsGui());
		})); PresetsBTN.active = true;
		EditorBTN = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			minecraft.displayGuiScreen(new EditorGui());
		})); EditorBTN.active = false;
		AnimationBTN = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); AnimationBTN.active = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
		SkinBTN = addButton(new Button(30, 315, 50, 20, Translation.Skin, (value) -> {
			if (URLField.getText() != "") setSkin();//Main.GuiSettings.player.sendMessage(new StringTextComponent("/skin set " + URLField.getText()));//GlobalModelManager.Skin.setSkin(Main.GuiSettings.player, URLField.getText());
		}));
		DefualtBTN = addButton(new Button(480, 10, 50, 20, Translation.Defualt, (value) -> {
			MinusBTN1.setMessage("+");
			MinusBTN2.setMessage("+");
			MinusBTN3.setMessage("+");
			ShowBTN.setMessage(Translation.Show);
			ScaleField.setText("100");
			SizeField.setText("100");
			PosXField.setText("0");
			PosYField.setText("0");
			PosZField.setText("0");
		}));
		MinusBTN1 = addButton(new Button(575, 85, 20, 20, "+", (value) -> {
			if (MinusBTN1.getMessage() == "+") MinusBTN1.setMessage("-");
			else MinusBTN1.setMessage("+");
		}));
		MinusBTN2 = addButton(new Button(575, 110, 20, 20, "+", (value) -> {
			if (MinusBTN2.getMessage() == "+") MinusBTN2.setMessage("-");
			else MinusBTN2.setMessage("+");
		}));
		MinusBTN3 = addButton(new Button(575, 135, 20, 20, "+", (value) -> {
			if (MinusBTN3.getMessage() == "+") MinusBTN3.setMessage("-");
			else MinusBTN3.setMessage("+");
		}));
		ShowBTN = addButton(new Button(600, 160, 70, 20, Translation.Show, (value) -> {
			if (ShowBTN.getMessage() == Translation.Show) {
				ShowBTN.setMessage(Translation.Hide);
				Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Show = false;
				HideObj();
			}
			else {
				ShowBTN.setMessage(Translation.Show);
				ShowObj();
			}
		}));
		ElytraBTN = addButton(new Button(80, 315, 50, 20, Translation.Elytra, (value) -> {
			if (URLField.getText() != "") setElytra();//Main.GuiSettings.player.sendMessage(new StringTextComponent("/cape set " + URLField.getText()));//GlobalModelManager.Elytra.setElytra(Main.GuiSettings.player, URLField.getText());
		}));
		String PartsName = Translation.Parts;
		
		if (Main.GuiSettings.SelectedPart != "") {
			GlobalModelData data = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data;
			PartsName = Main.GuiSettings.SelectedPart;
			if (Main.GuiSettings.SelectedPartIsLimb) {
				ShowBTN.setMessage(data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Show ? Translation.Show : Translation.Hide);
				MinusBTN1.setMessage(String.valueOf(data.parts.Limb.get(Main.GuiSettings.SelectedPartID).X).startsWith("-") ? "-" : "+");
				MinusBTN2.setMessage(String.valueOf(data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Y).startsWith("-") ? "-" : "+");
				MinusBTN3.setMessage(String.valueOf(data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Z).startsWith("-") ? "-" : "+");
			} else {
				ShowBTN.setMessage(data.parts.Part.get(Main.GuiSettings.SelectedPartID).Show ? Translation.Show : Translation.Hide);
				MinusBTN1.setMessage(String.valueOf(data.parts.Part.get(Main.GuiSettings.SelectedPartID).X).startsWith("-") ? "-" : "+");
				MinusBTN2.setMessage(String.valueOf(data.parts.Part.get(Main.GuiSettings.SelectedPartID).Y).startsWith("-") ? "-" : "+");
				MinusBTN3.setMessage(String.valueOf(data.parts.Part.get(Main.GuiSettings.SelectedPartID).Z).startsWith("-") ? "-" : "+");
			}
		}
		PartBTN = addButton(new Button(600, 10, 75, 20, PartsName, (value) -> {
			minecraft.displayGuiScreen(new PartsGui());
		}));
	}
	
	@Override
	public void LoadTextFields() {
		//if (Main.OtherSaveData.sliders == false) {
			ScaleField = new TextFieldWidget(fontRenderer, 40, 80, 75, 20, "ScaleField"); addTextField(ScaleField);
			
			SizeField = new TextFieldWidget(fontRenderer, 600, 60, 70, 20, "SizeField"); addTextField(SizeField);
			PosXField = new TextFieldWidget(fontRenderer, 600, 85, 70, 20, "PosXField"); addTextField(PosXField);
			PosYField = new TextFieldWidget(fontRenderer, 600, 110, 70, 20, "PosYField"); addTextField(PosYField);
			PosZField = new TextFieldWidget(fontRenderer, 600, 135, 70, 20, "PosZField"); addTextField(PosZField);
		/*} else {
			GlobalModelData data = Main.Data.playerData.get(Main.GuiSettings.player).data;
			int Size = 100;
			int PosX = 100;
			int PosY = 100;
			int PosZ = 100;
			ModelParts parts = data.parts;
			int Scale = Math.round(data.GlobalScale * 100);
			if (Main.GuiSettings.SelectedPartID != -1) {
				if (Main.GuiSettings.SelectedPartIsLimb) {
					Size = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Scale * 100);
					PosX = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).X * 100);
					PosY = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Y * 100);
					PosZ = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Z * 100);
					LoadSliders(Size, PosX, PosY, PosZ, Scale, data.parts.Part.get(Main.GuiSettings.SelectedPartID));
				} else {
					if (parts.Part.size() -1 >= Main.GuiSettings.SelectedPartID) {
						Size = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Scale * 100);
						PosX = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).X * 100);
						PosY = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Y * 100);
						PosZ = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Z * 100);
						LoadSliders(Size, PosX, PosY, PosZ, Scale, data.parts.Part.get(Main.GuiSettings.SelectedPartID));
					} else {
						LoadSlidersFake(Size, PosX, PosY, PosZ, Scale);
					}
				}
			} else {
				LoadSlidersFake(Size, PosX, PosY, PosZ, Scale);
			}
		}*/
		
		URLField = new TextFieldWidget(fontRenderer, 30, 340, 150, 20, "URLField"); addTextField(URLField);
		URLField.setMaxStringLength(Main.OtherSaveData.maxURLlength);
		super.LoadTextFields();
	}
	
	/*public void LoadSliders(int Size, int PosX, int PosY, int PosZ, int Scale, ModelPart part) {
		ScaleSlider = new Slider(40, 80, 75, 20, "", "%", 10, 500, Scale, false, true, null, (value) -> {
			Main.Data.playerData.get(Main.GuiSettings.player).data.GlobalScale = (float)value.getValue();//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(ScaleSlider);
		
		SizeSlider = new Slider(600, 60, 70, 20, "", "%", 10, 500, Size, false, true, null, (value) -> {
			part.Scale = (float)value.getValue();//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(SizeSlider);
		PosXSlider = new Slider(600, 85, 70, 20, "", "%", -500, 500, PosX, false, true, null, (value) -> {
			float v = (float)(MinusBTN1.getMessage() == "+" ? value.getValue() / 100F : (value.getValue() / 100F) * -1);
			part.X = v;//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(PosXSlider);
		PosYSlider = new Slider(600, 110, 70, 20, "", "%", -500, 500, PosY, false, true, null, (value) -> {
			float v = (float)(MinusBTN2.getMessage() == "+" ? value.getValue() / 100F : (value.getValue() / 100F) * -1);
			part.Y = v;//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(PosYSlider);
		PosZSlider = new Slider(600, 135, 70, 20, "", "%", -500, 500, PosZ, false, true, null, (value) -> {
			float v = (float)(MinusBTN3.getMessage() == "+" ? value.getValue() : value.getValue() * -1) / 1000f;
			part.Z = v;//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(PosZSlider);
	}
	public void LoadSlidersFake(int Size, int PosX, int PosY, int PosZ, int Scale) {
		ScaleSlider = new Slider(40, 80, 75, 20, "", "%", 10, 500, Scale, false, true, null, (value) -> {
			Main.Data.playerData.get(Main.GuiSettings.player).data.GlobalScale = (float)value.getValue()  / 1000f;//Float.parseFloat(SizeField.getText()) / 100F;
		}); addSlider(ScaleSlider);
		
		SizeSlider = new Slider(600, 60, 70, 20, "", "%", 10, 500, Size, false, true, null, (value) -> {
		}); addSlider(SizeSlider);
		PosXSlider = new Slider(600, 85, 70, 20, "", "%", -500, 500, PosX, false, true, null, (value) -> {
		}); addSlider(PosXSlider);
		PosYSlider = new Slider(600, 110, 70, 20, "", "%", -500, 500, PosY, false, true, null, (value) -> {
		}); addSlider(PosYSlider);
		PosZSlider = new Slider(600, 135, 70, 20, "", "%", -500, 500, PosZ, false, true, null, (value) -> {
		}); addSlider(PosZSlider);
	}*/

	
	@Override
	public void UpdateTextFields() {
		//if (!Main.OtherSaveData.sliders) {
			if (Utillities.isNumeric(ScaleField.getText())) {
				float value = Float.parseFloat(ScaleField.getText());
				if (value >= 10 && value <= 500)
					Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.GlobalScale = Float.parseFloat(ScaleField.getText()) / 100;
			}
			if (Main.GuiSettings.SelectedPartID != -1) {
				if (Main.GuiSettings.SelectedPartIsLimb) {
					UpdateLimbData();
				} else {
					UpdatePartData();
				}
			}
		//}
		SavePreset();
		UpdateAction();
	}
	
	@Override
	public void LoadString(int clr) {
		this.drawString(fontRenderer, Translation.URL, 5, 346, clr);
		this.drawString(fontRenderer, Translation.ArmType, 375, 15, clr);
		this.drawString(fontRenderer, Translation.GlobalScale, 10, 60, clr);
		this.drawString(fontRenderer, Translation.Scale, 565, 66, clr);
		this.drawString(fontRenderer, Translation.Position, 530, 91, clr);
		/*drawString(fontRenderer, Translation.Looking, 35, 116, 0xFFFFFF);
		drawString(fontRenderer, Translation.Swing, 35, 142, 0xFFFFFF);*/
		super.LoadString(clr);
	}
	
	@Override
	public void UpdateAction() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		super.UpdateAction();
	}
	 
	public void SavePreset() {
		if (Main.GuiSettings.SelectedPresetID == -1) return;
		Main.presets.SavePreset(Main.GuiSettings.SelectedPresetID);
	}
	
	public void HideObj() {
		if (Main.GuiSettings.SelectedPartIsLimb) {
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Show = false;
		} else {
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).Show = false;
		}
	}
	
	public void ShowObj() {
		if (Main.GuiSettings.SelectedPartIsLimb) {
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Show = true;
		} else {
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).Show = true;
		}
	}
	
	public void setElytra() {
		try {
			SkinsCommand.setPlayerCapeByURL(Main.GuiSettings.player, Main.GuiSettings.player, new URL(URLField.getText()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		SavePreset();
	}
	
	public void setSkin() {
		try {
			SkinsCommand.setPlayerSkinByURL(Main.GuiSettings.player, Main.GuiSettings.player, new URL(URLField.getText()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		SavePreset();
	}
	
	//--LimbData--\\
	public void UpdateLimbData() {
		//if (Main.OtherSaveData.sliders) return;
		if (Utillities.isNumeric(SizeField.getText())) {
			float value = Float.parseFloat(SizeField.getText());
			if (value >= 10 && value <= 500)
				Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Scale = value / 100F;
		}
		if (Utillities.isNumeric(PosXField.getText())) {
			float value = Float.parseFloat(PosXField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).X = MinusBTN1.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
		if (Utillities.isNumeric(PosYField.getText())) {
			float value = Float.parseFloat(PosYField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Y = MinusBTN2.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
		if (Utillities.isNumeric(PosZField.getText())) {
			float value = Float.parseFloat(PosZField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Limb.get(Main.GuiSettings.SelectedPartID).Z = MinusBTN3.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
	}
	
	public void GetLimbData() {
		//if (Main.OtherSaveData.sliders) return;
		if (Main.GuiSettings.SelectedPartID == -1 || !Main.GuiSettings.SelectedPartIsLimb) return;
		ModelParts parts = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts;
		SizeField.setText(String.valueOf(Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Scale * 100)));
		int PosX = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).X * 100);
		int PosY = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Y * 100);
		int PosZ = Math.round(parts.Limb.get(Main.GuiSettings.SelectedPartID).Z * 100);
		if (String.valueOf(PosX).startsWith("-")) {
			MinusBTN1.setMessage("-");
			PosX *= -1;
		}
		if (String.valueOf(PosY).startsWith("-")) {
			MinusBTN2.setMessage("-");
			PosY *= -1;
		}
		if (String.valueOf(PosZ).startsWith("-")) {
			MinusBTN3.setMessage("-");
			PosZ *= -1;
		}
		PosXField.setText(String.valueOf(PosX));
		PosYField.setText(String.valueOf(PosY));
		PosZField.setText(String.valueOf(PosZ));
	}
	//--LimbData--\\
	
	//--PartData--\\
	public void UpdatePartData() {
		//if (Main.OtherSaveData.sliders) return;
		if (Utillities.isNumeric(SizeField.getText())) {
			float value = Float.parseFloat(SizeField.getText());
			if (value >= 10 && value <= 500)
				Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).Scale = Float.parseFloat(SizeField.getText()) / 100F;
		}
		if (Utillities.isNumeric(PosXField.getText())) {
			float value = Float.parseFloat(PosXField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).X = MinusBTN1.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
		if (Utillities.isNumeric(PosYField.getText())) {
			float value = Float.parseFloat(PosYField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).Y = MinusBTN2.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
		if (Utillities.isNumeric(PosZField.getText())) {
			float value = Float.parseFloat(PosZField.getText());
			Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.get(Main.GuiSettings.SelectedPartID).Z = MinusBTN3.getMessage() == "+" ? value / 100F : (value / 100F) * -1;
		}
	}
	
	public void GetPartData() {
		//if (Main.OtherSaveData.sliders) return;
		if (Main.GuiSettings.SelectedPartID == -1 || Main.GuiSettings.SelectedPartIsLimb) return;
		ModelParts parts = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts;
		SizeField.setText(String.valueOf(Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Scale * 100)));
		int PosX = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).X * 100);
		int PosY = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Y * 100);
		int PosZ = Math.round(parts.Part.get(Main.GuiSettings.SelectedPartID).Z * 100);
		if (String.valueOf(PosX).startsWith("-")) {
			MinusBTN1.setMessage("-");
			PosX *= -1;
		}
		if (String.valueOf(PosY).startsWith("-")) {
			MinusBTN2.setMessage("-");
			PosY *= -1;
		}
		if (String.valueOf(PosZ).startsWith("-")) {
			MinusBTN3.setMessage("-");
			PosZ *= -1;
		}
		PosXField.setText(String.valueOf(PosX));
		PosYField.setText(String.valueOf(PosY));
		PosZField.setText(String.valueOf(PosZ));
	}
	//--PartData--\\
	
	public void GetGlobalData() {
		GlobalModelData data = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data;
		ScaleField.setText(String.valueOf(Math.round(data.GlobalScale * 100)));
	}

}
