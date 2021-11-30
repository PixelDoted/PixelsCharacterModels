package me.PixelDots.PixelsCharacterModels.client.gui.Animation;

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
import net.minecraft.client.gui.widget.button.Button;

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
			minecraft.displayGuiScreen(new PresetsGui());
		})); Presets.active = false;
		Editor = addButton(new Button(60, 10, 50, 20, Translation.Editor, (value) -> {
			minecraft.displayGuiScreen(new EditorGui());
		})); Editor.active = true;
		Animation = addButton(new Button(10, 30, 50, 20, Translation.Animation, (value) -> {
			minecraft.displayGuiScreen(new AnimationGui());
		})); Animation.active = true;
		Frame = addButton(new Button(60, 30, 50, 20, Translation.Frame, (value) -> {
			Minecraft.getInstance().displayGuiScreen(new FrameGui());
		}) ); Frame.active = true;
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
			Button b = new Button(130 + (60*col), (15*((row + 1) + row) + 5), 50, 20, Main.animations.list.get(i).id, (value) -> {
				Main.GuiSettings.SelectedAnimationID = num;
				if (isFrame) {
					Minecraft.getInstance().displayGuiScreen(new FrameAnimsGui(true));
				} else {
					Minecraft.getInstance().displayGuiScreen(new AnimationsGui());
				}
			});
			if (Main.GuiSettings.SelectedAnimationID == i && isFrame == false) b.active = false;
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
		AnimName = new TextFieldWidget(minecraft.fontRenderer, 60, 60, 50, 20, "AnimName"); addTextField(AnimName);
		super.LoadTextFields();
	}
	
	@Override
	public void UpdateTextFields() {
		super.UpdateTextFields();
	}
	
	@Override
	public void LoadString(int clr) {
		super.LoadString(clr);
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
