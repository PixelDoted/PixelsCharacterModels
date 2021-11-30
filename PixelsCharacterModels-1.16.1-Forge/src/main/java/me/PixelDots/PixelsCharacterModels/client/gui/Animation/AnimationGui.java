package me.PixelDots.PixelsCharacterModels.client.gui.Animation;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PartsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.SettingsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapBool;
import me.PixelDots.PixelsCharacterModels.util.Maps.MapVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;

public class AnimationGui extends HandlerGui
{
	
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	Button PresetsBTN;
	Button EditorBTN;
	Button AnimationBTN;
	Button Frame;
	
	Button Settings;
	
	Button AnimationsBTN;
	Button PartsBTN;
	
	Button SetRot;
	Button SetTrans;
	Button SetColHeight;
	Button SetRotPoint;
	
	Button MinusBTNRot;
	Button MinusBTNRot1;
	Button MinusBTNRot2;
	
	Button MinusBTNTrans;
	Button MinusBTNTrans1;
	Button MinusBTNTrans2;
	
	Button MinusBTNRotPoint;
	Button MinusBTNRotPoint1;
	Button MinusBTNRotPoint2;
	
	Button RemoveAngle;
	Button stoponwalk;
	
	TextFieldWidget RotBoxX;
	TextFieldWidget RotBoxY;
	TextFieldWidget RotBoxZ;
	
	TextFieldWidget RotPointBoxX;
	TextFieldWidget RotPointBoxY;
	TextFieldWidget RotPointBoxZ;
	
	TextFieldWidget TransBoxX;
	TextFieldWidget TransBoxY;
	TextFieldWidget TransBoxZ;
	
	TextFieldWidget ColHeightBox;
	
	private TranslationText Translation = new TranslationText();
	
	public AnimationGui() {
		super(1, 1, "Animation", "textures/gui/presets.png");
	}
	
