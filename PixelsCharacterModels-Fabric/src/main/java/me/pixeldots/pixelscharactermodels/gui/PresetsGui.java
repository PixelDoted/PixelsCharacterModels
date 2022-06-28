package me.pixeldots.pixelscharactermodels.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.files.PresetHelper;
import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.network.ClientNetwork;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class PresetsGui extends EntityGuiHandler {

    private int yscroll = 0;
    public float entityViewScale = 75;

    public String selectedPreset = "";
    public List<ButtonWidget> presetButtons = new ArrayList<>();

    public static String path_offset = "";

    public PresetsGui(LivingEntity _entity) {
        super("Presets");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
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
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {})).active = false;
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {
            setScreen(new EditorGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text.of("Animation"), (btn) -> {
            setScreen(new AnimationGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text.of("Settings"), (btn) -> {
            setScreen(new SettingsGui(entity, this.entityViewScale));
        }));

        int presets_offset = 15;
        if (PCMMain.settings.preview_preset) {
            presets_offset = 35;
            addButton(new FlatButtonWidget(5, 15, 110, 10, Text.of("Select"), (btn) -> {
                if (selectedPreset.equals("default")) defaultPreset(true);
                else selectPreset(new File(selectedPreset), true);
            }));
        }

        ButtonWidget default_preset = addButton(new FlatButtonWidget(5, presets_offset+yscroll, 110, 10, Text.of(path_offset == "" ? "default" : "back"), (btn) -> {
            if (path_offset == "") defaultPreset(false);
            else {
                path_offset = path_offset.substring(0, path_offset.lastIndexOf(File.separator));
                this.client.setScreen(new PresetsGui(entity, entityViewScale));
            }
        }));
        presetButtons.add(default_preset);
        presets_offset += 10;

        File[] files = new File(this.client.runDirectory.getAbsolutePath() + File.separator + "PCM" + File.separator + "Presets" + path_offset).listFiles();
        for (int i = 0; i < files.length; i++) {
            final File file = files[i];
            if (!file.isDirectory() && !file.getName().endsWith(".json")) continue;

            final boolean is_preset = (file.isDirectory() && containsRoot(file.listFiles())) || file.getName().endsWith(".json");
            ButtonWidget widget = addButton(new FlatButtonWidget(15, presets_offset+(i*10)+yscroll, 100, 10, Text.of((is_preset ? "" : "~") + file.getName()), (btn) -> {
                if (is_preset) selectPreset(file, false);
                else { 
                    path_offset += "/" + file.getName();
                    this.client.setScreen(new PresetsGui(entity, entityViewScale));
                }
            }));
            ButtonWidget save_widget = addButton(new FlatButtonWidget(5, presets_offset+(i*10)+yscroll, 10, 10, Text.of("S"), (btn) -> {
                PresetHelper.write_preset(file, entity, model);
            }, this, Arrays.asList(Text.of("Save")) ));

            widget.visible = !((PCMMain.settings.preview_preset && widget.y < 35) || widget.y < 10);
            presetButtons.add(widget); presetButtons.add(save_widget);
        }

        TextFieldWidget createname = addTextField(new TextFieldWidget(textRenderer, 125, 60, 55, 10, Text.of("")));
        addButton(new FlatButtonWidget(125, 45, 55, 10, Text.of("create"), (btn) -> {
            File file = new File(this.client.runDirectory.getAbsolutePath() + File.separator + "PCM" + File.separator + "Presets" + path_offset + File.separator + createname.getText());
            PresetHelper.write_preset(file, entity, model);
            this.client.setScreen(new PresetsGui(entity, entityViewScale));
        }));
        addButton(new FlatButtonWidget(125, 35, 55, 10, Text.of("rename"), (btn) -> {
            File file = new File(selectedPreset);
            if (!file.exists() || selectedPreset.equals("default")) return;
            String new_path = selectedPreset.replace(file.getName(), createname.getText());

            file.renameTo(new File(new_path));
            selectedPreset = new_path;
            this.client.setScreen(new PresetsGui(entity, entityViewScale));
        }));

        addButton(new FlatButtonWidget(125, 15, 55, 10, Text.of("remove"), (btn) -> {
            File file = new File(selectedPreset);
            if (!file.exists() || selectedPreset.equals("default")) return;
            file.delete();
            
            selectedPreset = "";
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
        float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2)+65 - mouseX;
            entityMouseY = (float)(this.height/2+37-125) - mouseY;
        }

        if (entity != null)
            drawEntity(this.width/2+65, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity, PCMMain.settings.show_block_under_player_ui);

        drawColor(matrices, 0, 0, 185, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 185, 9, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 184, 9, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, 15, this.height-6, 0, 0, 0, 188);
        drawVerticalLine(matrices, 120, 15, this.height-6, 0, 0, 0, 188);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);

        drawCenteredText(matrices, textRenderer, Text.of(""), 60, this.height-60, 0xFFFFFF);
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

    public void selectPreset(File file, boolean force_load) {
        selectedPreset = file.getAbsolutePath();
        if (force_load || !PCMMain.settings.preview_preset)
            PresetHelper.read_preset(file, entity, model);
    }

    public void defaultPreset(boolean force_load) {
        selectedPreset = "default";
        if (force_load || !PCMMain.settings.preview_preset) {
            ClientHelper.reset_entity(uuid);

            ClientNetwork.send_pehkui_scale(entity, 1);

            ClientNetwork.send_skin_suffix(uuid, "");
            SkinHelper.setSkinSuffix(uuid, "");
            SkinHelper.reloadSkins();
        }
    }

}
