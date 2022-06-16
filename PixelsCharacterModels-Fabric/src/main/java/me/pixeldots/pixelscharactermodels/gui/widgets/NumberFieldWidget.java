package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NumberFieldWidget extends TextFieldWidget {

    public float value = 0;
    public boolean show_decimal = false;
    public boolean only_positive = true;

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
        return PCMUtils.getFloat(this.getText());
    }

    public void setNumber(float _value) {
        this.value = only_positive && _value < 0 ? 0 : _value;
        this.setText(String.valueOf(this.value));
    }

    @Override
    public void setText(String text) {
        super.setText(!show_decimal ? text.substring(0, text.indexOf(".")) : text);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height))
            setNumber(value+Math.round(amount));
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void write(String text) {
        float v = PCMUtils.getFloat(text);
        this.value = only_positive && v < 0 ? 0 : v;

        String s = String.valueOf(this.value);
        super.write(!show_decimal ? s.substring(0, s.indexOf(".")) : s);
    }
    
}
