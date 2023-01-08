package me.pixeldots.pixelscharactermodels.mixin;

//@Mixin(Screen.class)
public abstract class ScreenMixin {
    // useless, this is useless

    /*@Shadow abstract Element addDrawableChild(Element drawableElement);

    @Inject(at = @At("RETURN"), method = "init(Lnet/minecraft/client/MinecraftClient;II)V")
	private void init(MinecraftClient client, int width, int height, CallbackInfo info) {
        if ((Object)this instanceof TitleScreen) {
            ButtonWidget widget = new FlatButtonWidget(5, 5, 100, 10, Text.of("Model"), (btn) -> {
                LivingEntity entity = null;

                if (PCMMain.settings.keybinding_opens_editor) PCMClient.minecraft.setScreen(new EditorGui(entity));
		        else PCMClient.minecraft.setScreen(new PresetsGui(entity));
            });
            this.addDrawableChild(widget);
        }
	}*/
}
