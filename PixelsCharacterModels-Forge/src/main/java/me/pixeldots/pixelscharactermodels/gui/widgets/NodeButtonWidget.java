package me.pixeldots.pixelscharactermodels.gui.widgets;


import com.mojang.blaze3d.vertex.PoseStack;

import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import net.minecraft.network.chat.Component;

public class NodeButtonWidget extends FlatButtonWidget {

    public DragAction action;

    public boolean isDragging = false;
    public double DragDistance = 0;
    public int lastScroll = 0;

    public NodeButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, DragAction onDrag) {
        super(x, y, width, height, message, onPress);
        this.action = onDrag;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (isDragging) {
            int d = -(int)Math.round(lastScroll/15d);
            int move = ((int)Math.round(DragDistance) + d)*(height+1);
            hLine(matrices, x-11, x+width, y-1+move, 0xFFFFFFFF);
            hLine(matrices, x-11, x+width, y+height+move, 0xFFFFFFFF);
            vLine(matrices, x-11, y-1+move, y+height+move, 0xFFFFFFFF);
            vLine(matrices, x+width, y-1+move, y+height+move, 0xFFFFFFFF);
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            DragDistance += deltaY/(this.height+1);
        } else if (!EditorGui.isDragging && this.clicked(mouseX, mouseY)) {
            isDragging = true;
            EditorGui.isDragging = true;
            DragDistance = deltaY/(this.height+1);
            lastScroll = EditorGui.yscroll;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDragging) {
            isDragging = false;
            EditorGui.isDragging = false;
            action.drag((int)Math.round(DragDistance), lastScroll);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public interface DragAction {
        void drag(int nodeDistance, int lastScroll);
    }
    
}
