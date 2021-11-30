package me.PixelDots.PixelsCharacterModels.client.gui.Handlers.Widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class Button extends net.minecraft.client.gui.widget.button.Button
{
	
	public IPressable pressable = null;
	
	public Button(int X, int Y, int WIDTH, int HEIGHT, String TEXT, IPressable pressable) {
		super(X, Y, WIDTH, HEIGHT, new StringTextComponent(TEXT), null);
		this.pressable = pressable;
	}
	
	public Button(int X, int Y, int WIDTH, int HEIGHT, ITextComponent TEXT, IPressable pressable) {
		super(X, Y, WIDTH, HEIGHT, TEXT, null);
		this.pressable = pressable;
	}
	
	public abstract static interface IPressable {
		public abstract void onPress(Button btn);
	}
	
	public void setMessage(String s) {
		this.func_238482_a_(new StringTextComponent(s));
	}
	
	public void setMessage(ITextComponent s) {
		this.func_238482_a_(s);
	}
	
	public String getMessage() {
		return this.func_230458_i_().getString();
	}
	
	public ITextComponent IgetMessage() {
		return this.func_230458_i_();
	}
	
	public void onPress() {
		this.pressable.onPress(this);
	}
	
	public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
		this.onPress();
	}
	
	protected boolean clicked(double p_clicked_1_, double p_clicked_3_) {
		return this.field_230693_o_ && this.field_230694_p_ && p_clicked_1_ >= (double)this.field_230690_l_ && p_clicked_3_ >= (double)this.field_230691_m_ && p_clicked_1_ < (double)(this.field_230690_l_ + this.field_230688_j_) && p_clicked_3_ < (double)(this.field_230691_m_ + this.field_230689_k_);
	}
	
	protected boolean isValidClickButton(int p_isValidClickButton_1_) {
		return p_isValidClickButton_1_ == 0;
	}
	
	public void playDownSound(SoundHandler p_playDownSound_1_) {
		p_playDownSound_1_.play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	@Override
	public boolean func_231044_a_(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (this.field_230693_o_ && this.field_230694_p_) {
	        if (this.isValidClickButton(p_mouseClicked_5_)) {
	           boolean flag = this.clicked(p_mouseClicked_1_, p_mouseClicked_3_);
	           if (flag) {
	              this.playDownSound(Minecraft.getInstance().getSoundHandler());
	              this.onClick(p_mouseClicked_1_, p_mouseClicked_3_);
	              return true;
	            }
	    	}
	    	return false;
	    } else {
	    	return false;
	    }
		//return super.func_231044_a_(p_231044_1_, p_231044_3_, p_231044_5_);
	}

}
