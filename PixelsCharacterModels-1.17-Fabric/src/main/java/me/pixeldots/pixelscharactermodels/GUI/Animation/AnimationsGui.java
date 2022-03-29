package me.pixeldots.pixelscharactermodels.GUI.Animation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.GUI.Editor.EditorGui;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class AnimationsGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget CreateAnimation;
	public TextFieldWidget AnimationName;
	
	public List<ButtonWidget> Animations = new ArrayList<ButtonWidget>();
	public GuiHandler lastGui;
	public String returnValue = "";
	
	public int maxCol = 10;
	public int maxRow = 5;
	
	public AnimationsGui() {
		super("Animations");
	}
	
	public AnimationsGui(GuiHandler c, String value) {
		this();
		lastGui = c;
		this.returnValue = value;
	}
	
	public AnimationsGui(GuiHandler c) {
		this();
		lastGui = c;
	}
	
	@Override
	public void init() {
		super.init();
		Presets = addButton(new ButtonWidget(5,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Presets), (button) -> {
			PixelsCharacterModels.client.openScreen(new PresetsGui());
		}));
		Editor = addButton(new ButtonWidget(60,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Editor), (button) -> {
			PixelsCharacterModels.client.openScreen(new EditorGui());
		}));
		Animation = addButton(new ButtonWidget(5,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Animation), (button) -> {
			PixelsCharacterModels.client.openScreen(new AnimationGui());
		}));
		Frames = addButton(new ButtonWidget(60,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Frames), (button) -> {
			PixelsCharacterModels.client.openScreen(new FramesGui());
		}));
		
		CreateAnimation = addButton(new ButtonWidget(120,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
			if (AnimationName.getText().replace(" ", "") == "") PixelsCharacterModels.client.minecraft.player.sendMessage(Text.of(PixelsCharacterModels.TranslatedText.setAnimName), false);
			else {
				PixelsCharacterModels.client.currentStoredAnimation = new PCMAnimation(AnimationName.getText());
				PixelsCharacterModels.client.writeAnimation(AnimationName.getText(), PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
				PixelsCharacterModels.GuiData.SelectedAnimation = AnimationName.getText();
			}
		}));
		AnimationName = addTextField(new TextFieldWidget(textRendererGUI, 175, 30, 50, 20, Text.of("name")));
		
		int Col = 1;
		int Row = 1;
		File[] Anims = PixelsCharacterModels.AnimationsData.getAnimations();
		for (int i = 0; i < Anims.length; i++) {
			String key = (String) Anims[i].getName().replace(".json", "");
			Animations.add(addButton(new ButtonWidget(200+(Row*55), 50+(Col*25), 50, 20, Text.of(key), (button) -> {
				button.active = false;
				SelectAnimation(key);
			})));
			Col++;
			if (Col > maxCol) {
				Row++;
				Col = 0;
			}
			if (Row > maxRow) break;
		}
		
		for (int i = 0; i < Animations.size(); i++) {
			if (Animations.get(i).getMessage().asString() == PixelsCharacterModels.GuiData.SelectedAnimation)
			{Animations.get(i).active = false; break;}
		}
	}
	
	public void SelectAnimation(String name) {
		PixelsCharacterModels.GuiData.SelectedAnimation = name;
		PixelsCharacterModels.AnimationsData.loadAnimation(name, PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
		if (returnValue == "addToFrames") {
			PixelsCharacterModels.client.currentStoredFrames.frames.add(name);
			PixelsCharacterModels.client.writeFrames(PixelsCharacterModels.GuiData.SelectedFrames, PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
		}
		PixelsCharacterModels.client.openScreen(lastGui);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public boolean isNumeric(String s) {
		return NumberUtils.isCreatable(s);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
