package me.pixeldots.pixelscharactermodels.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationFile {
    
    public boolean loop = false;
    public float framerate = 60;
    public List<Frame> frames = new ArrayList<>();

    public float getFPSDifference(float fps) {
        return 1f/(fps/framerate);
    }

    public AnimationFile() {
        frames.add(new Frame());
    }

    public class Frame {
        public String script = "";
        public Map<String, String> parts = new HashMap<>();
        public float run_frame = 0;
    }

}
