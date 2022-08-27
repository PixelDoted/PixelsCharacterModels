package me.pixeldots.pixelscharactermodels.files.old;

import net.minecraft.util.math.Vec3f;

@Deprecated
public class OldMapVec3 {
	
	public float X = 0;
	public float Y = 0;
	public float Z = 0;
	
	public OldMapVec3() {}
	
	public OldMapVec3(float X, float Y, float Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public OldMapVec3(double X, double Y, double Z) {
		this.X = (float)X;
		this.Y = (float)Y;
		this.Z = (float)Z;
	}
	
	public OldMapVec3(String X, String Y, String Z) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
		this.Z = Float.parseFloat(Z);
	}
	
	public OldMapVec3 minus(float X, float Y, float Z) {
		this.X -= X;
		this.Y -= Y;
		this.Z -= Z;
		return this;
	}
	
	public OldMapVec3 add(float X, float Y, float Z) {
		this.X += X;
		this.Y += Y;
		this.Z += Z;
		return this;
	}
	
	public OldMapVec3 times(float X, float Y, float Z) {
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
