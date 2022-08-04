package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class FloatFieldWidget extends EditBox {

    public float value = 0;
    public float max_value = 0;
    public boolean only_positive = true;

    public FloatFieldWidget(Font textRenderer, int x, int y, int width, int height, float _value) {
        super(textRenderer, x, y, width, height, Component.literal(""));
        this.setMessage(Component.literal(String.valueOf(_value)));
        this.value = _value;
    }

    public FloatFieldWidget(Font textRenderer, int x, int y, int width, int height, float _value, float _maxvalue) {
        this(textRenderer, x, y, width, height, _value);
        max_value = _maxvalue;
    }

    public float getNumber() {
        return PCMUtils.getFloat(this.getMessage().getString());
    }

    public void setNumber(float _value) {
        this.value = only_positive && _value < 0 ? 0 : _value;
        if (max_value != 0) {
            if (this.value > max_value) this.value = max_value;
            else if (!only_positive && this.value < -max_value) this.value = -max_value;
        }
        super.setMessage(Component.literal(String.valueOf(this.value)));
    }

    @Override
    public void setMessage(Component text) {
        if (!PCMUtils.isFloat(text.getString())) return;
        setNumber(PCMUtils.getFloat(text.getString()));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height))
            setNumber(value+Math.round(amount));
        
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void insertText(String text) {
        String s = this.getMessage().getString();
        super.insertText(text);

        String current = this.getMessage().getString();
        if (current.equals("") || current.equals("-") || current.equals(".") || current.equals("-.")) this.value = 0;
        else if (!PCMUtils.isFloat(current)) super.setMessage(Component.literal(s));
        else { 
            this.value = PCMUtils.getFloat(text);
            if (max_value == 0) return;
            if (this.value > max_value) setNumber(max_value);
            else if (!only_positive && this.value < -max_value) setNumber(-max_value);
        }
    }
    
}
