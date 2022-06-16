package me.pixeldots.pixelscharactermodels.files;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class AnimationPlayer {
    
    public int index;
    public float frame;

    public String name;
    public AnimationFile animation;

    public AnimationPlayer(AnimationFile _animation, String _name) {
        this.animation = _animation;
        this.name = _name;
    }

    public void updateCurrent(LivingEntity entity, EntityModel<?> model, float _frame, int _index) {
        if (animation.frames.size() > _index+1) {
            if (animation.frames.get(_index+1).run_frame <= _frame) {
                AnimationHelper.stop(entity, model, false);
                AnimationHelper.play(name, animation.frames.get(_index+1), entity, model);
                index = _index+1;
            }
        } else AnimationHelper.stop(entity, model, true);
    }

}
