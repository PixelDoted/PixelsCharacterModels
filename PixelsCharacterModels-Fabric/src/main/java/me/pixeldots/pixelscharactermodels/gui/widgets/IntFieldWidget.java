package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class IntFieldWidget extends TextFieldWidget {

    public int value = 0;
    public int max_value = 0;
    public boolean only_positive = true;

    public IntFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, int _value) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.setText(String.valueOf(_value));
        this.value = _value;
    }

    public IntFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, int _value, int _maxvalue) {
        this(textRenderer, x, y, width, height, _value);
        max_value = _maxvalue;
    }

    public int getNumber() {
        return PCMUtils.getInt(this.getText());
    }

    public void setNumber(int _value) {
        this.value = only_positive && _value < 0 ? 0 : _value;
        if (max_value != 0) {
            if (this.value > max_value) this.value = max_value;
            else if (!only_positive && this.value < -max_value) this.value = -max_value;
        }
        super.setText(String.valueOf(this.value));
    }

    @Override
    public void setText(String text) {
        if (!PCMUtils.isInt(text)) return;
        setNumber(PCMUtils.getInt(text));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height))
            setNumber((int)(value+Math.round(amount)));
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void write(String text) {
        String s = this.getText();
        super.write(text);

        if (!PCMUtils.isInt(this.getText())) super.setText(s);
        else { 
            this.value = PCMUtils.getInt(text);
            if (max_value == 0) return;
            if (this.value > max_value) setNumber(max_value);
            else if (!only_positive && this.value < -max_value) setNumber(-max_value);
        }
    }
    
    
}
