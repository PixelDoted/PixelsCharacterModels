package me.pixeldots.pixelscharactermodels.files.old;

import net.minecraft.world.phys.Vec2;

@Deprecated
public class OldMapVec2 {

	public float X = 0;
	public float Y = 0;
	
	public OldMapVec2() {}
	
	public OldMapVec2(float X, float Y) {
		this.X = X;
		this.Y = Y;
	}
	
	public OldMapVec2(String X, String Y) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
	}
	
	public OldMapVec2 minus(float X, float Y) {
		this.X -= X;
		this.Y -= Y;
		return this;
	}
	
	public OldMapVec2 add(float X, float Y) {
		this.X += X;
		this.Y += Y;
		return this;
	}
	
	public OldMapVec2 times(float X, float Y) {
		this.X *= X;
		this.Y *= Y;
		return this;
	}
	
	public Vec2 toVec() {
		return new Vec2(X, Y);
	}
	
}
