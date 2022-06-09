package me.pixeldots.pixelscharactermodels.gui.widgets;


import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import net.minecraft.text.Text;

public class NodeButtonWidget extends AButtonWidget {

    public DragAction action;

    public boolean isDragging = false;
    public double DragDistance = 0;
    public int lastScroll = 0;

    public NodeButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, DragAction onDrag) {
        super(x, y, width, height, message, onPress);
        this.action = onDrag;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            DragDistance += deltaY/(this.height+5d);
        } else if (!EditorGui.isDragging && this.clicked(mouseX, mouseY)) {
            isDragging = true;
            EditorGui.isDragging = true;
            DragDistance = deltaY/(this.height+5d);
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
