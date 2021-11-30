package me.PixelDots.PixelsCharacterModels.Model.Capabilities.model;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ModelPathProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IModelPath.class)
    public static final Capability<IModelPath> MODEL_LOC = null;

    private LazyOptional<IModelPath> instance = LazyOptional.of(MODEL_LOC::getDefaultInstance);
    
    
    
    @Override
    public INBT serializeNBT() {
        return (INBT) MODEL_LOC.getStorage().writeNBT(MODEL_LOC, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        MODEL_LOC.getStorage().readNBT(MODEL_LOC, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return MODEL_LOC.orEmpty(cap, instance);
	}

}