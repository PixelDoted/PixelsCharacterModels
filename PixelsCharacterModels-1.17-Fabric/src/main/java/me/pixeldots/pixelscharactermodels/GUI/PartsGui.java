package me.pixeldots.pixelscharactermodels.GUI;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.brigadier.arguments.FloatArgumentType;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.accessors.PlayerModelAccessor;
import me.pixeldots.pixelscharactermodels.model.part.ModelPartData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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
	
	public List<ButtonWidget> Parts = new ArrayList<ButtonWidget>();
	public GuiHandler lastGui;
	
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
		
		Parts.add(addButton(new ButtonWidget(200, 50, 50, 20, Text.of("Head"), (button) -> {
			button.active = false;
			SelectPart("Head",0,PixelsCharacterModels.GuiData.model.head);
		})));
		Parts.add(addButton(new ButtonWidget(200, 80, 50, 20, Text.of("Body"), (button) -> {
			button.active = false;
			SelectPart("Body",1,PixelsCharacterModels.GuiData.model.body);
		})));
		Parts.add(addButton(new ButtonWidget(200, 120, 50, 20, Text.of("LeftArm"), (button) -> {
			button.active = false;
			SelectPart("LeftArm",2,PixelsCharacterModels.GuiData.model.leftArm);
		})));
		Parts.add(addButton(new ButtonWidget(200, 150, 50, 20, Text.of("RightArm"), (button) -> {
			button.active = false;
			SelectPart("RightArm",3,PixelsCharacterModels.GuiData.model.rightArm);
		})));
		Parts.add(addButton(new ButtonWidget(200, 180, 50, 20, Text.of("LeftLeg"), (button) -> {
			button.active = false;
			SelectPart("LeftLeg",4,PixelsCharacterModels.GuiData.model.leftLeg);
		})));
		Parts.add(addButton(new ButtonWidget(200, 210, 50, 20, Text.of("RightLeg"), (button) -> {
			button.active = false;
			SelectPart("RightLeg",5,PixelsCharacterModels.GuiData.model.rightLeg);
		})));
		
		//Create = addButton(new ButtonWidget(5, 100, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
		//	MinecraftClient.getInstance().openScreen(new CreatePartGui());
		//}));
		//Remove = addButton(new ButtonWidget(5, 125, 50, 20, Text.of(PixelsCharacterModels.TranslatedText.Remove), (button) -> {
		//	RemovePart(PixelsCharacterModels.GuiData.SelectedPartModel);
		//}));
		if (PixelsCharacterModels.GuiData.model != null) {
			List<ModelPart> parts = ((PlayerModelAccessor)PixelsCharacterModels.GuiData.model).getParts();
			for (int i = 0; i < parts.size(); i++) {
				ModelPartData part = PixelsCharacterModels.dataPackets.get(parts.get(i));
				if (part != null) {	
					if (part.name != "") {
						int num = i;
						Parts.add(addButton(new ButtonWidget(200, 240, 50, 20, Text.of(part.name), (button) -> {
							button.active = false;
							SelectPart(part.name, 6+num, parts.get(num));
						})));
					}
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
		MinecraftClient.getInstance().openScreen(lastGui);
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
