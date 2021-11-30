package me.PixelDots.PixelsCharacterModels.Model.Capabilities.model;

import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ModelPathStorage implements Capability.IStorage<IModelPath> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IModelPath> capability, IModelPath instance, Direction side) {
    	return StringNBT.valueOf(instance.getModel());
    }

    @Override
    public void readNBT(Capability<IModelPath> capability, IModelPath instance, Direction side, INBT nbt) {
        if(nbt instanceof StringNBT) {
            instance.setModel(((StringNBT) nbt).getString());
        }
    }
    
}