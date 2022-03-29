package me.PixelDots.PixelsCharacterModels.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.client.gui.Animation.AnimationGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class SettingsGui extends HandlerGui {
	
	public GuiSettings settings = new GuiSettings();
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	
	TextFieldWidget AnimKey1; Button AnimFrame1;
	TextFieldWidget AnimKey2; Button AnimFrame2;
	TextFieldWidget AnimKey3; Button AnimFrame3;
	TextFieldWidget AnimKey4; Button AnimFrame4;
	TextFieldWidget AnimKey5; Button AnimFrame5;
	Button Sliders;
	TextFieldWidget URLLength; Slider URLLengthS;
	Button showNameTags;
	Button showLoadingDatainChat;
	
	private TranslationText Translation = new TranslationText();
	
	public SettingsGui() {
		super(1, 1, "Settings", "textures/gui/presets.png");
	}
	
	@Override
	public void init() {
		
		UpdateBTNs();
		
		AnimKey1 = new TextFieldWidget(fontRenderer, 60, 60, 50, 20, new StringTextComponent("1")); addTextField(AnimKey1);
		AnimKey2 = new TextFieldWidget(fontRenderer, 60, 80, 50, 20, new StringTextComponent("2")); addTextField(AnimKey2);
		AnimKey3 = new TextFieldWidget(fontRenderer, 60, 100, 50, 20, new StringTextComponent("3")); addTextField(AnimKey3);
		AnimKey4 = new TextFieldWidget(fontRenderer, 60, 120, 50, 20, new StringTextComponent("4")); addTextField(AnimKey4);
		AnimKey5 = new TextFieldWidget(fontRenderer, 60, 140, 50, 20, new StringTextComponent("5")); addTextField(AnimKey5);
		
		/*if (Sliders.getMessage() == Translation.Sliders) {
			URLLengthS = new Slider(60, 190, 50, 20, "", "", 1, 1000, Main.OtherSaveData.maxURLlength, false, true, null, (value) -> {
				Main.OtherSaveData.maxURLlength = value.getValueInt();//Math.round( Utillities.getSliderValue(value, false) );
			}); addSlider(URLLengthS);
		} else {*/
			URLLength = new TextFieldWidget(fontRenderer, 60, 190, 50, 20, new StringTextComponent("URLLength")); addTextField(URLLength);
		//}
		
		AnimKey1.setText(Main.animations.AnimKey1);
		AnimKey2.setText(Main.animations.AnimKey2);
		AnimKey3.setText(Main.animations.AnimKey3);
		AnimKey4.setText(Main.animations.AnimKey4);
		AnimKey5.setText(Main.animations.AnimKey5);
		
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
		
		AnimFrame1 = addButton(new Button(120, 60, 50, 20, Translation.Animation, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Animation)) value.setMessage(Translation.Frame);
			else value.setMessage(Translation.Animation);
			Main.animations.Anim1isFrames = value.getMessage().equalsIgnoreCase(Translation.Animation) ? false : true;
			SaveData();
		}));
		AnimFrame2 = addButton(new Button(120, 80, 50, 20, Translation.Animation, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Animation)) value.setMessage(Translation.Frame);
			else value.setMessage(Translation.Animation);
			Main.animations.Anim2isFrames = value.getMessage().equalsIgnoreCase(Translation.Animation) ? false : true;
			SaveData();
		}));
		AnimFrame3 = addButton(new Button(120, 100, 50, 20, Translation.Animation, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Animation)) value.setMessage(Translation.Frame);
			else value.setMessage(Translation.Animation);
			Main.animations.Anim3isFrames = value.getMessage().equalsIgnoreCase(Translation.Animation) ? false : true;
			SaveData();
		}));
		AnimFrame4 = addButton(new Button(120, 120, 50, 20, Translation.Animation, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Animation)) value.setMessage(Translation.Frame);
			else value.setMessage(Translation.Animation);
			Main.animations.Anim4isFrames = value.getMessage().equalsIgnoreCase(Translation.Animation) ? false : true;
			SaveData();
		}));
		AnimFrame5 = addButton(new Button(120, 140, 50, 20, Translation.Animation, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Animation)) value.setMessage(Translation.Frame);
			else value.setMessage(Translation.Animation);
			Main.animations.Anim5isFrames = value.getMessage().equalsIgnoreCase(Translation.Animation) ? false : true;
			SaveData();
		}));
		
		Sliders = addButton(new Button(60, 165, 50, 20, Translation.Sliders, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.Sliders)) value.setMessage(Translation.TextFields);
			else value.setMessage(Translation.Sliders);
			if (value.getMessage().equalsIgnoreCase(Translation.Sliders)) Main.OtherSaveData.sliders = true;
			else Main.OtherSaveData.sliders = false;
		})); Sliders.field_230693_o_ = false;
		showNameTags = addButton(new Button(fontRenderer.getStringWidth("show NameTags") + 60, 215, 50, 20, Translation.TRUE, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.FALSE)) value.setMessage(Translation.TRUE);
			else value.setMessage(Translation.FALSE);
			if (value.getMessage().equalsIgnoreCase(Translation.TRUE)) Main.OtherSaveData.showNameTags = true;
			else Main.OtherSaveData.showNameTags = false;
		}));
		showLoadingDatainChat = addButton(new Button(fontRenderer.getStringWidth("show Loading Data in Chat") + 60, 240, 50, 20, Translation.TRUE, (value) -> {
			if (value.getMessage().equalsIgnoreCase(Translation.FALSE)) value.setMessage(Translation.TRUE);
			else value.setMessage(Translation.FALSE);
			if (value.getMessage().equalsIgnoreCase(Translation.TRUE)) Main.OtherSaveData.showLoadingDatainChat = true;
			else Main.OtherSaveData.showLoadingDatainChat = false;
		}));
		
		AnimFrame1.setMessage(Main.animations.Anim1isFrames ? Translation.Frames : Translation.Animation);
		AnimFrame2.setMessage(Main.animations.Anim2isFrames ? Translation.Frames : Translation.Animation);
		AnimFrame3.setMessage(Main.animations.Anim3isFrames ? Translation.Frames : Translation.Animation);
		AnimFrame4.setMessage(Main.animations.Anim4isFrames ? Translation.Frames : Translation.Animation);
		AnimFrame5.setMessage(Main.animations.Anim5isFrames ? Translation.Frames : Translation.Animation);
		Sliders.setMessage(Main.OtherSaveData.sliders ? Translation.Sliders : Translation.TextFields);
		showNameTags.setMessage(Main.OtherSaveData.showNameTags ? Translation.TRUE : Translation.FALSE);
		showLoadingDatainChat.setMessage(Main.OtherSaveData.showLoadingDatainChat ? Translation.TRUE : Translation.FALSE);
	}
	
	@Override
	public void drawText(MatrixStack stack, int clr) {
		drawString(stack, fontRenderer, Translation.animkey1, 10, 67, clr);
		drawString(stack, fontRenderer, Translation.animkey2, 10, 87, clr);
		drawString(stack, fontRenderer, Translation.animkey3, 10, 107, clr);
		drawString(stack, fontRenderer, Translation.animkey4, 10, 127, clr);
		drawString(stack, fontRenderer, Translation.animkey5, 10, 147, clr);
		drawString(stack, fontRenderer, Translation.URL + " " + Translation.Length, 10, 197, clr);
		drawString(stack, fontRenderer, "show NameTags", 60, 215, clr);
		drawString(stack, fontRenderer, "show Loading Data in Chat", 60, 240, clr);
	}
	
	@Override
	public void UpdateTextFields() {
		Main.animations.AnimKey1 = AnimKey1.getText().toLowerCase();
		Main.animations.AnimKey2 = AnimKey2.getText().toLowerCase();
		Main.animations.AnimKey3 = AnimKey3.getText().toLowerCase();
		Main.animations.AnimKey4 = AnimKey4.getText().toLowerCase();
		Main.animations.AnimKey5 = AnimKey5.getText().toLowerCase();
		SaveData();
	}
	
	@Override
	public void onClose() {
		SaveData();
		super.onClose();
	}
	
	public void SaveData() {
		Main.OtherSaveData.Save();
	}

}