	@Override
	protected void init() {
		super.init();
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
		})); EditorBTN.active = true;
		AnimationBTN = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); AnimationBTN.active = false;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
		
		Settings = addButton(new Button(10, 130, 50, 20, Translation.Settings, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new SettingsGui());
		}));
		
		String PartsName = Translation.Parts;
		String AnimName = Translation.Animations;
		
		if (Main.GuiSettings.SelectedPart != "")
			PartsName = Main.GuiSettings.SelectedPart;
		if (Main.GuiSettings.SelectedAnimationID != -1)
			AnimName = Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).id;
		
		AnimationsBTN = addButton(new Button(10, 75, 50, 20, AnimName, (value) -> {
			minecraft.displayGuiScreen(new AnimationsGui());
		}));
		PartsBTN = addButton(new Button(10, 100, 50,20, PartsName, (value) -> {
			minecraft.displayGuiScreen(new PartsGui(1));
		}));
		
		MinusBTNRot = addButton(new Button(75, 75, 20, 20, "+", (value) -> {
			if (MinusBTNRot.getMessage() == "+") MinusBTNRot.setMessage("-");
			else MinusBTNRot.setMessage("+");
		}));
		MinusBTNRot1 = addButton(new Button(75, 100, 20, 20, "+", (value) -> {
			if (MinusBTNRot1.getMessage() == "+") MinusBTNRot1.setMessage("-");
			else MinusBTNRot1.setMessage("+");
		}));
		MinusBTNRot2 = addButton(new Button(75, 125, 20, 20, "+", (value) -> {
			if (MinusBTNRot2.getMessage() == "+") MinusBTNRot2.setMessage("-");
			else MinusBTNRot2.setMessage("+");
		}));
		
		MinusBTNRotPoint = addButton(new Button(180, 75, 20, 20, "+", (value) -> {
			if (MinusBTNRotPoint.getMessage() == "+") MinusBTNRotPoint.setMessage("-");
			else MinusBTNRotPoint.setMessage("+");
		}));
		MinusBTNRotPoint1 = addButton(new Button(180, 100, 20, 20, "+", (value) -> {
			if (MinusBTNRotPoint1.getMessage() == "+") MinusBTNRotPoint1.setMessage("-");
			else MinusBTNRotPoint1.setMessage("+");
		}));
		MinusBTNRotPoint2 = addButton(new Button(180, 125, 20, 20, "+", (value) -> {
			if (MinusBTNRotPoint2.getMessage() == "+") MinusBTNRotPoint2.setMessage("-");
			else MinusBTNRotPoint2.setMessage("+");
		}));
		
		MinusBTNTrans = addButton(new Button(385, 75, 20, 20, "+", (value) -> {
			if (MinusBTNTrans.getMessage() == "+") MinusBTNTrans.setMessage("-");
			else MinusBTNTrans.setMessage("+");
		}));
		MinusBTNTrans1 = addButton(new Button(385, 100, 20, 20, "+", (value) -> {
			if (MinusBTNTrans1.getMessage() == "+") MinusBTNTrans1.setMessage("-");
			else MinusBTNTrans1.setMessage("+");
		}));
		MinusBTNTrans2 = addButton(new Button(385, 125, 20, 20, "+", (value) -> {
			if (MinusBTNTrans2.getMessage() == "+") MinusBTNTrans2.setMessage("-");
			else MinusBTNTrans2.setMessage("+");
		}));
		
		RemoveAngle = addButton(new Button(75, 175, 50, 20, "Remove", (value) -> {
			RemoveAngle();
		}));
		stoponwalk = addButton(new Button(75, 200, 50, 20, Translation.TRUE, (value) -> {
			if (value.getMessage() == Translation.TRUE) value.setMessage(Translation.FALSE);
			else value.setMessage(Translation.TRUE);
			if (Main.GuiSettings.SelectedAnimationID != -1) {
				boolean v = (value.getMessage() == Translation.TRUE ? true : false);
				Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).stoponwalk = v;
				SaveAnimation();
			}
		}));
		if (Main.GuiSettings.SelectedAnimationID != -1)
			stoponwalk.setMessage(String.valueOf( Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).stoponwalk ));
		
		SetRot = addButton(new Button(75, 150, 50, 20, "Set", (value) -> {
			SetAngle();
		}));
		SetRotPoint = addButton(new Button(180, 150, 50, 20, "Set", (value) -> {
			SetRotPoint();
		}));
		SetTrans = addButton(new Button(385, 150, 50, 20, "Set", (value) -> {
			SetTransform();
		}));
		SetColHeight = addButton(new Button(490, 100, 50, 20, "Set", (value) -> {
			SetOther();
		}));
	}
	
	@Override
	public void LoadTextFields() {
		RotBoxX = new TextFieldWidget(fontRenderer, 100, 75, 70, 20, "RotBoxX"); addTextField(RotBoxX);
		RotBoxY = new TextFieldWidget(fontRenderer, 100, 100, 70, 20, "RotBoxX"); addTextField(RotBoxY);
		RotBoxZ = new TextFieldWidget(fontRenderer, 100, 125, 70, 20, "RotBoxX"); addTextField(RotBoxZ);
		
		RotPointBoxX = new TextFieldWidget(fontRenderer, 205, 75, 70, 20, "RotPointBoxX"); addTextField(RotPointBoxX);
		RotPointBoxY = new TextFieldWidget(fontRenderer, 205, 100, 70, 20, "RotPointBoxX"); addTextField(RotPointBoxY);
		RotPointBoxZ = new TextFieldWidget(fontRenderer, 205, 125, 70, 20, "RotPointBoxX"); addTextField(RotPointBoxZ);
		
		TransBoxX = new TextFieldWidget(fontRenderer, 410, 75, 70, 20, "TransBoxX"); addTextField(TransBoxX);
		TransBoxY = new TextFieldWidget(fontRenderer, 410, 100, 70, 20, "TransBoxX"); addTextField(TransBoxY);
		TransBoxZ = new TextFieldWidget(fontRenderer, 410, 125, 70, 20, "TransBoxX"); addTextField(TransBoxZ);
		
		ColHeightBox = new TextFieldWidget(fontRenderer, 490, 75, 70, 20, "ColHeightBox"); addTextField(ColHeightBox);
		super.LoadTextFields();
	}
	
	@Override
	public void LoadString(int clr) {
		drawString(fontRenderer, "stop when", 10, 160, clr);
		drawString(fontRenderer, "walking" , 10, 200, clr);
		
		drawString(fontRenderer, "Rotation", 75, 62, clr);
		drawString(fontRenderer, "Rotation", 180, 50, clr);
		drawString(fontRenderer, "Point", 180, 62, clr);
		
		drawString(fontRenderer, "Transform", 385, 62, clr);
		drawString(fontRenderer, "Collision", 490, 50, clr);
		drawString(fontRenderer, "Height", 490, 62, clr);
		super.LoadString(clr);
	}
	
	public void SetTransform() {
		if (!Utillities.isNumeric(TransBoxX.getText()) || !Utillities.isNumeric(TransBoxY.getText()) || !Utillities.isNumeric(TransBoxZ.getText())) return;
		float X = (MinusBTNTrans.getMessage() == "+" ? (Float.parseFloat(TransBoxX.getText()) / 100) : (Float.parseFloat(TransBoxX.getText()) / 100) *  -1);
		float Y = (MinusBTNTrans1.getMessage() == "+" ? (Float.parseFloat(TransBoxY.getText()) / 100) : (Float.parseFloat(TransBoxY.getText()) / 100) *  -1);
		float Z = (MinusBTNTrans2.getMessage() == "+" ? (Float.parseFloat(TransBoxZ.getText()) / 100) : (Float.parseFloat(TransBoxZ.getText()) / 100) *  -1);
		Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).Translate = new MapVec3(X,Y,Z);
		SaveAnimation();
	}
	
	public void SetOther() {
		if (!Utillities.isNumeric(ColHeightBox.getText())) return;
		Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).ColHeight = Float.parseFloat(ColHeightBox.getText()) / 10;
		SaveAnimation();
	}
	
	public void SetAngle() {
		if (!Utillities.isNumeric(RotBoxX.getText()) || !Utillities.isNumeric(RotBoxY.getText()) || !Utillities.isNumeric(RotBoxZ.getText())) return;
		
		if (Float.parseFloat(RotBoxX.getText()) >= 360) RotBoxX.setText("0");
		if (Float.parseFloat(RotBoxY.getText()) >= 360) RotBoxY.setText("0");
		if (Float.parseFloat(RotBoxZ.getText()) >= 360) RotBoxZ.setText("0");
		
		if (Main.GuiSettings.SelectedPart.equalsIgnoreCase("")) return;
		else if (Main.GuiSettings.SelectedPartIsLimb == true && Main.GuiSettings.SelectedPart.equalsIgnoreCase("ItemRight")) return;
		else if (Main.GuiSettings.SelectedPartIsLimb == true && Main.GuiSettings.SelectedPart.equalsIgnoreCase("ItemLeft")) return;
		
		@SuppressWarnings("unused")
		Animation anim = Main.animations.list.get(Main.GuiSettings.SelectedAnimationID);
		double X = 1.6F * (Float.parseFloat(RotBoxX.getText()) / 90); X = (Math.floor(X * 10) / 10);
		double Y = 1.6F * (Float.parseFloat(RotBoxY.getText()) / 90); Y = (Math.floor(Y * 10) / 10);
		double Z = 1.6F * (Float.parseFloat(RotBoxZ.getText()) / 90); Z = (Math.floor(Z * 10) / 10);
		X = MinusBTNRot.getMessage() == "+" ? X * -1 : X;
		Y = MinusBTNRot1.getMessage() == "+" ? Y * -1 : Y;
		Z = MinusBTNRot2.getMessage() == "+" ? Z * -1 : Z;
		Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).setAngle(PartsBTN.getMessage().toLowerCase(), new MapVec3((float)X,(float)Y,(float)Z), new MapBool(Main.GuiSettings.SelectedPartIsLimb));
		SaveAnimation();
	}
	
	public void SetRotPoint() {
		if (!Utillities.isNumeric(RotPointBoxX.getText()) || !Utillities.isNumeric(RotPointBoxY.getText()) || !Utillities.isNumeric(RotPointBoxZ.getText())) return;
		float X = (MinusBTNRotPoint.getMessage() == "+" ? (Float.parseFloat(RotPointBoxX.getText()) / 100) : (Float.parseFloat(RotPointBoxX.getText()) / 100) *  -1);
		float Y = (MinusBTNRotPoint1.getMessage() == "+" ? (Float.parseFloat(RotPointBoxY.getText()) / 100) : (Float.parseFloat(RotPointBoxY.getText()) / 100) *  -1);
		float Z = (MinusBTNRotPoint2.getMessage() == "+" ? (Float.parseFloat(RotPointBoxZ.getText()) / 100) : (Float.parseFloat(RotPointBoxZ.getText()) / 100) *  -1);
		Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).setAngle(PartsBTN.getMessage().toLowerCase(), new MapBool(Main.GuiSettings.SelectedPartIsLimb), new MapVec3(X,Y,Z));
		SaveAnimation();
	}
	
	public void RemoveAngle() {
		if (Main.GuiSettings.SelectedAnimationID == -1) return;
		if (Main.GuiSettings.SelectedPartID == -1) return;
		Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).removeAngle(PartsBTN.getMessage().toLowerCase());
		SaveAnimation();
	}
	
	public void SaveAnimation() {
		Main.animations.SaveAnimation(Main.GuiSettings.SelectedAnimationID);
	}
	
	@Override
	public void UpdateAction() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		super.UpdateAction();
	}

}