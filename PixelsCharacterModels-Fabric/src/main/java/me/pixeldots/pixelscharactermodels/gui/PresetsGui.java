package me.pixeldots.pixelscharactermodels.gui;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.scriptedmodels.platform.FabricUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class PresetsGui extends GuiHandler {

    private int yscroll = 0;

    public UUID uuid;
    public float entityViewScale = 75;

    public int selectedPreset = -1;
    public List<ButtonWidget> presetButtons = new ArrayList<>();

    public PresetsGui() {
        super("Presets");
    }

    public PresetsGui(float _entityViewScale) {
        super("Presets");
        entityViewScale = _entityViewScale;
    }

    @Override
    public void init() {
        super.init();
        LivingEntity entity = PixelsCharacterModels.minecraft.player;
        IAnimalModelMixin model = (IAnimalModelMixin)FabricUtils.getModel(entity);
        uuid = entity.getUuid();

        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {
            //this.client.setScreen(new PresetsGui());
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {
            this.client.setScreen(new EditorGui(this.entityViewScale));
        }));

        int presets_offset = 15;
        if (PixelsCharacterModels.settings.preview_preset) {
            presets_offset = 35;
            addButton(new ButtonWidget(5, 15, 110, 10, Text.of("Select"), (btn) -> {

            }));
        }

        File[] files = Paths.get(".", "PCM/Presets").toFile().listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            final int num = i;

            ButtonWidget widget = addButton(new ButtonWidget(5, presets_offset+(i*10)+yscroll, 110, 10, Text.of(file.getName()), (btn) -> {
                selectedPreset = num;
            }));
            widget.visible = !((PixelsCharacterModels.settings.preview_preset && widget.y < 35) || widget.y < 10);
            presetButtons.add(widget);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX < 120) {
            yscroll += amount*10;
            if (yscroll > 0) yscroll = 0;
            else {
                for (ButtonWidget widget : presetButtons) {
                    widget.y += amount*10;
                    widget.visible = !((PixelsCharacterModels.settings.preview_preset && widget.y < 35) || widget.y < 10);
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

        if (PixelsCharacterModels.settings.player_faces_cursor_ui) { 
            entityMouseX -= mouseX;
            entityMouseY -= mouseY;
        } else {
            entityMouseX -= this.width/2-13.5f;
            entityMouseY -= this.height/2+80;
        }

        if (PixelsCharacterModels.settings.show_block_under_player_ui) {
            drawEntityOnBlock(this.width/2, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, this.client.player);
        } else {
            drawEntity(this.width/2, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, this.client.player);
        }

        drawColor(matrices, 0, 0, 120, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 120, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 119, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);
        super.render(matrices, mouseX, mouseY, delta);
    }
    
}
