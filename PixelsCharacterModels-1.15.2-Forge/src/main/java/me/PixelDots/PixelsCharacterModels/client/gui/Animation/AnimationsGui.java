package me.PixelDots.PixelsCharacterModels.client.gui.Animation;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.PixelDots.PixelsCharacterModels.Main;
import me.PixelDots.PixelsCharacterModels.Model.GlobalModelManager;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;
import me.PixelDots.PixelsCharacterModels.client.gui.EditorGui;
import me.PixelDots.PixelsCharacterModels.client.gui.PresetsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameAnimsGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Frames.FrameGui;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.HandlerGui;
import me.PixelDots.PixelsCharacterModels.util.TranslationText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets.Button;
import net.minecraft.util.text.StringTextComponent;

public class AnimationsGui extends HandlerGui
{
	
	FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
	
	TextFieldWidget AnimName;
	
	Button Presets;
	Button Editor;
	Button Animation;
	Button Frame;
	Button CreateBTN;
	Button RemoveBTN;
	
	private TranslationText Translation = new TranslationText();
	
	private boolean isFrame = false;
	
	public AnimationsGui() {
		super(256, 256, "Presets", "textures/gui/presets.png");
	}
	
	public AnimationsGui(boolean isFrame) {
		super(256, 256, "Presets", "textures/gui/presets.png");
		this.isFrame = isFrame;
	}
	
	@Override
	public void init() {
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
		CreateBTN = addButton(new Button(60, 85, 50, 20, Translation.Create, (value) -> {
			CreateAnimation();
		}));
		RemoveBTN = addButton(new Button(60, 110, 50, 20, Translation.Remove, (value) -> {
			RemoveAnimation();
		}));
		int row = 0;
		int col = 0;
		for (int i = 0; i < Main.animations.list.size(); i++) {
			int num = i;
			Button b = new Button(130 + (60*col), (15*((row + 1) + row) + 5), 50, 20, new StringTextComponent(Main.animations.list.get(i).id), (value) -> {
				Main.GuiSettings.SelectedAnimationID = num;
				if (isFrame) {
					Minecraft.getInstance().displayGuiScreen(new FrameAnimsGui(true));
				} else {
					Minecraft.getInstance().displayGuiScreen(new AnimationsGui());
				}
			});
			if (Main.GuiSettings.SelectedAnimationID == i && isFrame == false) b.field_230693_o_ = false;
			addButton(b);
			row++;
			if (row >= 11) {
				row = 0;
				col++;
			}
			
		}
	}
	
	@Override
	public void LoadTextFields() {
		AnimName = new TextFieldWidget(minecraft.fontRenderer, 60, 60, 50, 20, new StringTextComponent("AnimName")); addTextField(AnimName);
		super.LoadTextFields();
	}
	
	@Override
	public void UpdateTextFields() {
		super.UpdateTextFields();
	}
	
	@Override
	public void LoadString(MatrixStack stack, int clr) {
		super.LoadString(stack, clr);
	}
	
	public void CreateAnimation() {
		for (int i = 0; i < Main.animations.list.size(); i++) {
			if (Main.animations.list.get(i).id.toLowerCase().startsWith(AnimName.getText().toLowerCase())) {
				return;
			}
		}
		if (AnimName.getText() == "") return;
		Animation anim = new Animation();
		anim.id = AnimName.getText().toLowerCase();
		Main.animations.addAnim(anim);
		Main.GuiSettings.SelectedAnimationID = Main.animations.getAnimIDfromName(anim.id);
		Minecraft.getInstance().displayGuiScreen(new AnimationGui());
	}
	
	public void RemoveAnimation() {
		if (Main.GuiSettings.SelectedAnimationID == -1) return;
		Main.animations.removeAnim(Main.GuiSettings.SelectedAnimationID);
		Main.GuiSettings.SelectedAnimationID = -1;
		Minecraft.getInstance().displayGuiScreen(new AnimationGui());
	}
	
	@Override
	public void UpdateAction() {
		GlobalModelManager.Model.setModel(Main.GuiSettings.player, Main.Data.playerData.get(Main.GuiSettings.player.getUniqueID()).data);
		super.UpdateAction();
	}
	
}
