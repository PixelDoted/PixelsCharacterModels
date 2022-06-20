package me.pixeldots.pixelscharactermodels.utils.data;

import java.util.ArrayList;
import java.util.List;

import me.pixeldots.pixelscharactermodels.Animation.PCMFrames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

public class FramesData {
	
	public String name = "";
	public float TimePerFrame = 1;
	public boolean Loop = false;
	public List<String> Frames = new ArrayList<String>();
	
	public PCMFrames convertToFrames(PlayerEntity entity, PlayerEntityModel<?> model) {
		PCMFrames frames = new PCMFrames();
		frames.name = this.name;
		frames.TimePerFrame = this.TimePerFrame;
		frames.Loop = this.Loop;
		frames.frames = this.Frames;
		return frames;
	}
	
}
