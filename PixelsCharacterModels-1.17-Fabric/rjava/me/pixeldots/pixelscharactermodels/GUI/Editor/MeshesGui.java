package me.pixeldots.pixelscharactermodels.GUI.Editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.GuiHandler;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.AnimationGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.FramesGui;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MeshesGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget BackButton;
	
	public List<ButtonWidget> Meshes = new ArrayList<ButtonWidget>();
	public GuiHandler lastGui;
	
	public MeshesGui() {
		super("Meshes");
	}
	
	public MeshesGui(GuiHandler c) {
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
		if (lastGui != null) {
			BackButton = addButton(new ButtonWidget(5, 55, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Back), (button) -> {
				PixelsCharacterModels.client.openScreen(lastGui);
			}));
		}
		
		Meshes.add(addButton(new ButtonWidget(200, 50, 50, 20, Text.of("Cube"), (button) -> {
			button.active = true;
			SelectMesh("Cube");
		})));
		
		File[] MeshFiles = PixelsCharacterModels.saveData.getMeshes();
		int row = 1;
		int col = 0;
		if (MeshFiles != null) {
			for (File file : MeshFiles) {
				String name = file.getName().replace(".obj", "");
				Meshes.add(addButton(new ButtonWidget(200+(55*col),50+(25*row),50,20, Text.of(name), (button) -> {
					button.active = true;
					SelectMesh(name);
				})));
				row++;
				if (row > 8) {
					col++;
					row = 0;
				}
			}
		}
		
		for (ButtonWidget widget : Meshes) {
			if (widget.getMessage().asString() == PixelsCharacterModels.GuiData.createPartData.mesh) {
				widget.active = false;
				break;
			}
		}
	}
	
	public void SelectMesh(String name) {
		PixelsCharacterModels.GuiData.createPartData.mesh = name;
		PixelsCharacterModels.client.openScreen(lastGui);
	}
	public void RemovePart(ModelPart part) {
		
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
