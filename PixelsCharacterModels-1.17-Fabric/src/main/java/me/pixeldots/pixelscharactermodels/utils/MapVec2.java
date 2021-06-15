package me.pixeldots.pixelscharactermodels.utils;

import net.minecraft.util.math.Vec2f;

public class MapVec2 {

	public float X = 0;
	public float Y = 0;
	
	public MapVec2() {}
	
	public MapVec2(float X, float Y) {
		this.X = X;
		this.Y = Y;
	}
	
	public MapVec2(String X, String Y) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
	}
	
	public MapVec2 minus(float X, float Y) {
		this.X -= X;
		this.Y -= Y;
		return this;
	}
	
	public MapVec2 add(float X, float Y) {
		this.X += X;
		this.Y += Y;
		return this;
	}
	
	public MapVec2 times(float X, float Y) {
		this.X *= X;
		this.Y *= Y;
		return this;
	}
	
	public Vec2f toVec() {
		return new Vec2f(X, Y);
	}
	
}
