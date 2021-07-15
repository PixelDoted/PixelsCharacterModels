package me.pixeldots.pixelscharactermodels.GUI;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.Animation.PCMAnimation;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FramesAnimationGUI extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget MoveUp;
	public ButtonWidget MoveDown;
	public ButtonWidget Remove;
	public ButtonWidget Add;
	
	public List<ButtonWidget> Animations = new ArrayList<ButtonWidget>();
	public GuiHandler lastGui;
	
	public int maxCol = 10;
	public int maxRow = 5;
	
	public FramesAnimationGUI() {
		super("Frames Animations");
	}
	
	public FramesAnimationGUI(GuiHandler c) {
		this();
		lastGui = c;
	}
	
	@Override
	public void init() {
		super.init();
		Presets = addButton(new ButtonWidget(5,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Presets), (button) -> {
			MinecraftClient.getInstance().openScreen(new PresetsGui());
		}));
		Editor = addButton(new ButtonWidget(60,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Editor), (button) -> {
			MinecraftClient.getInstance().openScreen(new EditorGui());
		}));
		Animation = addButton(new ButtonWidget(5,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Animation), (button) -> {
			MinecraftClient.getInstance().openScreen(new AnimationGui());
		}));
		Frames = addButton(new ButtonWidget(60,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Frames), (button) -> {
			MinecraftClient.getInstance().openScreen(new FramesGui());
		}));
		
		MoveUp = addButton(new ButtonWidget(5, 60, 50, 20, Text.of("Move UP"), (button) -> {
			int i = PixelsCharacterModels.PCMClient.currentStoredFrames.frames.indexOf(PixelsCharacterModels.GuiData.SelectedAnimation);
			if (i-1 != -1) {
				String store = PixelsCharacterModels.PCMClient.currentStoredFrames.frames.get(i);
				PixelsCharacterModels.PCMClient.currentStoredFrames.frames.set(i, PixelsCharacterModels.PCMClient.currentStoredFrames.frames.get(i-1));
				PixelsCharacterModels.PCMClient.currentStoredFrames.frames.set(i-1, store);
			}
		}));
		MoveDown = addButton(new ButtonWidget(5, 85, 50, 20, Text.of("Move DOWN"), (button) -> {
			int i = PixelsCharacterModels.PCMClient.currentStoredFrames.frames.indexOf(PixelsCharacterModels.GuiData.SelectedAnimation);
			if (!(i+1 >= PixelsCharacterModels.PCMClient.currentStoredFrames.frames.size())) {
				String store = PixelsCharacterModels.PCMClient.currentStoredFrames.frames.get(i);
				PixelsCharacterModels.PCMClient.currentStoredFrames.frames.set(i, PixelsCharacterModels.PCMClient.currentStoredFrames.frames.get(i+1));
				PixelsCharacterModels.PCMClient.currentStoredFrames.frames.set(i+1, store);
			}
		}));
		
		Add = addButton(new ButtonWidget(5, 5, 110, 20, Text.of("Add"), (button) -> {
			PixelsCharacterModels.client.openScreen(new AnimationsGui(this, "addToFrames"));
		}));
		Remove = addButton(new ButtonWidget(5, 135, 50, 20, Text.of("Remove"), (button) -> {
			PixelsCharacterModels.PCMClient.currentStoredFrames.frames.remove(PixelsCharacterModels.GuiData.SelectedAnimation);
		}));
		
		if (PixelsCharacterModels.PCMClient.currentStoredFrames.frames.contains(PixelsCharacterModels.GuiData.SelectedAnimation)) {
			MoveUp.active = false;
			MoveDown.active = false;
			Add.active = false;
			Remove.active = false;
		}
		
		int Col = 1;
		int Row = 1;
		for (int i = 0; i < PixelsCharacterModels.PCMClient.currentStoredFrames.frames.size(); i++) {
			String key = (String) PixelsCharacterModels.PCMClient.currentStoredFrames.frames.get(i);
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
		MinecraftClient.getInstance().openScreen(lastGui);
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
