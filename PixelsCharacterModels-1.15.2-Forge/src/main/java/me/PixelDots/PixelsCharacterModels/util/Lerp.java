package me.PixelDots.PixelsCharacterModels.util;

import net.minecraft.util.math.MathHelper;

public class Lerp 
{
	
	public float Linear(float a, float b, float t) {
		/*float end = (b < 0 ? b - 0.01f : b + 0.01f);
		if (b == 0) {
			if (a < 0) b += 0.01f;
			else if (a > 0) b -= 0.01f;
		}*/
		float end = b;
		float v = (float) MathHelper.lerp(t, a, end);
		if (v > b && v > a) { v = b; }
		else if (v < b && v < a) { v = b; }
		if (a == b) v = b;
		if (Utillities.sameInRange(v, end, 0.02f)) v = end;
		return v;
	}
	
	public float Curve(float a, float b, float t) {
		/*float end = (b < 0 ? b - 0.01f : b + 0.01f);
		if (b == 0) {
			if (a < 0) b += 0.01f;
			else if (a > 0) b -= 0.01f;
		}*/
		float end = b;
		float v = (float) MathHelper.lerp(t, a, end);
		if (v > b && v > a) { v = b; }
		else if (v < b && v < a) { v = b; }
		if (a == b) v = b;
		if (Utillities.sameInRange(v, end, 0.02f)) v = end;
		return v;
	}
	
	/*public static float Linear(float a, float b, float t) {
		float time = ((t/1.75f)*100f);
		float end = (b != 0 ? b : 0);
		
		if (b == 0) { if (a > 0) {b = -1;} if (a < 0) {b = 1;} }
		float value = ( a == b ? b : (a+(b / time)) );
		
		if (value > end && value > a) { value = end; }
		else if (value < end && value < a) { value = end; }
		return value;
	}*/
	
	/*public float oldLinear(float a, float b, float t) {
		float time = (t*100f);
		float value = ( a == b ? b : (a+(b / time)) );
		if (value > b && value > a) {
			value = b;
		}
		else if (value < b && value < a) {
			value = b;
		}
		return value;
	}
	
	public float oldCurve(float a, float b, float t) {
		float time = (t*100f) * (a-b)/b;
		float value = ( a == b ? a : ((b / -time)+a) );
		if (value > b && value > a) {
			value = b;
		}
		else if (value < b && value < a) {
			value = b;
		}
		return value;
	}*/
	
}