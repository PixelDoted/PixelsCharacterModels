package me.PixelDots.PixelsCharacterModels.client.gui.PartCreation;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PartsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.CreatePart;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;

public class CreatePartGui extends HandlerGui
{
	
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	TextFieldWidget NameField;
	
	TextFieldWidget PreScaleXField;
	TextFieldWidget PreScaleYField;
	TextFieldWidget PreScaleZField;
	
	TextFieldWidget PreXField;
	TextFieldWidget PreYField;
	TextFieldWidget PreZField;
	
	TextFieldWidget RotPointXField;
	TextFieldWidget RotPointYField;
	TextFieldWidget RotPointZField;
	
	TextFieldWidget TextureUField;
	TextFieldWidget TextureVField;
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	
	Button CreateBTN;
	Button MeshTypeBTN;
	Button ParentBTN;
	
	Button MinusPreX;
	Button MinusPreY;
	Button MinusPreZ;
	
	Button MinusRotPointX;
	Button MinusRotPointY;
	Button MinusRotPointZ;
	
	private TranslationText Translation = new TranslationText();
	
	private CreatePart create = new CreatePart();
	
	boolean firstAdd = false;
	int UseableID = -1;
	
	public CreatePartGui() {
		super(1,1 , "Presets", "textures/gui/presets.png");
		UseableID = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.size();
	}
	
