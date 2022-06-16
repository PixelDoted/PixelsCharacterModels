package me.pixeldots.pixelscharactermodels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.EditorGui;
import me.pixeldots.pixelscharactermodels.gui.PresetsGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

//@Mixin(Screen.class)
public abstract class ScreenMixin {
    // useless, this is useless

    /*@Shadow abstract Element addDrawableChild(Element drawableElement);

    @Inject(at = @At("RETURN"), method = "init(Lnet/minecraft/client/MinecraftClient;II)V")
	private void init(MinecraftClient client, int width, int height, CallbackInfo info) {
        if ((Object)this instanceof TitleScreen) {
            ButtonWidget widget = new ButtonWidget(5, 5, 100, 10, Text.of("Model"), (btn) -> {
                LivingEntity entity = null;

                if (PCMMain.settings.keybinding_opens_editor) PCMClient.minecraft.setScreen(new EditorGui(entity));
		        else PCMClient.minecraft.setScreen(new PresetsGui(entity));
            });
            this.addDrawableChild(widget);
        }
	}*/
}
