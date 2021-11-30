package me.PixelDots.PixelsCharacterModels.client.gui.Frames;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.SettingsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import me.PixelDots.PixelsCharacterModels.util.Utillities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.util.text.StringTextComponent;

public class FrameGui extends HandlerGui
{
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	Button Settings;
	
	Button Frames;
	Button Linear;
	TextFieldWidget Time;
	Button FrameAnims;
	Button Loop;
	Button FrameReset;
	
	private TranslationText Translation = new TranslationText();
	
	public FrameGui() {
		super(0,0, "Frame", "textures/gui/presets.png");
	}
	
	@Override
	public void init() {
		
		UpdateBTNs();
		Time = new TextFieldWidget(fontRenderer, 310,30,50,20, new StringTextComponent("Time")); addTextField(Time);
		if(Main.GuiSettings.SelectedFrameID != -1) { 
			float t = Main.frames.list.get(Main.GuiSettings.SelectedFrameID).time;
			Time.setText(
				String.valueOf( Math.round( (1 - t * 2) * 100 ) ));
		}
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
		}) ); Frame.field_230693_o_ = false;
		
		Frames = addButton(new Button(130, 30, 50, 20, Translation.Frames, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FramesGui());
		}) );
		FrameAnims = addButton(new Button(190,30,50,20, Translation.Animations, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameAnimsGui());
		}) );
		Linear = addButton(new Button(250,30,50,20, Translation.Linear, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Linear)) value.setMessage(Translation.Linear);//Translation.Curve
			else value.setMessage(Translation.Linear);
			if (Main.GuiSettings.SelectedFrameID != -1) {
				Main.frames.list.get(Main.GuiSettings.SelectedFrameID).Linear = value.getMessage().equalsIgnoreCase(Translation.Linear) ? true : false;
				Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
			}
		}) );
		Loop = addButton(new Button(370,30,50,20, Translation.FALSE, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.FALSE)) value.setMessage(Translation.TRUE);
			else value.setMessage(Translation.FALSE);
			if (Main.GuiSettings.SelectedFrameID != -1) {
				Main.frames.list.get(Main.GuiSettings.SelectedFrameID).Loop = value.getMessage().equalsIgnoreCase(Translation.FALSE) ? false : true;
				Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
			}
		}) );
		FrameReset = addButton(new Button(425, 30, 50, 20, Translation.TRUE, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.FALSE)) value.setMessage(Translation.TRUE);
			else value.setMessage(Translation.FALSE);
			if (Main.GuiSettings.SelectedFrameID != -1) {
				Main.frames.list.get(Main.GuiSettings.SelectedFrameID).resetnextframe = value.getMessage().equalsIgnoreCase(Translation.FALSE) ? false : true;
				Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
			}
		}));
		if (Main.GuiSettings.SelectedFrameID != -1)
			FrameReset.setMessage(new StringTextComponent(String.valueOf( Main.frames.list.get(Main.GuiSettings.SelectedFrameID).resetnextframe )));
		
		Settings = addButton(new Button(10, 60, 50, 20, Translation.Settings, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new SettingsGui());
		}));
		
		if (Main.GuiSettings.SelectedFrameID != -1) {
			Linear.setMessage((Main.frames.list.get(Main.GuiSettings.SelectedFrameID).Linear ? Translation.Linear : Translation.Curve));
			Loop.setMessage((Main.frames.list.get(Main.GuiSettings.SelectedFrameID).Loop ? Translation.TRUE : Translation.FALSE));
		}
	}
	
	@Override
	public void drawText(MatrixStack stack, int clr) {
		String TimeText = "Time";
		if (Main.GuiSettings.SelectedFrameID != -1) {
			float t = Main.frames.list.get(Main.GuiSettings.SelectedFrameID).time;
			float v = ((1 - t) * 2) * 100;
			float round = Math.round(v - 100); float Final = round/100;
			TimeText = String.valueOf( Final ) + " s";
		}
		drawString(stack, fontRenderer, TimeText, 310, 70, clr);
		drawString(stack, fontRenderer, Translation.Loop, 370, 70, clr);
		drawString(stack, fontRenderer, "Reset old frame on next frame", 425, 10, clr);
	}
	
	@Override
	public void UpdateTextFields() {
		if (Utillities.isNumeric(Time.getText())) {
			if (Main.GuiSettings.SelectedFrameID != -1) {
				Main.frames.list.get(Main.GuiSettings.SelectedFrameID).time =  1 - ((Float.parseFloat(Time.getText()) / 2 + 50) / 100);
				Main.frames.SaveFrames(Main.GuiSettings.SelectedFrameID);
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
	
}