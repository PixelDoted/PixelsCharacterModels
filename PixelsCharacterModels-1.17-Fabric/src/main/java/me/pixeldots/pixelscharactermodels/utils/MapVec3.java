package me.pixeldots.pixelscharactermodels.utils;

import net.minecraft.util.math.Vec3f;

public class MapVec3 {
	
	public float X = 0;
	public float Y = 0;
	public float Z = 0;
	
	public MapVec3() {}
	
	public MapVec3(float X, float Y, float Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public MapVec3(double X, double Y, double Z) {
		this.X = (float)X;
		this.Y = (float)Y;
		this.Z = (float)Z;
	}
	
	public MapVec3(String X, String Y, String Z) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
		this.Z = Float.parseFloat(Z);
	}
	
	public MapVec3 minus(float X, float Y, float Z) {
		this.X -= X;
		this.Y -= Y;
		this.Z -= Z;
		return this;
	}
	
	public MapVec3 add(float X, float Y, float Z) {
		this.X += X;
		this.Y += Y;
		this.Z += Z;
		return this;
	}
	
	public MapVec3 times(float X, float Y, float Z) {
		this.X *= X;
		this.Y *= Y;
		this.Z *= Z;
		return this;
	}
	
	public float magnitude() {
		return this.X+this.Y+this.Z;
	}
	
	public Vec3f toVec() {
		return new Vec3f(X, Y, Z);
	}
	
	
}
