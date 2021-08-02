package me.pixeldots.pixelscharactermodels.Animation;

import java.util.ArrayList;
import java.util.List;

import me.pixeldots.pixelscharactermodels.utils.data.FramesData;

public class PCMFrames {
	
	public String name = "";
	public float TimePerFrame = 1;
	public boolean Loop = false;
	
	public List<String> frames = new ArrayList<String>();
	
	public PCMFrames() {}
	public PCMFrames(String name) {
		this.name = name;
	}
	
	public FramesData convertToData() {
		FramesData data = new FramesData();
		data.name = this.name;
		data.TimePerFrame = this.TimePerFrame;
		data.Loop = this.Loop;
		data.Frames = this.frames;
		return data;
	}
	
}
