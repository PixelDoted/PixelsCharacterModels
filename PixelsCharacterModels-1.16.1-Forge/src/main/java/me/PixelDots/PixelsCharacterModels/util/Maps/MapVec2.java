package me.PixelDots.PixelsCharacterModels.util.Maps;

public class MapVec2 
{

	public float X = 0;
	public float Y = 0;
	
	public MapVec2(float X, float Y) {
		this.X = X;
		this.Y = Y;
	}
	
	public MapVec2(String X, String Y) {
		this.X = Float.parseFloat(X);
		this.Y = Float.parseFloat(Y);
	}

	public void copy(MapVec2 a) {
		X = a.X;
		Y = a.Y;
	}

	public MapVec2 times(float X, float Y) {
		this.X *= X;
		this.Y *= Y;
		return this;
	}

	public MapVec2 add(float X, float Y) {
		this.X += X;
		this.Y += Y;
		return this;
	}
	
	public MapVec2 minus(float X, float Y) {
		this.X -= X;
		this.Y -= Y;
		return this;
	}
	
	public MapVec2 devide(float X, float Y) {
		this.X /= X;
		this.Y /= Y;
		return this;
	}
	
}
