package me.pixeldots.pixelscharactermodels.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Mixin(PlayerEntityModel.class)
public interface PlayerModelAccessor {

	@Accessor("parts")
	List<ModelPart> getParts();
	
}
