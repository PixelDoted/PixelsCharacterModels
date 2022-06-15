package me.pixeldots.pixelscharactermodels.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.lang.Override;

import com.mojang.blaze3d.platform.TextureUtil;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class CustomPartTexture extends ResourceTexture {

    private final WeakReference<ByteBuffer> _data;

    public CustomPartTexture(Identifier location, ByteBuffer data) {
        super(location);
        if (data == null)
            throw new IllegalArgumentException("buffer must not be null");

        _data = new WeakReference<>(data);
    }
    
    public ByteBuffer getData() {
        return _data.get();
    }

    public Identifier getLocation() {
        return location;
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        clearGlId();

        ByteBuffer buf;
        if ((buf = _data.get()) == null) // gc
            throw new FileNotFoundException(getLocation().toString());

        try (NativeImage image = NativeImage.read(buf.duplicate())) {
            synchronized (this) {
                TextureUtil.prepareImage(getGlId(), 0, image.getWidth(), image.getHeight());
                image.upload(0, 0, 0, false);
            }
        }
    }

}