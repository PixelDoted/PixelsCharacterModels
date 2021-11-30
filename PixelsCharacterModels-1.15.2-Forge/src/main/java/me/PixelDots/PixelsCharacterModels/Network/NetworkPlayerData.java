package me.PixelDots.PixelsCharacterModels.Network;

import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class NetworkPlayerData 
{
	
	public GlobalModelData data = null;
	public String username = "";
	public float playerHeight = 1.8f;
	public float playerWidth = 0.6f;
	
	public PartModelRenderer FakeHead = null;
	public PartModelRenderer FakeBody = null;
	public PartModelRenderer FakeLeftArm = null;
	public PartModelRenderer FakeRightArm = null;
	public PartModelRenderer FakeLeftLeg = null;
	public PartModelRenderer FakeRightLeg = null;
	
	public NetworkPlayerData() {
		FakeHead = null;
		FakeBody = null;
		FakeLeftArm = null;
		FakeRightArm = null;
		FakeLeftLeg = null;
		FakeRightLeg = null;
		data = new GlobalModelData();
		username = "";
		playerHeight = 1.8f;
		playerWidth = 0.6f;
	}
	
}
