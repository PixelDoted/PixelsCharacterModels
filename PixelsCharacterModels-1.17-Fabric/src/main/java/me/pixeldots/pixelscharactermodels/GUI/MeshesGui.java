package me.pixeldots.pixelscharactermodels.GUI;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.brigadier.arguments.FloatArgumentType;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MeshesGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget Create;
	public ButtonWidget Remove;
	
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
		
		Meshes.add(addButton(new ButtonWidget(200, 50, 50, 20, Text.of("Cube"), (button) -> {
			button.active = true;
			SelectMesh("Cube");
		})));
		
		/*for (int i = 0; i < PixelsCharacterModels.OtherSaveData.MeshNames.size(); i++) {
			String name = PixelsCharacterModels.OtherSaveData.MeshNames.get(i);
			Meshes.add(addButton(new ButtonWidget(200,50,50,20, Text.of(name), (button) -> {
				button.active = true;
				SelectMesh(name);
			})));
		}*/
		
		for (int i = 0; i < Meshes.size(); i++) {
			if (Meshes.get(i).getMessage().asString() == PixelsCharacterModels.GuiData.createPartData.mesh)
			{Meshes.get(i).active = false; break;}
		}
	}
	
	public void SelectMesh(String name) {
		PixelsCharacterModels.GuiData.createPartData.mesh = name;
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
