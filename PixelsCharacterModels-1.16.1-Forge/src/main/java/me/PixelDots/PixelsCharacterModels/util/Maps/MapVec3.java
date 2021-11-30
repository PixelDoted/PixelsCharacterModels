package me.PixelDots.PixelsCharacterModels.util.Maps;

import net.minecraft.client.renderer.Vector3f;

public class MapVec3 
{
	
	public float X = 0;
	public float Y = 0;
	public float Z = 0;
	
	public MapVec3(float X, float Y, float Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public MapVec3(String X, String Y, String Z) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
		this.Z = Float.parseFloat(Z);
	}

	public void copy(MapVec3 a) {
		X = a.X;
		Y = a.Y;
		Z = a.Z;
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

	public Vector3f toVec() {
		return new Vector3f(X,Y,Z);
	}
	
}