	public CreatePartGui(CreatePart create) {
		this();
		this.create = create;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int centerX = width / 2;
		int centerY = height / 2;
		//drawEntityOnScreen(centerX + 175, centerY, 60, (float)(centerX) - mouseX, (float)(centerY - 30) - mouseY, Minecraft.getInstance().player);
		drawEntityOnScreen(centerX + 175, centerY, 60, (float)(centerX + 175) - mouseX, (float)(centerY) - mouseY, Main.GuiSettings.player);
		super.render(mouseX, mouseY, partialTicks);
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
		Presets = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			RemoveObject();
			minecraft.displayGuiScreen(new PresetsGui());
		})); Presets.active = true;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			RemoveObject();
			minecraft.displayGuiScreen(new EditorGui());
		})); Editor.active = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			RemoveObject();
			minecraft.displayGuiScreen(new AnimationGui());
		})); Animation.active = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			RemoveObject();
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
		
		CreateBTN = addButton(new Button(250, 190, 50, 20, Translation.Create, (value) -> {
			RemoveObject();
			Minecraft.getInstance().displayGuiScreen(new PartsGui(create, true));
		}));
		
		String MeshTypeText = "cube";
		if (!(Main.GuiSettings.SelectedMeshIsCube)) MeshTypeText = Main.GuiSettings.SelectedMesh;
		
		MeshTypeBTN = addButton(new Button(225, 40, 50, 20, MeshTypeText, (value) -> {
			RemoveObject();
			Minecraft.getInstance().displayGuiScreen(new MeshTypeGui(create));
		}));
		ParentBTN = addButton(new Button(275, 40, 50, 20, Main.GuiSettings.SelectedPart, (value) -> {
			RemoveObject();
			Minecraft.getInstance().displayGuiScreen(new PartsGui(create, false));
		}));
		
		MinusPreX = addButton(new Button(190, 100, 20, 20, "+", (value) -> {
			if (MinusPreX.getMessage() == "+") MinusPreX.setMessage("-");
			else MinusPreX.setMessage("+");
		}));
		MinusPreY = addButton(new Button(280, 100, 20, 20, "+", (value) -> {
			if (MinusPreY.getMessage() == "+") MinusPreY.setMessage("-");
			else MinusPreY.setMessage("+");
		}));
		MinusPreZ = addButton(new Button(370, 100, 20, 20, "+", (value) -> {
			if (MinusPreZ.getMessage() == "+") MinusPreZ.setMessage("-");
			else MinusPreZ.setMessage("+");
		}));
		
		MinusRotPointX = addButton(new Button(190, 130, 20, 20, "+", (value) -> {
			if (MinusRotPointX.getMessage() == "+") MinusRotPointX.setMessage("-");
			else MinusRotPointX.setMessage("+");
		}));
		MinusRotPointY = addButton(new Button(280, 130, 20, 20, "+", (value) -> {
			if (MinusRotPointY.getMessage() == "+") MinusRotPointY.setMessage("-");
			else MinusRotPointY.setMessage("+");
		}));
		MinusRotPointZ = addButton(new Button(370, 130, 20, 20, "+", (value) -> {
			if (MinusRotPointZ.getMessage() == "+") MinusRotPointZ.setMessage("-");
			else MinusRotPointZ.setMessage("+");
		}));
	}
	
	@Override
	public void LoadTextFields() {
		NameField = new TextFieldWidget(fontRenderer, 250, 10, 50, 20, "NameField"); addTextField(NameField);
		
		PreScaleXField = new TextFieldWidget(fontRenderer, 190, 70, 50, 20, "PreScaleXField"); addTextField(PreScaleXField);
		PreScaleYField = new TextFieldWidget(fontRenderer, 250, 70, 50, 20, "PreScaleYField"); addTextField(PreScaleYField);
		PreScaleZField = new TextFieldWidget(fontRenderer, 310, 70, 50, 20, "PreScaleZField"); addTextField(PreScaleZField);
		
		PreXField = new TextFieldWidget(fontRenderer, 220, 100, 50, 20, "PreXField"); addTextField(PreXField);
		PreYField = new TextFieldWidget(fontRenderer, 310, 100, 50, 20, "PreYField"); addTextField(PreYField);
		PreZField = new TextFieldWidget(fontRenderer, 400, 100, 50, 20, "PreZField"); addTextField(PreZField);
		
		RotPointXField = new TextFieldWidget(fontRenderer, 220, 130, 50, 20, "PreScaleXField"); addTextField(RotPointXField);
		RotPointYField = new TextFieldWidget(fontRenderer, 310, 130, 50, 20, "RotPointYField"); addTextField(RotPointYField);
		RotPointZField = new TextFieldWidget(fontRenderer, 400, 130, 50, 20, "RotPointZField"); addTextField(RotPointZField);
		
		TextureUField = new TextFieldWidget(fontRenderer, 280, 160, 50, 20, "TextureUField"); addTextField(TextureUField);
		TextureVField = new TextFieldWidget(fontRenderer, 220, 160, 50, 20, "TextureVField"); addTextField(TextureVField);
		
		//setText
		NameField.setText(create.name);
		
		PreScaleXField.setText(String.valueOf(create.PreScaleX));
		PreScaleYField.setText(String.valueOf(create.PreScaleY));
		PreScaleZField.setText(String.valueOf(create.PreScaleZ));
		
		PreXField.setText(String.valueOf(Math.round(create.PreX * 100)));
		PreYField.setText(String.valueOf(Math.round(create.PreY * 100)));
		PreZField.setText(String.valueOf(Math.round(create.PreZ * 100)));
		
		RotPointXField.setText(String.valueOf(Math.round(create.RotPointX * 100)));
		RotPointYField.setText(String.valueOf(Math.round(create.RotPointY * 100)));
		RotPointZField.setText(String.valueOf(Math.round(create.RotPointZ * 100)));
		
		TextureUField.setText(String.valueOf(create.TextureU));
		TextureVField.setText(String.valueOf(create.TextureV));
		//setText
		
		super.LoadTextFields();
	}
	
	@Override
	public void LoadString(int clr) {
		int l = MathHelper.ceil(1.0 * 255.0F) << 24; // 16777215 | l
		int c = 16777215 | l;
		drawString(fontRenderer, "Name", 220, 16, c);
		drawString(fontRenderer, "PreScale", 137,75, c);
		drawString(fontRenderer, "PrePosition", 127, 105, c);
		drawString(fontRenderer, "RotationPoint", 120, 135, c);
		
		drawString(fontRenderer, "Texture Offset", 130, 166, c);
		super.LoadString(clr);
	}
	
	@Override
	public void UpdateTextFields() {
		create.name = NameField.getText();
		create.mesh = Main.GuiSettings.SelectedMesh;
		create.meshID = Main.GuiSettings.SelectedMeshID;
		create.parentID = Main.GuiSettings.SelectedPartID;
		create.parentisLimb = Main.GuiSettings.SelectedPartIsLimb;
		SetPreScale();
		SetPrePos();
		SetRotPoint();
		SetTextureOffset();
		AddObject();
	}
	
	public void SetTextureOffset() {
		if (!(Utillities.isNumeric(TextureUField.getText()) && Utillities.isNumeric(TextureVField.getText()))) return;
		create.TextureU = Integer.valueOf(TextureUField.getText());
		create.TextureV = Integer.valueOf(TextureVField.getText());
	}
	
	public void SetPreScale() {
		if (!(Utillities.isNumeric(PreScaleXField.getText()) && Utillities.isNumeric(PreScaleYField.getText()) && Utillities.isNumeric(PreScaleZField.getText()))) return;
		create.PreScaleX = Integer.valueOf(PreScaleXField.getText());
		create.PreScaleY = Integer.valueOf(PreScaleYField.getText());
		create.PreScaleZ = Integer.valueOf(PreScaleZField.getText());
	}
	
	public void SetPrePos() {
		if (!(Utillities.isNumeric(PreXField.getText()) && Utillities.isNumeric(PreYField.getText()) && Utillities.isNumeric(PreZField.getText()))) return;
		if (PreXField.getText() == "" || PreYField.getText() == "" || PreZField.getText() == "") return;
		create.PreX = Float.valueOf(MinusPreX.getMessage() + PreXField.getText()) / 100;
		create.PreY = Float.valueOf(MinusPreY.getMessage() + PreYField.getText()) / 100;
		create.PreZ = Float.valueOf(MinusPreZ.getMessage() + PreZField.getText()) / 100;
	}
	
	public void SetRotPoint() {
		if (!(Utillities.isNumeric(RotPointXField.getText()) && Utillities.isNumeric(RotPointYField.getText()) && Utillities.isNumeric(RotPointZField.getText()))) return;
		if (RotPointXField.getText() == "" || RotPointYField.getText() == "" || RotPointZField.getText() == "") return;
		create.RotPointX = Float.valueOf(MinusRotPointX.getMessage() + RotPointXField.getText()) / 100;
		create.RotPointY = Float.valueOf(MinusRotPointY.getMessage() + RotPointYField.getText()) / 100;
		create.RotPointZ = Float.valueOf(MinusRotPointZ.getMessage() + RotPointZField.getText()) / 100;
	}
	
	public void AddObject() {
		String typeValue = create.mesh;
		if (firstAdd == true) RemoveObject();
		if (create.name == "")
			return;
		if (Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.ParthasID(create.name)) return;
		if (typeValue == "")
			typeValue = "cube";
		Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.add(new ModelPart(create));
		firstAdd = true;
	}
	public void RemoveObject() {
		Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.removePart(UseableID);
		//if (!(Main.Data.playerData.get(Main.GuiSettings.player).data.parts.Part.size() > UseableID)) return;
		//Main.Data.playerData.get(Main.GuiSettings.player).data.parts.Part.remove(UseableID);
	}
	
	@Override
	public void onClose() {
		if (firstAdd == true) RemoveObject();
		super.onClose();
	}
	
	@Override
	public void UpdateAction() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		super.UpdateAction();
	}
	
}
