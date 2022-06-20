package me.pixeldots.pixelscharactermodels.GUI.Editor;

import org.apache.commons.lang3.math.NumberUtils;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.GUI.PresetsGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.AnimationGui;
import me.pixeldots.pixelscharactermodels.GUI.Animation.FramesGui;
import me.pixeldots.pixelscharactermodels.GUI.Handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.model.CreatePartData;
import me.pixeldots.pixelscharactermodels.model.PreviewModelPart;
import me.pixeldots.pixelscharactermodels.model.part.createPartHelper;
import me.pixeldots.pixelscharactermodels.model.part.cube.ModelPartCube;
import me.pixeldots.pixelscharactermodels.model.part.mesh.ModelPartMesh;
import me.pixeldots.pixelscharactermodels.utils.GuiData;
import me.pixeldots.pixelscharactermodels.utils.MapVec2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CreatePartGui extends GuiHandler {
	
	public ButtonWidget Presets;
	public ButtonWidget Editor;
	public ButtonWidget Animation;
	public ButtonWidget Frames;
	
	public ButtonWidget Parent;
	public ButtonWidget Mesh;
	
	public TextFieldWidget PartName;
	
	public TextFieldWidget PositionX;
	public TextFieldWidget PositionY;
	public TextFieldWidget PositionZ;
	
	public TextFieldWidget SizeX;
	public TextFieldWidget SizeY;
	public TextFieldWidget SizeZ;
	
	public TextFieldWidget TextureOffsetX;
	public TextFieldWidget TextureOffsetY;
	public TextFieldWidget TextureSizeX;
	public TextFieldWidget TextureSizeY;

	public TextFieldWidget TextureID;
	
	public ButtonWidget CreatePart;
	
	public CreatePartGui() {
		super("CreatePart");
	}
	
	@Override
	public void init() {
		super.init();
		Presets = addButton(new ButtonWidget(5,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Presets), (button) -> {
			PixelsCharacterModels.previewModelPart = null;
			PixelsCharacterModels.client.openScreen(new PresetsGui(false));
		}));
		Editor = addButton(new ButtonWidget(60,5,50,20, Text.of(PixelsCharacterModels.TranslatedText.Editor), (button) -> {
			PixelsCharacterModels.previewModelPart = null;
			PixelsCharacterModels.client.openScreen(new EditorGui());
		}));
		Animation = addButton(new ButtonWidget(5,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Animation), (button) -> {
			PixelsCharacterModels.previewModelPart = null;
			PixelsCharacterModels.client.openScreen(new AnimationGui());
		}));
		Frames = addButton(new ButtonWidget(60,30,50,20, Text.of(PixelsCharacterModels.TranslatedText.Frames), (button) -> {
			PixelsCharacterModels.previewModelPart = null;
			PixelsCharacterModels.client.openScreen(new FramesGui());
		}));
		
		PartName = addTextField(new TextFieldWidget(textRendererGUI, 200, 60, 75, 20, Text.of("Name")));
		
		Mesh = addButton(new ButtonWidget(188, 85, 50, 20, Text.of("Mesh"), (button) -> {
			PixelsCharacterModels.client.openScreen(new MeshesGui(this));
		}));
		Parent = addButton(new ButtonWidget(238, 85, 50, 20, Text.of("Parent"), (button) -> {
			PixelsCharacterModels.client.openScreen(new PartsGui(this));
		}));
		PositionX = addTextField(new TextFieldWidget(textRendererGUI, 185, 110, 35, 20, Text.of("Position X")));
		PositionY = addTextField(new TextFieldWidget(textRendererGUI, 222, 110, 35, 20, Text.of("Position Y")));
		PositionZ = addTextField(new TextFieldWidget(textRendererGUI, 259, 110, 35, 20, Text.of("Position Z")));
		
		SizeX = addTextField(new TextFieldWidget(textRendererGUI, 185, 135, 35, 20, Text.of("Size X")));
		SizeY = addTextField(new TextFieldWidget(textRendererGUI, 222, 135, 35, 20, Text.of("Size Y")));
		SizeZ = addTextField(new TextFieldWidget(textRendererGUI, 259, 135, 35, 20, Text.of("Size Z")));
		
		TextureOffsetX = addTextField(new TextFieldWidget(textRendererGUI, 185, 160, 54, 20, Text.of("Texture X")));
		TextureOffsetY = addTextField(new TextFieldWidget(textRendererGUI, 241, 160, 54, 20, Text.of("Texture Y")));
		
		TextureSizeX = addTextField(new TextFieldWidget(textRendererGUI, 185, 185, 54, 20, Text.of("Texture Width")));
		TextureSizeY = addTextField(new TextFieldWidget(textRendererGUI, 241, 185, 54, 20, Text.of("Texture Height")));

		TextureID = addTextField(new TextFieldWidget(textRendererGUI, 185, 210, 100, 20, Text.of("Texture ID")));

		CreatePart = addButton(new ButtonWidget(185, 235, 100, 20, Text.of(PixelsCharacterModels.TranslatedText.Create), (button) -> {
			PixelsCharacterModels.previewModelPart = null;
			GuiData data = PixelsCharacterModels.GuiData;
			if (data.createPartData.mesh != "Cube") {
				createPartHelper.createMesh(data.createPartData.mesh, data.createPartData.Position, data.createPartData.Size, data.createPartData.UV, data.model, data.entity, data.SelectedPartModel, PartName.getText(), TextureID.getText());
			} else {
				createPartHelper.createCuboid(data.createPartData.Position, data.createPartData.Size, data.createPartData.Pivot, new MapVec2(64, 64), data.createPartData.UV, data.SelectedPartModel, PartName.getText(), TextureID.getText());
			}
			if (PixelsCharacterModels.GuiData.SelectedPresetPath.endsWith(".json"))
				PixelsCharacterModels.client.writePreset(PixelsCharacterModels.GuiData.SelectedPresetPath, client.player, PixelsCharacterModels.PlayerDataList.get(client.player.getUuid()).model);
			
			PixelsCharacterModels.client.openScreen(new PartsGui());
		}));
		
		if (PixelsCharacterModels.GuiData.createPartData != null) {
			CreatePartData data = PixelsCharacterModels.GuiData.createPartData;
			PartName.setText(data.name);
			Mesh.setMessage(Text.of(data.mesh));
			Parent.setMessage(Text.of(PixelsCharacterModels.GuiData.SelectedPart));
			UpdatePreview();
		}
	}

	@Override
	public void onClose() {
		PixelsCharacterModels.previewModelPart = null;
		super.onClose();
	}

	@Override
	public void tick() {
		if (isNumeric(PositionX.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Position.X = Float.parseFloat(PositionX.getText());
		}
		if (isNumeric(PositionY.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Position.Y = Float.parseFloat(PositionY.getText());
		}
		if (isNumeric(PositionZ.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Position.Z = Float.parseFloat(PositionZ.getText());
		}
		
		if (isNumeric(SizeX.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Size.X = Float.parseFloat(SizeX.getText());
		}
		if (isNumeric(SizeY.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Size.Y = Float.parseFloat(SizeY.getText());
		}
		if (isNumeric(SizeZ.getText())) {
			PixelsCharacterModels.GuiData.createPartData.Size.Z = Float.parseFloat(SizeZ.getText());
		}
		
		if (isNumeric(TextureOffsetX.getText())) {
			PixelsCharacterModels.GuiData.createPartData.UV.X = Float.parseFloat(TextureOffsetX.getText());
		}
		if (isNumeric(TextureOffsetY.getText())) {
			PixelsCharacterModels.GuiData.createPartData.UV.Y = Float.parseFloat(TextureOffsetY.getText());
		}
		if (isNumeric(PositionX.getText()) || isNumeric(PositionY.getText()) || isNumeric(PositionZ.getText()) || 
			isNumeric(SizeX.getText()) || isNumeric(SizeY.getText()) || isNumeric(SizeZ.getText()) || isNumeric(TextureOffsetX.getText()) || isNumeric(TextureOffsetY.getText())) {
			UpdatePreview();
		}
		super.tick();
	}
	
	public boolean isNumeric(String s) {
		return NumberUtils.isCreatable(s);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		drawString(matrices, "Position", 145, 110);
		drawString(matrices, "Size", 163, 135);
		drawString(matrices, "Texture Offset", 109, 160);
		drawString(matrices, "Texture Size", 119, 185);
		drawString(matrices, "Texture Name", 119, 210);
		
		drawEntity(this.width/2+100, this.height/2, 75, (float)(this.width/2+100) - mouseX, (float)(this.height/2-125) - mouseY, PixelsCharacterModels.GuiData.entity);
		super.render(matrices, mouseX, mouseY, delta);
	}

	public void UpdatePreview() {
		GuiData data = PixelsCharacterModels.GuiData;
		if (data.createPartData.mesh != "Cube") {
			ModelPartMesh mesh = createPartHelper.createMeshReturn(data.createPartData.mesh, data.createPartData.Position, data.createPartData.Size, data.createPartData.UV, data.model, data.entity, data.SelectedPartModel, PartName.getText(), TextureID.getText());
			if (mesh == null) return;

			PreviewModelPart preview = new PreviewModelPart();
			preview.mesh = mesh;
			preview.owner = PixelsCharacterModels.dataPackets.get(data.SelectedPartModel);
			PixelsCharacterModels.previewModelPart = preview;
		} else {
			ModelPartCube cube = createPartHelper.createCuboidReturn(data.createPartData.Position, data.createPartData.Size, data.createPartData.Pivot, new MapVec2(64, 64), data.createPartData.UV, data.SelectedPartModel, PartName.getText(), TextureID.getText());
			if (cube == null) return;

			PreviewModelPart preview = new PreviewModelPart();
			preview.cube = cube;
			preview.owner = PixelsCharacterModels.dataPackets.get(data.SelectedPartModel);
			PixelsCharacterModels.previewModelPart = preview;
		}
	}
	
}
