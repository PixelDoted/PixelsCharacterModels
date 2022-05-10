package me.pixeldots.pixelscharactermodels.Handlers;

import com.mojang.authlib.GameProfile;

import me.pixeldots.pixelscharactermodels.Handlers.Renderer.CubioudRenderer;
import me.pixeldots.pixelscharactermodels.Handlers.Renderer.PlayerRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
  
public class RenderingHandler {
	
	public GameProfile currentPlayerRendering = null;

	public CubioudRenderer cubioud;
	public PlayerRenderer player;

	public RenderingHandler() {
		cubioud = new CubioudRenderer(this);
		player = new PlayerRenderer(this);
	}
	
	public void renderItemHead(LivingEntity entity, ItemStack stack, MatrixStack matrices) {
	}
	
	public void renderItemTail(LivingEntity entity, ItemStack stack, MatrixStack matrices) {
	}

}
