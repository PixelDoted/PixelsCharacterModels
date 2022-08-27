package me.pixeldots.pixelscharactermodels.files;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class AnimationPlayer {
    
    public int index; // current frame index
    public float frame; // current frame

    public String name; // animation name
    public AnimationFile animation; // animation data

    private float last_frame = -1;

    public AnimationPlayer(AnimationFile _animation, String _name) {
        this.animation = _animation;
        this.name = _name;
    }

    // updates the current frame index
    public void updateCurrent(float _frame, LivingEntity entity, EntityModel<?> model) {
        last_frame = last_frame == -1f ? _frame : last_frame+(_frame-last_frame)*.0125f;
        this.frame += last_frame;

        boolean check = animation.frames.size() > index+1;
        if (animation.loop || check) {
            if (!check) index = 0;
            if (animation.frames.get(index).run_frame <= frame) {
                AnimationHelper.stop(entity, model, false);
                AnimationHelper.play(name, this, animation.frames.get(index+1), entity, model);
                index = index+1;
                this.frame = 0;
            } else if (animation.lerp) {
                AnimationHelper.stop(entity, model, false);
                AnimationHelper.play(name, this, animation.frames.get(index), entity, model);
            }
        } else AnimationHelper.stop(entity, model, true);
    }
    
}
