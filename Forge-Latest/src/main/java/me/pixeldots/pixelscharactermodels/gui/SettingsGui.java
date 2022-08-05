package me.pixeldots.pixelscharactermodels.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.handlers.EntityGuiHandler;
import me.pixeldots.pixelscharactermodels.gui.handlers.GuiHandler;
import me.pixeldots.pixelscharactermodels.gui.widgets.FlatButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.ToggleWidget;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.loading.FMLLoader;

public class SettingsGui extends EntityGuiHandler {

    public float entityViewScale = 75;

    public String selectedPreset = "";
    public List<Button> presetButtons = new ArrayList<>();

    public static String path_offset = "";

    private String version = "";

    public SettingsGui(LivingEntity _entity) {
        super("Settings");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
        uuid = _entity.getUUID();
        version = FMLLoader.getLoadingModList().getModFileById("pcm").versionString();
    }

    public SettingsGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        path_offset = "";
        this.minecraft.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();

        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text("pcm.menu.Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text("pcm.menu.Editor"), (btn) -> {
            setScreen(new EditorGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text("pcm.menu.Animation"), (btn) -> {
            setScreen(new AnimationGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text("pcm.menu.Settings"), (btn) -> {})).active = false;

        addButton(new FlatButtonWidget(5, 15, 230, 10, Text("pcm.gui.Save"), (btn) -> {
            PCMMain.settings.save(PCMMain.SettingsPath);
        }));
        addDrawableElement(new ToggleWidget(5, 30, 230, 10, String("pcm.setting.ShowBlockUnderPlayer"), PCMMain.settings.show_block_under_player_ui, (val) -> {
            PCMMain.settings.show_block_under_player_ui = val;
        }));
        addDrawableElement(new ToggleWidget(5, 45, 230, 10, String("pcm.setting.PlayerFacesCursor"), PCMMain.settings.player_faces_cursor_ui, (val) -> {
            PCMMain.settings.player_faces_cursor_ui = val;
        }));
        addDrawableElement(new ToggleWidget(5, 60, 230, 10, String("pcm.setting.KeybindingOpensEditor"), PCMMain.settings.keybinding_opens_editor, (val) -> {
            PCMMain.settings.keybinding_opens_editor = val;
        }));
        addDrawableElement(new ToggleWidget(5, 75, 230, 10, String("pcm.setting.PreviewPreset"), PCMMain.settings.preview_preset, (val) -> {
            PCMMain.settings.preview_preset = val;
        }));
        addDrawableElement(new ToggleWidget(5, 90, 230, 10, String("pcm.setting.RadiansInsteadOfDegress"), PCMMain.settings.radians_instead_of_degress, (val) -> {
            PCMMain.settings.radians_instead_of_degress = val;
        }));
        addDrawableElement(new ToggleWidget(5, 105, 230, 10, String("pcm.setting.ShowNameTags"), PCMMain.settings.show_nametags, (val) -> {
            PCMMain.settings.show_nametags = val;
        }));
        addDrawableElement(new ToggleWidget(5, 120, 230, 10, String("pcm.setting.ShowArmor"), PCMMain.settings.show_armor, (val) -> {
            PCMMain.settings.show_armor = val;
        }));

        EditBox A = addTextField(new EditBox(textRenderer, 5+60, 150, 170, 10, Component.literal("")));
        A.setResponder((s) -> { PCMMain.settings.animations[0] = s; }); A.setValue(PCMMain.settings.animations[0]);
        EditBox B = addTextField(new EditBox(textRenderer, 5+60, 165, 170, 10, Component.literal("")));
        B.setResponder((s) -> { PCMMain.settings.animations[1] = s; }); B.setValue(PCMMain.settings.animations[1]);
        EditBox C = addTextField(new EditBox(textRenderer, 5+60, 180, 170, 10, Component.literal(""))); 
        C.setResponder((s) -> { PCMMain.settings.animations[2] = s; }); C.setValue(PCMMain.settings.animations[2]);
        EditBox D = addTextField(new EditBox(textRenderer, 5+60, 195, 170, 10, Component.literal(""))); 
        D.setResponder((s) -> { PCMMain.settings.animations[3] = s; }); D.setValue(PCMMain.settings.animations[3]);
        EditBox E = addTextField(new EditBox(textRenderer, 5+60, 210, 170, 10, Component.literal(""))); 
        E.setResponder((s) -> { PCMMain.settings.animations[4] = s; }); E.setValue(PCMMain.settings.animations[4]);


        addButton(new FlatButtonWidget(5, this.height-30, 230, 10, Text("pcm.gui.RequestEntityData"), (btn) -> {
            ClientNetwork.request_entitys();
        }));
        addButton(new FlatButtonWidget(5, this.height-15, 230, 10, Text("pcm.gui.ClearEntityData"), (btn) -> {
            SkinHelper.clearSkins();
            PCMClient.EntityAnimationList.clear();
            ClientHelper.reset_entity(this.uuid);
        }));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= 240 && mouseX < this.width) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        float entityMouseX = 0;
        float entityMouseY = 0;

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX = (float)(this.width/2)+120 - mouseX;
            entityMouseY = (float)(this.height/2+37-125) - mouseY;
        }

        if (entity != null)
            drawEntity(this.width/2+120, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity, PCMMain.settings.show_block_under_player_ui);

        drawColor(matrices, 0, 0, 240, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 240, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 239, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);

        drawString(matrices, "Animation 1", 5, 145);
        drawString(matrices, "Animation 2", 5, 160);
        drawString(matrices, "Animation 3", 5, 175);
        drawString(matrices, "Animation 4", 5, 190);
        drawString(matrices, "Animation 5", 5, 205);
        
        drawString(matrices, textRenderer, Component.literal("Version: " + version), 250, this.height-15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        path_offset = "";
        super.onClose();
    }

}
