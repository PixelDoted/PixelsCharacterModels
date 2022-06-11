package me.pixeldots.pixelscharactermodels.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.PresetHelper;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import virtuoel.pehkui.api.ScaleTypes;

public class PresetsGui extends GuiHandler {

    private int yscroll = 0;

    public LivingEntity entity;
    public IAnimalModelMixin model;
    public UUID uuid;
    public float entityViewScale = 75;

    public String selectedPreset = "";
    public List<ButtonWidget> presetButtons = new ArrayList<>();

    public static String path_offset = "";

    public PresetsGui(LivingEntity _entity) {
        super("Presets");
        entity = _entity;
        model = (IAnimalModelMixin)PlatformUtils.getModel(_entity);
        uuid = _entity.getUuid();
    }

    public PresetsGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        path_offset = "";
        this.client.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();

        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {}));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {
            setScreen(new EditorGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text.of("Animation"), (btn) -> {
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text.of("Settings"), (btn) -> {
        }));

        int presets_offset = 15;
        if (PCMMain.settings.preview_preset) {
            presets_offset = 35;
            addButton(new ButtonWidget(5, 15, 110, 10, Text.of("Select"), (btn) -> {
                selectPreset(entity, model, new File(selectedPreset), true);
            }));
        }

        ButtonWidget default_preset = addButton(new ButtonWidget(5, presets_offset+yscroll, 110, 10, Text.of("default"), (btn) -> {
            ClientHelper.reset_entity(uuid);
            ScaleTypes.BASE.getScaleData(entity).setScale(1);
            SkinHelper.setSkinSuffix(uuid, "");
            SkinHelper.reloadSkins();
        }));
        presetButtons.add(default_preset);

        File[] files = new File(this.client.runDirectory.getAbsolutePath() + "/PCM/Presets" + path_offset).listFiles();
        for (int i = 0; i < files.length; i++) {
            final File file = files[i];
            if (!file.isDirectory()) continue;

            final boolean is_preset = file.isDirectory() && containsRoot(file.listFiles());
            ButtonWidget widget = addButton(new ButtonWidget(15, presets_offset+(i*10)+yscroll, 100, 10, Text.of((is_preset ? "" : "~") + file.getName()), (btn) -> {
                if (is_preset) selectPreset(entity, model, file, false);
                else path_offset += "/" + file.getName();
            }));
            ButtonWidget save_widget = addButton(new ButtonWidget(5, presets_offset+(i*10)+yscroll, 10, 10, Text.of("S"), (btn) -> {
                PresetHelper.write_preset(file, entity, model);
            }));

            widget.visible = !((PCMMain.settings.preview_preset && widget.y < 35) || widget.y < 10);
            presetButtons.add(widget); presetButtons.add(save_widget);
        }

        TextFieldWidget createname = addTextField(new TextFieldWidget(textRenderer, 5, this.height-30, 110, 10, Text.of("")));
        addButton(new ButtonWidget(5, this.height-15, 110, 10, Text.of("create"), (btn) -> {
            File file = new File(this.client.runDirectory.getAbsolutePath() + "/PCM/Presets" + path_offset + "/" + createname.getText());
            PresetHelper.write_preset(file, entity, model);
            this.client.setScreen(new PresetsGui(entity, entityViewScale));
        }));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX < 120) {
            yscroll += amount*10;
            if (yscroll > 0) yscroll = 0;
            else {
                for (ButtonWidget widget : presetButtons) {
                    widget.y += amount*10;
                    widget.visible = !((PCMMain.settings.preview_preset && widget.y < 35) || widget.y < 10);
                }
            }
        } else if (mouseX >= 120 && mouseX < this.width) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float entityMouseX = (float)(this.width/2);
        float entityMouseY = (float)(this.height/2+37-125);

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX -= mouseX;
            entityMouseY -= mouseY;
        } else {
            entityMouseX -= this.width/2-13.5f;
            entityMouseY -= this.height/2+80;
        }

        if (entity != null) {
            if (PCMMain.settings.show_block_under_player_ui) {
                drawEntityOnBlock(this.width/2, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity);
            } else {
                drawEntity(this.width/2, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity);
            }
        }

        drawColor(matrices, 0, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        path_offset = "";
        super.close();
    }
    
    public boolean containsRoot(File[] files) {
        for (File file : files) {
            if (file.getName().equalsIgnoreCase("preset.json")) return true;
        }
        return false;
    }

    public void selectPreset(LivingEntity entity, IAnimalModelMixin model, File file, boolean force_load) {
        if (!force_load && PCMMain.settings.preview_preset) {
            selectedPreset = file.getAbsolutePath();
        } else PresetHelper.read_preset(file, entity, model);
    }

}
