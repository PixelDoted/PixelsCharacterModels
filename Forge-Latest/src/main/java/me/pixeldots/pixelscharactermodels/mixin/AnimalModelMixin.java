package me.pixeldots.pixelscharactermodels.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationPlayer;
import me.pixeldots.scriptedmodels.ScriptedModels;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;

@Mixin(AgeableListModel.class)
public class AnimalModelMixin {
    
    // update the entity's current animation
    @Inject(method = "renderToBuffer", at = @At("TAIL"))
    public void render(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
        UUID uuid = ScriptedModels.Rendering_Entity.getUUID();
        if (PCMClient.EntityAnimationList.containsKey(uuid)) { // check if the entity is playing an animation
            AnimationPlayer player = PCMClient.EntityAnimationList.get(uuid); // get the animation player

            float frame_diff = player.animation.getFPSDifference(Integer.parseInt(PCMClient.minecraft.fpsString)); // get the next frame value
            player.updateCurrent(frame_diff, ScriptedModels.Rendering_Entity, (EntityModel<?>)(Object)this); // update the current frame index
        }
    }

}
