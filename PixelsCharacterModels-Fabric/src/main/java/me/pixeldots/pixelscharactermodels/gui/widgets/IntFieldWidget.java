package me.pixeldots.pixelscharactermodels.gui.widgets;

import me.pixeldots.pixelscharactermodels.other.PCMUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class IntFieldWidget extends TextFieldWidget {

    public int value = 0;
    public boolean only_positive = true;

    public IntFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, int _value) {
        super(textRenderer, x, y, width, height, Text.of(""));
        this.setText(String.valueOf(_value));
        this.value = _value;
    }

    public int getNumber() {
        return PCMUtils.getInt(this.getText());
    }

    public void setNumber(int _value) {
        this.value = only_positive && _value < 0 ? 0 : _value;
        this.setText(String.valueOf(this.value));
    }

    @Override
    public void setText(String text) {
        if (!PCMUtils.isInt(text)) return;
        this.value = PCMUtils.getInt(text);
        super.setText(text);
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
        else this.value = PCMUtils.getInt(text);
    }
    
}
