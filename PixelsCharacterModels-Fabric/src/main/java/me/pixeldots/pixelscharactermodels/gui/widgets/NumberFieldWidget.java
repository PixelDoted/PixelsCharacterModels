package me.pixeldots.pixelscharactermodels.gui.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NumberFieldWidget extends TextFieldWidget {

    public float value = 0;
    public boolean show_decimal = true;

    public NumberFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, float _value) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.setText(String.valueOf(_value));
        this.value = _value;
    }

    public NumberFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, float _value, boolean _showdecimal) {
        this(textRenderer, x, y, width, height, _value);
        this.show_decimal = _showdecimal;
    }

    public float getNumber() {
        String text = this.getText();
        try {
            value = Float.parseFloat(text);
        } catch (NumberFormatException e) {}
        return value;
    }

    public void setNumber(float _value) {
        this.value = _value;
        this.setText(String.valueOf(value));
    }

    @Override
    public void setText(String text) {
        if (!show_decimal) text = text.substring(0, text.indexOf("."));
        super.setText(text);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height))
            setNumber(value+Math.round(amount));
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
}
