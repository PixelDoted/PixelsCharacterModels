package me.PixelDots.PixelsCharacterModels.client.gui;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.*;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.ErrorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.Reference;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.edoren.skin_changer.server.SkinsCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class PresetsGui extends HandlerGui
{
	
	@SuppressWarnings("unused")
	private Logger LOGGER = LogManager.getLogger();
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	TextFieldWidget PresetName;
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	
	Button Settings;
	
	Button UpdateBTN;
	Button AddBTN;
	Button RemoveBTN;
	
	int mouseX = 0;
	int mouseY = 0;
	
	private TranslationText Translation = new TranslationText();
	
	public PresetsGui() {
		super(256, 256, "Presets", "textures/gui/presets.png");
	}
	
	@Override
	public void init() {
		if (Main.GuiSettings.player == null) {
			minecraft.displayGuiScreen(new ErrorGui("Player not found, try reopening the menu", "0a"));
		} else {
			if (!Main.Data.playerData.containsKey(Main.GuiSettings.player.getUniqueID())) {
				minecraft.displayGuiScreen(new ErrorGui("data was not found, try reopening the menu", "1a"));
			}
		}
		super.init();
	}
	
	@Override
	public void LoadButtons() {
		Presets = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			minecraft.displayGuiScreen(new PresetsGui());
		})); Presets.field_230693_o_ = false;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			minecraft.displayGuiScreen(new EditorGui());
		})); Editor.field_230693_o_ = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); Animation.field_230693_o_ = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.field_230693_o_ = true;
		AddBTN = addButton(new Button(10, 110, 50, 20, Translation.Add, (value) -> {
			Main.GuiSettings.SelectedPresetID = -1;
			SetPreset();
			
		}));
		RemoveBTN = addButton(new Button(10, 140, 50, 20, Translation.Remove, (value) -> {
			if (Main.GuiSettings.SelectedPresetID != -1)
				Main.presets.RemovePreset(Main.GuiSettings.SelectedPresetID);
			Main.GuiSettings.SelectedPresetID = -1;
			minecraft.displayGuiScreen(new PresetsGui());
		}));
		Settings = addButton(new Button(10, 160, 50, 20, Translation.Settings, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new SettingsGui());
		}));
		if (Main.updateTracker.hasUpdate()) {
			UpdateBTN = addButton(new Button(625, 10, 50, 20, Translation.Update, (value) -> {
				ConfirmScreen con = new ConfirmScreen(null, new StringTextComponent("PCM Update Tracker"), new StringTextComponent("Would you like to go to curseforge to download PCM " + Main.updateTracker.getVersion()));
				minecraft.displayGuiScreen(con);
			}));
		}
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < Main.presets.list.size(); i++) { //6 per col, max 24
			int num = i;
			Button b = addButton( new Button(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, new StringTextComponent(Main.presets.list.get(i).name), (value) -> {
				SelectPreset(num);
			}));
			if (Main.GuiSettings.SelectedPresetID != -1) if (i == Main.GuiSettings.SelectedPresetID) b.field_230693_o_ = false;
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
		}
	}
	
	public void SetPreset() {
		int ID = Main.GuiSettings.SelectedPresetID;
		if (ID != -1) {
			Main.presets.SetPreset(ID, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
			Main.presets.LoadPresets();
			Minecraft.getInstance().displayGuiScreen(new PresetsGui());
		}
		else if (PresetName.getText() != "") {
			Main.presets.CreatePreset(Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data, PresetName.getText());
			Main.GuiSettings.SelectedPresetID = Main.presets.list.size() - 1;
			Minecraft.getInstance().displayGuiScreen(new PresetsGui());
		}
	}
	
	@Override
	public void LoadTextFields() {
		PresetName = new TextFieldWidget(minecraft.fontRenderer, 10, 80, 50, 20, new StringTextComponent("PresetName")); addTextField(PresetName);
		super.LoadTextFields();
	}
	
	@Override
	public void mouseMoved(double xPos, double mouseY) {
		super.mouseMoved(xPos, mouseY);
	}
	
	@Override
	public void LoadString(MatrixStack stack, int clr) {
		drawString(stack, fontRenderer, Translation.Version + ": " + Reference.VERSION, 10, 357, clr);
		if (Main.updateTracker.hasUpdate()) {
			drawString(stack, fontRenderer, Translation.Version + ": " + Main.updateTracker.getVersion(), 625, 10, clr);
		}
		super.LoadString(stack, clr);
	}
	
	@Override
	public void UpdateAction() {
		if (Main.GuiSettings.player != null) {
			if (Main.Data.playerData.containsKey(Main.GuiSettings.player.getUniqueID())) {
				GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
			}
		}
		super.UpdateAction();
	}
	
	public void SelectPreset(int ID) {
		Main.GuiSettings.SelectedPresetID = ID;
		UsePreset();
		Minecraft.getInstance().displayGuiScreen(new PresetsGui());
	}
	
	public void UsePreset() {
		if (Main.GuiSettings.player != null) {
			if (Main.Data.playerData.containsKey(Main.GuiSettings.player.getUniqueID())) {
				Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data = Main.presets.list.get(Main.GuiSettings.SelectedPresetID).data;
				Main.presets.lockedin = Main.GuiSettings.SelectedPresetID; 
				PlayerEntity player = Main.GuiSettings.player;
				if (Main.OtherSaveData.showLoadingDatainChat) player.sendMessage(new StringTextComponent("Loading preset..."), player.getUniqueID());
				/* Skin Texture */
				String skin = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.ClientMemorySkinTexture;
				/*GlobalModelManager.Skin.*/setSkin(skin);
				/* Elytra Texture */
				String elytra = Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data.ClientMemoryElytraTexture;
				/*GlobalModelManager.Elytra.*/setElytra(elytra);
				String LoadedTXT = "";
				if (Main.Data.playerData.get(player.getUniqueID()).data == Main.presets.list.get(Main.GuiSettings.SelectedPresetID).data) 
				{ LoadedTXT = "Loaded preset..."; }
				else { LoadedTXT = "Failed to load preset..."; }
				if (Main.OtherSaveData.showLoadingDatainChat) {
					player.sendMessage(new StringTextComponent(LoadedTXT), player.getUniqueID());
				}
				Main.GuiSettings.SelectedPartID = -1;
				Main.GuiSettings.SelectedPart = "";
				Main.GuiSettings.SelectedPartIsLimb = false;
				UpdateAction();
			}
		}
	}
	
	public void setElytra(String url) {
		if (url == "") return;
		try {
			SkinsCommand.setPlayerCapeByURL(Main.GuiSettings.player, Main.GuiSettings.player, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}   
	}
	
	public void setSkin(String url) {
		if (url == "") return;
		try {
			SkinsCommand.setPlayerSkinByURL(Main.GuiSettings.player, Main.GuiSettings.player, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
