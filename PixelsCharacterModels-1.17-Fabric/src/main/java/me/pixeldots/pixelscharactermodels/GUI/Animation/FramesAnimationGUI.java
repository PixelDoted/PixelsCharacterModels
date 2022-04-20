package me.pixeldots.pixelscharactermodels.GUI.Animation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.GUI.Editor.EditorGui;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
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
			PixelsCharacterModels.client.openScreen(new PresetsGui(false));
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
		
		MoveUp = addButton(new ButtonWidget(5, 60, 50, 20, Text.of("Move UP"), (button) -> {
			int i = PixelsCharacterModels.client.currentStoredFrames.frames.indexOf(PixelsCharacterModels.GuiData.SelectedAnimation);
			if (i-1 != -1) {
				String store = PixelsCharacterModels.client.currentStoredFrames.frames.get(i);
				PixelsCharacterModels.client.currentStoredFrames.frames.set(i, PixelsCharacterModels.client.currentStoredFrames.frames.get(i-1));
				PixelsCharacterModels.client.currentStoredFrames.frames.set(i-1, store);
				PixelsCharacterModels.client.writeFrames(PixelsCharacterModels.GuiData.SelectedFrames, PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
			}
			PixelsCharacterModels.client.openScreen(new FramesAnimationGUI());
		}));
		MoveDown = addButton(new ButtonWidget(5, 85, 50, 20, Text.of("Move DOWN"), (button) -> {
			int i = PixelsCharacterModels.client.currentStoredFrames.frames.indexOf(PixelsCharacterModels.GuiData.SelectedAnimation);
			if (!(i+1 >= PixelsCharacterModels.client.currentStoredFrames.frames.size())) {
				String store = PixelsCharacterModels.client.currentStoredFrames.frames.get(i);
				PixelsCharacterModels.client.currentStoredFrames.frames.set(i, PixelsCharacterModels.client.currentStoredFrames.frames.get(i+1));
				PixelsCharacterModels.client.currentStoredFrames.frames.set(i+1, store);
				PixelsCharacterModels.client.writeFrames(PixelsCharacterModels.GuiData.SelectedFrames, PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
			}
			PixelsCharacterModels.client.openScreen(new FramesAnimationGUI());
		}));
		
		Add = addButton(new ButtonWidget(5, 110, 50, 20, Text.of("Add"), (button) -> {
			PixelsCharacterModels.client.openScreen(new AnimationsGui(this, "addToFrames"));
		}));
		Remove = addButton(new ButtonWidget(5, 135, 50, 20, Text.of("Remove"), (button) -> {
			PixelsCharacterModels.client.currentStoredFrames.frames.remove(PixelsCharacterModels.GuiData.SelectedAnimation);
			PixelsCharacterModels.client.writeFrames(PixelsCharacterModels.GuiData.SelectedFrames, PixelsCharacterModels.GuiData.entity, PixelsCharacterModels.GuiData.model);
			PixelsCharacterModels.client.openScreen(new FramesAnimationGUI());
		}));
		
		if (!PixelsCharacterModels.client.currentStoredFrames.frames.contains(PixelsCharacterModels.GuiData.SelectedAnimation)) {
			MoveUp.active = false;
			MoveDown.active = false;
			Remove.active = false;
		}
		
		int Col = 1;
		int Row = 1;
		for (int i = 0; i < PixelsCharacterModels.client.currentStoredFrames.frames.size(); i++) {
			String key = (String) PixelsCharacterModels.client.currentStoredFrames.frames.get(i);
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
		PixelsCharacterModels.client.openScreen(new FramesAnimationGUI());
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
