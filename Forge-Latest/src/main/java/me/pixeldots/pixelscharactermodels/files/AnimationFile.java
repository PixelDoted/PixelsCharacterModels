package me.pixeldots.pixelscharactermodels.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationFile {
    
    public boolean loop = false; // if the animations is looping
    public int framerate = 60; // the animations framerate
    public boolean lerp = true;
    public List<Frame> frames = new ArrayList<>(); // the animations frames
    public float version = 2;

    // get the frame from fps and framerate
    public float getFPSDifference(float fps) {
        if (fps == 0) return 0;
        return (1f/fps)*framerate;
    }

    public AnimationFile() {
        frames.add(new Frame());
    }

    public static class Frame {
        public String script = ""; // frame script
        public Map<String, String> parts = new HashMap<>(); // frame parts script
        public int run_frame = 0; // frames till run
        public boolean is_filler = false; // is this frame a filler frame
    }

}
