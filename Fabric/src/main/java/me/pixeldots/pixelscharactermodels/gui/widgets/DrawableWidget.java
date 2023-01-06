package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

public class DrawableWidget extends DrawableHelper implements Drawable, Element, Selectable {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean active = true;
    public boolean visible = true;
    public boolean focused = false;
    public boolean hovered = false;

    public DrawableWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.hovered = hovered(mouseX, mouseY);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder var1) {
    }

    @Override
    public SelectionType getType() {
        if (this.focused) {
            return SelectionType.FOCUSED;
        }

        if (this.hovered) {
            return SelectionType.HOVERED;
        }

        return SelectionType.NONE;
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (!this.active || !this.visible) {
            return false;
        }

        this.focused = !this.focused;
        return this.focused;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseX > this.x && mouseX < this.x+this.width && mouseY > this.y && mouseY < this.y+this.height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active) {
            return false;
        }

        if (button == 0 && this.clicked(mouseX, mouseY)) {
            MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.onClick();
            return true;
        }
        return false;
    }

    public boolean clicked(double mouseX, double mouseY) {
        return this.active && this.visible && hovered(mouseX, mouseY);
    }

    public boolean hovered(double mouseX, double mouseY) {
        return mouseX > this.x && mouseX < this.x+this.width && mouseY > this.y && mouseY < this.y+this.height;
    }

    public void onClick() {}
}
