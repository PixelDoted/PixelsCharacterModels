package me.PixelDots.PixelsCharacterModels.client.gui.Frames;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Frames.Frames;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;

public class FramesGui extends HandlerGui
{
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	Button Add;
	Button Remove;
	TextFieldWidget textBox;
	
	private TranslationText Translation = new TranslationText();
	
	public FramesGui() {
		super(0,0, "Frames", "textures/gui/presets.png");
	}
	
	@Override
	public void init() {
		
		UpdateBTNs();
		textBox = new TextFieldWidget(fontRenderer, 10, 80, 50, 20, "name"); addTextField(textBox);
		super.init();
	}
	
	public void UpdateBTNs() {
		Presets = addButton(new Button(10, 10, 50, 20, Translation.Presets, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new PresetsGui());
		}) ); Presets.active = true;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new EditorGui());
		}) ); Editor.active = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new AnimationGui());
		}) ); Animation.active = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
		
		Add = addButton(new Button(10, 110, 50, 20, Translation.Add, (value) -> {
			AddFrame();
		}) );
		Remove = addButton(new Button(10, 140, 50, 20, Translation.Remove, (value) -> {
			RemoveFrame();
		}) );
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < Main.frames.list.size(); i++) {
			int num = i;
			Button b = new Button(120 + 10 + (60*col), (15*((row + 1) + row) + 5), 50, 20, Main.frames.list.get(i).id, (value) -> {
				Main.GuiSettings.SelectedFrameID = num;
				Minecraft.getInstance().displayGuiScreen(new FramesGui());
			});
			if (Main.GuiSettings.SelectedFrameID != -1) if (i == Main.GuiSettings.SelectedFrameID) b.active = false;
			addButton( b );
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
		}
	}
	
	public void AddFrame() {
		if (textBox.getText() == "") return;
		for (int i = 0; i < Main.frames.list.size(); i++) {
			if (Main.frames.list.get(i).id.equalsIgnoreCase(textBox.getText())) return;
		}
		Frames frames = new Frames();
		frames.id = textBox.getText();
		Main.frames.list.add(frames);
		Minecraft.getInstance().displayGuiScreen(new FramesGui());
	}
	
	public void RemoveFrame() {
		if (Main.GuiSettings.SelectedFrameID == -1) return;
		Main.frames.removeFrames(Main.GuiSettings.SelectedFrameID);
		Main.GuiSettings.SelectedFrameID = -1;
		Minecraft.getInstance().displayGuiScreen(new FramesGui());
	}
	
	@Override
	public void onClose() {
		runUpdate();
		super.onClose();
	}
	
	public void runUpdate() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
	}
	
}