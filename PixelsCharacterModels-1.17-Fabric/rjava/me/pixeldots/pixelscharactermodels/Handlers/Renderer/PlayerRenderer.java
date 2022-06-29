package me.pixeldots.pixelscharactermodels.Handlers.Renderer;

import me.pixeldots.pixelscharactermodels.Handlers.FramesHandler;
import me.pixeldots.pixelscharactermodels.Handlers.RenderingHandler;

import java.util.List;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import me.pixeldots.pixelscharactermodels.PlayerData;
import me.pixeldots.pixelscharactermodels.Handlers.Renderer.PlayerRenderer;
import me.pixeldots.pixelscharactermodels.accessors.PlayerModelAccessor;
import me.pixeldots.pixelscharactermodels.model.ModelPartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerRenderer {
    
    private RenderingHandler m_handler;

    public PlayerRenderer(RenderingHandler _handler) {
        this.m_handler = _handler;
    }

    public void playerRenderHead(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (!PixelsCharacterModels.PlayerDataList.containsKey(entity.getUuid())) PixelsCharacterModels.PlayerDataList.put(entity.getUuid(), new PlayerData(entity, model));
		FramesHandler.UpdateFrames(model, entity);
		m_handler.cubioud.currentPlayerParts = 0; 
		m_handler.cubioud.currentPlayerTris = 0;
	}
	
	public void playerRenderTail(PlayerEntityModel<?> model, PlayerEntity entity, LivingEntityRenderer<?,?> renderer) {
		if (PixelsCharacterModels.PlayerDataList.containsKey(entity.getUuid())) 
			setPlayerModelPartsData(model, entity);
	}

    public void setPlayerModelPartsData(PlayerEntityModel<?> model, PlayerEntity entity) {
		PlayerData data = PixelsCharacterModels.PlayerDataList.get(entity.getUuid());
		List<ModelPart> parts = ((PlayerModelAccessor)model).getParts();
		for (ModelPart part : parts) {
			if (!data.limbs.containsKey(part)) continue;

			ModelPartData partdata = data.limbs.get(part);
			if (partdata.player != null) continue;
			
			partdata.player = entity.getUuid();
		}
		if (data.limbs.containsKey(model.hat)) data.limbs.get(model.hat).setCopyFromPart(model.head);
		if (data.limbs.containsKey(model.jacket)) data.limbs.get(model.jacket).setCopyFromPart(model.body);
		if (data.limbs.containsKey(model.leftPants)) data.limbs.get(model.leftPants).setCopyFromPart(model.leftLeg);
		if (data.limbs.containsKey(model.rightPants)) data.limbs.get(model.rightPants).setCopyFromPart(model.rightLeg);
		if (data.limbs.containsKey(model.leftSleeve)) data.limbs.get(model.leftSleeve).setCopyFromPart(model.leftArm);
		if (data.limbs.containsKey(model.rightSleeve)) data.limbs.get(model.rightSleeve).setCopyFromPart(model.rightArm);
	}

}
