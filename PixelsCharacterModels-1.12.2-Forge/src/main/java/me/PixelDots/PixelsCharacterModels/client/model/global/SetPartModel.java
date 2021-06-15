package me.PixelDots.PixelsCharacterModels.client.model.global;

import me.PixelDots.PixelsCharacterModels.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class SetPartModel
{
	
	
	public void setScale(float V, String part) {
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Scale = V;
				break;
			}
		}
	}
	
	public float getScale(String part) {
		float v = 0;
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				v = Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Scale;
				break;
			}
		}
		return v;
	}

	
	public void setX(float V, String part) {
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).X = V;
				break;
			}
		}
	}
	
	public float getX(String part) {
		float v = 0;
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				v = Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).X;
				break;
			}
		}
		return v;
	}

	
	public void setY(float V, String part) {
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Y = V;
				break;
			}
		}
	}
	
	public float getY(String part) {
		float v = 0;
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				v = Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Y;
				break;
			}
		}
		return v;
	}

	
	public void setZ(float V, String part) {
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Z = V;
				break;
			}
		}
	}
	
	public float getZ(String part) {
		float v = 0;
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				v = Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Z;
				break;
			}
		}
		return v;
	}

	
	public void setShow(boolean V, String part) {
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Show = V;
				break;
			}
		}
	}
	
	public boolean getShow(String part) {
		boolean v = true;
		PlayerEntity player = Minecraft.getInstance().player;
		for (int i = 0; i < Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.size(); i++) {
			if (part == Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).name) {
				v = Main.Data.playerData.get(player.getUniqueID()).data.parts.Limb.get(i).Show;
				break;
			}
		}
		return v;
	}
	
}
