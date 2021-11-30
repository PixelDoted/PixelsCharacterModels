package me.PixelDots.PixelsCharacterModels.util;

import java.util.Random;
import java.util.UUID;

import it.unimi.dsi.fastutil.objects.ObjectList;
import me.PixelDots.PixelsCharacterModels.client.model.render.PartModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class Utillities 
{	
	
	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}
	
	public static boolean isNumeric(CharSequence cs) {
		if(isEmpty(cs))
			return false;
		
		int len = cs.length();
		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static PlayerEntity getPlayerFromUUID(UUID uuid) {
		return Minecraft.getInstance().world.getPlayerByUuid(uuid);
	}
	
	public static int RandomRange(int min, int max) {
		int value = new Random().nextInt();
		value = value < min ? min : value;
		value = value > max ? max : value;
		return value;
	}
	
	public static boolean sameInRange(float value, float end, float range) {
		if (value >= end-range && value  <= end+range) {
			return true;
		}
		return false;
	}
	
	//Child's ModelRenderer
	public static final String ChildrenField = "field_78805_m";
	
	@SuppressWarnings("unchecked")
	public static void removeChild(ModelRenderer model, int ID) {
		if (model instanceof PartModelRenderer) {
			((PartModelRenderer)model).childModels.remove(ID);
		} else if (ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField) instanceof ObjectList<?>) {
			((ObjectList<ModelRenderer>)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField)).remove(ID);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ModelRenderer getChild(ModelRenderer model, int ID) {
		if (model instanceof PartModelRenderer) {
			return ((PartModelRenderer)model).childModels.get(ID);
		} else if (ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField) instanceof ObjectList<?>) {
			return ((ObjectList<ModelRenderer>)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField)).get(ID);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static int childsSize(ModelRenderer model) {
		if (model instanceof PartModelRenderer) {
			return ((PartModelRenderer)model).childModels.size();
		} else if (ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField) instanceof ObjectList<?>) {
			return ((ObjectList<ModelRenderer>)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField)).size();
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public static void clearChildren(ModelRenderer model) {
		if (model instanceof PartModelRenderer) {
			((PartModelRenderer)model).childModels.clear();
		} else if (ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField) instanceof ObjectList<?>) {
			((ObjectList<ModelRenderer>)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, model, ChildrenField)).clear();
		}
<<<<<<< Updated upstream
		//Child's ModelRenderer
=======
	}
	
	public static void fixShowing(ModelRenderer model, PlayerEntity player) {
		for (int i = 0; i < Utillities.childsSize(model); i++)  {
			if (Utillities.getChild(model, i) != null) {
				if (Utillities.getChild(model, i) instanceof PartModelRenderer) {
					if (((PartModelRenderer)Utillities.getChild(model, i)).showThis == true) {
						if (((PartModelRenderer)Utillities.getChild(model, i)).uuid != player.getUniqueID()) {
							((PartModelRenderer)Utillities.getChild(model, i)).showThis = false;
						}
					}
				}
			}
		}
	}
	//Child's ModelRenderer
>>>>>>> Stashed changes
	
}
