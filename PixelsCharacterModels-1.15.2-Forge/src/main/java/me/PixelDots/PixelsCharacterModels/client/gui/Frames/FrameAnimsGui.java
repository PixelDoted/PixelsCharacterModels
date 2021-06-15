package me.PixelDots.PixelsCharacterModels.client.gui.Frames;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Frames.Frames;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import net.minecraft.client.Minecraft;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.util.text.StringTextComponent;

public class FrameAnimsGui extends HandlerGui
{
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	
	Button Add;
	Button Remove;
	
	private TranslationText Translation = new TranslationText();
	
	public FrameAnimsGui() {
		super(0,0, "Frame", "textures/gui/presets.png");
	}
	
	public FrameAnimsGui(boolean create) {
		this();
		AddAnim();
	}
	
	@Override
	public void init() {
		
		UpdateBTNs();
		
		super.init();
	}
	
	public void UpdateBTNs() {
		Presets = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new PresetsGui());
		}) ); Presets.field_230693_o_ = true;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new EditorGui());
		}) ); Editor.field_230693_o_ = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new AnimationGui());
		}) ); Animation.field_230693_o_ = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.field_230693_o_ = true;
		
		Add = addButton(new Button(10, 110, 50, 20, Translation.Add, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new AnimationsGui(true));
		}));
		Remove = addButton(new Button(10, 140, 50, 20, Translation.Remove, (value) -> {
			RemoveAnim();
		}));
		
		if (Main.GuiSettings.SelectedFrameID != -1) {
			Frames frame =  Main.frames.list.get(Main.GuiSettings.SelectedFrameID);
			int row = 0;
			int col = 0;
			for (int i = 0; i < frame.animNames.size(); i++) {
				int num = i;
				Button b = new Button(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, new StringTextComponent(frame.animNames.get(i)), (value) -> {
					Main.GuiSettings.SelectedAnimationID = Main.animations.getAnimIDfromName(frame.animNames.get(num));
					Minecraft.getInstance().displayGuiScreen(new FrameAnimsGui());
				});
				if (Main.GuiSettings.SelectedAnimationID == Main.animations.getAnimIDfromName(frame.animNames.get(i))) b.field_230693_o_ = false;
				addButton(b);
				row++;
				if (row >= 11) {
					col++;
					row = 0;
				}
			}
		}
	}
	
	@Override
	public void onClose() {
		runUpdate();
		super.onClose();
	}
	
	public void runUpdate() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
	}
	
	public void AddAnim() {
		if (Main.GuiSettings.SelectedFrameID == -1) return;
		Frames frame = Main.frames.list.get(Main.GuiSettings.SelectedFrameID);
		frame.animNames.add(Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).id);
		frame.anims.put(Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).id, Main.animations.list.get(Main.GuiSettings.SelectedAnimationID));
		Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
	}
	
	public void RemoveAnim() {
		if (Main.GuiSettings.SelectedFrameID == -1 || Main.GuiSettings.SelectedAnimationID == -1) return;
		Frames frame = Main.frames.list.get(Main.GuiSettings.SelectedFrameID);
		int ID = -1;
		for (int i = 0; i < frame.animNames.size(); i++) {
			if (frame.animNames.get(i).equalsIgnoreCase(Main.animations.list.get(Main.GuiSettings.SelectedAnimationID).id)) {
				ID = i;
				break;
			}
		}
		if (ID == -1) return;
		frame.anims.remove(frame.animNames.get(ID));
		frame.animNames.remove(ID);
		Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
		Minecraft.getInstance().displayGuiScreen(new FrameAnimsGui());
	}
	
}