package me.PixelDots.PixelsCharacterModels.client.gui;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.CreatePart;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PartCreation.CreatePartGui;
import me.PixelDots.PixelsCharacterModels.client.model.ModelPart;
import me.PixelDots.PixelsCharacterModels.client.model.ModelParts;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class PartsGui extends HandlerGui
{
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	
	Button CreatePart;
	Button RemovePart;
	Button DeselectPart;
	
	boolean hideItems = false;
	CreatePart create = null;
	public TranslationText Translation = new TranslationText();
	
	public PartsGui() {
		super(256, 256, "Presets", "textures/gui/presets.png");
	}
	
	public PartsGui(CreatePart createT, boolean build) {
		super(256, 256, "Presets", "textures/gui/presets.png");
		if (build) AddObject(createT);
		else create = createT;
	}
	
	public PartsGui(int unused) {
		this();
		hideItems = true;
	}
	
	public PartsGui(boolean unused) {
		this();
		hideItems = unused;
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
		Presets = addButton(new Button(10, 10, 50, 20, "Presets", (value) -> {
			minecraft.displayGuiScreen(new PresetsGui());
		})); Presets.active = true;
		Editor = addButton(new Button(60, 10, 50, 20, "Editor", (value) -> {
			minecraft.displayGuiScreen(new EditorGui());
		})); Editor.active = true;
		Animation = addButton(new Button(10, 30, 50, 20, "Animation", (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); Animation.active = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
		
		CreatePart = addButton(new Button(10, 60, 50, 20, "Add", (value) -> {
			Minecraft.getInstance().displayGuiScreen(new CreatePartGui());
		}));
		RemovePart = addButton(new Button(10, 85, 50, 20, "Remove", (value) -> {
			RemoveObject();
		}));
		DeselectPart = addButton(new Button(10, 140, 50, 20, "Deselect", (value) -> {
			Main.GuiSettings.SelectedPartID = -1;
			Main.GuiSettings.SelectedPartIsLimb = true;
			Main.GuiSettings.SelectedPart = "";
		}));
		
		ModelParts parts = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts;
		int Full = parts.Limb.size() + parts.Part.size();
		int row = 0;
		int col = 0;
		for (int i = 0; i < Full; i++) {
			int num = i;
			String BTNName = (i <= parts.Limb.size() - 1 ? parts.Limb.get(i).name : parts.Part.get(i - parts.Limb.size()).name);
			Button b = new Button(130 + (60*col), (15*((row + 1) + row) + 5),50,20, BTNName, (value) -> {
				Main.GuiSettings.SelectedPartID = num <= parts.Limb.size() - 1 ? num : num - parts.Limb.size();
				Main.GuiSettings.SelectedPart = num <= parts.Limb.size() - 1 ? parts.Limb.get(num).name : parts.Part.get(num - parts.Limb.size()).name;
				Main.GuiSettings.SelectedPartIsLimb = num <= parts.Limb.size() - 1 ? true : false;
				if (create == null) Minecraft.getInstance().displayGuiScreen(new PartsGui());
				else Minecraft.getInstance().displayGuiScreen(new CreatePartGui(create));
			});
			if (Main.GuiSettings.SelectedPartID == i && Main.GuiSettings.SelectedPartIsLimb == true) b.active = false;
			else if (Main.GuiSettings.SelectedPartID == i - parts.Limb.size() && Main.GuiSettings.SelectedPartIsLimb == false) b.active = false;
			else if (hideItems == true && Main.GuiSettings.SelectedPart.equalsIgnoreCase("ItemRight")) b.active = false;
			else if (hideItems == true && Main.GuiSettings.SelectedPart.equalsIgnoreCase("ItemLeft")) b.active = false;
			addButton(b);
			row++;
			if (row >= 11) {
				col++;
				row = 0;
			}
		}
	}
	
	@Override
	public void LoadTextFields() {
		super.LoadTextFields();
	}
	
	@Override
	public void LoadString(int clr) {
		super.LoadString(clr);
	}
	
	public void AddObject(CreatePart createT) {
		String typeValue = createT.mesh;
		if (createT.name == "")
			return;
		if (typeValue == "")
			typeValue = "cube";
		if (Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.ParthasID(createT.name)) return;
		Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.parts.Part.add(new ModelPart(createT));
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		Minecraft.getInstance().displayGuiScreen(new EditorGui());
	}
	public void RemoveObject() {
		if (Main.GuiSettings.SelectedPartID == -1 || Main.GuiSettings.SelectedPartIsLimb) 
			return;
		@SuppressWarnings("unused")
		GlobalModelData data = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data;
		int removeID = Main.GuiSettings.SelectedPartID;
		Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.removePart(removeID);
		Main.GuiSettings.SelectedPart = "";
		Main.GuiSettings.SelectedPartID = -1;
		Main.GuiSettings.SelectedPartIsLimb = true;
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		Minecraft.getInstance().displayGuiScreen(new PartsGui(hideItems));
	}
	
	@Override
	public void onClose() {
		if (Main.GuiSettings.SelectedPresetID != -1) Main.presets.SavePreset(Main.GuiSettings.SelectedPresetID);
		super.onClose();
	}
	
	@Override
	public void UpdateAction() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		super.UpdateAction();
	}
	
}
