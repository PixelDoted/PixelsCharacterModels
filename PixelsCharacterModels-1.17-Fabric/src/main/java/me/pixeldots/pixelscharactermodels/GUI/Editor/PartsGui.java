package me.pixeldots.pixelscharactermodels.GUI.Editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.AnimationGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.FramesGui;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class PartsGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget Create;
	public ButtonWidget Remove;
	public ButtonWidget BackButton;
	
	public List<ButtonWidget> Parts = new ArrayList<ButtonWidget>();
	public GuiHandler lastGui;
	
	public int PartModelID = -1;
	public boolean PartModelisMesh = false;
	
	public PartsGui() {
		super("Parts");
	}
	
	public PartsGui(GuiHandler c) {
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
		if (lastGui != null) {
			BackButton = addButton(new ButtonWidget(5, 55, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Back), (button) -> {
				PixelsCharacterModels.client.openScreen(lastGui);
			}));
		}
		
		Parts.add(addButton(new ButtonWidget(125, 5, 50, 20, Text.of("Head"), (button) -> {
			button.active = false;
			SelectPart("Head",0,PixelsCharacterModels.GuiData.model.head);
		})));
		Parts.add(addButton(new ButtonWidget(125, 30, 50, 20, Text.of("Body"), (button) -> {
			button.active = false;
			SelectPart("Body",1,PixelsCharacterModels.GuiData.model.body);
		})));
		Parts.add(addButton(new ButtonWidget(125, 55, 50, 20, Text.of("LeftArm"), (button) -> {
			button.active = false;
			SelectPart("LeftArm",2,PixelsCharacterModels.GuiData.model.leftArm);
		})));
		Parts.add(addButton(new ButtonWidget(125, 80, 50, 20, Text.of("RightArm"), (button) -> {
			button.active = false;
			SelectPart("RightArm",3,PixelsCharacterModels.GuiData.model.rightArm);
		})));
		Parts.add(addButton(new ButtonWidget(125, 105, 50, 20, Text.of("LeftLeg"), (button) -> {
			button.active = false;
			SelectPart("LeftLeg",4,PixelsCharacterModels.GuiData.model.leftLeg);
		})));
		Parts.add(addButton(new ButtonWidget(125, 130, 50, 20, Text.of("RightLeg"), (button) -> {
			button.active = false;
			SelectPart("RightLeg",5,PixelsCharacterModels.GuiData.model.rightLeg);
		})));
		
		Create = addButton(new ButtonWidget(5, 100, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
			PixelsCharacterModels.client.openScreen(new CreatePartGui());
		}));
		Remove = addButton(new ButtonWidget(5, 125, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Remove), (button) -> {
			RemovePart();
		}));
		if (PixelsCharacterModels.GuiData.SelectedPartModel != null) {
			ModelPartData data = PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel);
			int col = 0;
			int row = 0;
			for (int i = 0; i < data.cubes.size(); i++) {
				int num = i;
				Parts.add(addButton(new ButtonWidget(200+(55*col), 5+(25*row), 50, 20, Text.of(data.cubes.get(i).name), (button) -> {
					button.active = false;
					SelectPartModel(data.cubes.get(num).name, num, false);
				})));
				row++;
				if (row >= 8) {
					col++;
					row = 0;
				}
			}
			for (int i = 0; i < data.meshes.size(); i++) {
				int num = i;
				Parts.add(addButton(new ButtonWidget(200+(55*col), 5+(25*row), 50, 20, Text.of(data.meshes.get(i).name), (button) -> {
					button.active = false;
					SelectPartModel(data.meshes.get(num).name, num, true);
				})));
				row++;
				if (row >= 8) {
					col++;
					row = 0;
				}
			}
		}
		for (int i = 0; i < Parts.size(); i++) {
			if (Parts.get(i).getMessage().asString() == PixelsCharacterModels.GuiData.SelectedPart)
			{Parts.get(i).active = false; break;}
		}
	}
	
	public void SelectPart(String name, int id, ModelPart part) {
		PixelsCharacterModels.GuiData.SelectedPart = name;
		PixelsCharacterModels.GuiData.SelectedPartID = id;
		PixelsCharacterModels.GuiData.SelectedPartModel = part;
		PixelsCharacterModels.client.openScreen(lastGui);
	}
	
	public void SelectPartModel(String name, int id, boolean isMesh) {
		PartModelID = id;
		PartModelisMesh = isMesh;
	}
	public void RemovePart() {
		if (PartModelID == -1) return;
		if (PartModelisMesh) PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).meshes.remove(PartModelID);
		else PixelsCharacterModels.dataPackets.get(PixelsCharacterModels.GuiData.SelectedPartModel).cubes.remove(PartModelID);
		
		if (PixelsCharacterModels.GuiData.SelectedPresetID != -1) {
			PixelsCharacterModels.client.writePreset(PixelsCharacterModels.GuiData.SelectedPresetName.replace(".json", ""), client.player, PixelsCharacterModels.EntityModelList.get(client.player));
		}
		PixelsCharacterModels.client.openScreen(new PartsGui(lastGui));
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
