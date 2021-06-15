package me.PixelDots.PixelsCharacterModels.client.model.global;

import me.PixelDots.PixelsCharacterModels.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class SetGlobalModel
{

	
	public void setScale(float V) {
		PlayerEntity player = Minecraft.getInstance().player;
		Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale = V;
	}

	
	public float getScale() {
		PlayerEntity player = Minecraft.getInstance().player;
		return Main.Data.playerData.get(player.getUniqueID()).data.GlobalScale;
	}

	
	public void setX(float V) {
	}
	
	public float getX() {
		return 0;
	}

	
	public void setY(float V) {
		
	}
	
	public float getY() {
		return 0;
	}

	
	public void setZ(float V) {
		
	}
	
	public float getZ() {
		return 0;
	}

	
	public void setShow(boolean V) {
	}
	
	public boolean getShow() {
		return false;
	}
	
}
