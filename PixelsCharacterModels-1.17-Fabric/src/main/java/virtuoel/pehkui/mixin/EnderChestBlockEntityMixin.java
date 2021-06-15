package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double canPlayerUseModifyYOffset(double value, PlayerEntity player)
	{
		return value - player.getStandingEyeHeight();
	}
}
