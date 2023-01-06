package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class FloatFieldWidget extends TextFieldWidget {

    public float value = 0;
    public float max_value = 0;
    public boolean only_positive = true;

    public FloatFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, float _value) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.setText(String.valueOf(_value));
        this.value = _value;
    }

    public FloatFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, float _value, float _maxvalue) {
        this(textRenderer, x, y, width, height, _value);
        max_value = _maxvalue;
    }

    public float getNumber() {
        return PCMUtils.getFloat(this.getText());
    }

    public void setNumber(float _value) {
        this.value = only_positive && _value < 0 ? 0 : _value;
        if (max_value != 0) {
            if (this.value > max_value) this.value = max_value;
            else if (!only_positive && this.value < -max_value) this.value = -max_value;
        }
        super.setText(String.valueOf(this.value));
    }

    @Override
    public void setText(String text) {
        if (!PCMUtils.isFloat(text)) return;
        setNumber(PCMUtils.getFloat(text));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.getWidth()) && mouseY < (double)(this.getY() + this.getHeight()))
            setNumber(value+Math.round(amount));
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void write(String text) {
        String s = this.getText();
        super.write(text);

        String current = this.getText();
        if (current.equals("") || current.equals("-") || current.equals(".") || current.equals("-.")) this.value = 0;
        else if (!PCMUtils.isFloat(current)) super.setText(s);
        else { 
            this.value = PCMUtils.getFloat(text);
            if (max_value == 0) return;
            if (this.value > max_value) setNumber(max_value);
            else if (!only_positive && this.value < -max_value) setNumber(-max_value);
        }
    }
    
}
