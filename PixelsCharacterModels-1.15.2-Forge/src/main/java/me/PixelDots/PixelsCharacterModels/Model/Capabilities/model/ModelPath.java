package me.PixelDots.PixelsCharacterModels.Model.Capabilities.model;

public class ModelPath implements IModelPath {

    private String data = "";

    @Override
    public String getModel() {
        return this.data;
    }

    @Override
    public void setModel(String data) {
        this.data = data == null ? "" : data;
    }
}