package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.network.chat.Component;

public class ToggleWidget extends OffsetFlatButtonWidget {

    public boolean toggled = false;
    public ToggleAction on_toggle;
    public String base_message;

    public ToggleWidget(int x, int y, int width, int height, String message, boolean _toggled,  ToggleAction _ontoggle) {
        super(x, y, width, height, Component.literal((_toggled ? "[X] " : "[ ] ") + message), (btn) -> {});
        this.base_message = message;
        this.on_toggle = _ontoggle;
    }

    @Override
    public void onPress() {
        toggled = !toggled;
        this.setMessage(Component.literal((toggled ? "[X] " : "[ ] ") + base_message));
        on_toggle.invoke(toggled);
    }

    public interface ToggleAction {
        void invoke(boolean toggled);
    }

}
