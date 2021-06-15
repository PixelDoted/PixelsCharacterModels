package me.PixelDots.PixelsCharacterModels.KeyBindings;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;

public enum KeyBindings 
{
	
	CharCreatorMenu("key.pcm.charcremenu", GLFW.GLFW_KEY_R),
	Anim1BTN("key.pcm.anim1btn", GLFW.GLFW_KEY_X),
	Anim2BTN("key.pcm.anim2btn", GLFW.GLFW_KEY_C),
	Anim3BTN("key.pcm.anim3btn", GLFW.GLFW_KEY_V),
	Anim4BTN("key.pcm.anim4btn", GLFW.GLFW_KEY_B),
	Anim5BTN("key.pcm.anim5btn", GLFW.GLFW_KEY_N);
	
	private final KeyBinding keybinding;
	
	private KeyBindings(String keyName, int defualtKeyCode) {
		keybinding = new KeyBinding(keyName,defualtKeyCode, "key.categories.PixelsCharacterModels");
	}
	
	public KeyBinding getKeybind() {
		return keybinding;
	}
	
	public boolean isPressed() {
		return keybinding.isPressed();
	}
	
}