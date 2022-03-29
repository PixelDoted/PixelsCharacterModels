package me.PixelDots.PixelsCharacterModels.util;

import me.PixelDots.PixelsCharacterModels.Model.GlobalModelData;
import me.PixelDots.PixelsCharacterModels.client.Animations.Animation;

public class AnimUTILS 
{
	
	//Rotation
	public float animLerpX(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AnglePoses.get(id).X;
		float angleV = 0;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AnglePoses.get(id).X;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	
	public float animLerpY(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AnglePoses.get(id).Y;
		float angleV = 0;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AnglePoses.get(id).Y;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	
	public float animLerpZ(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AnglePoses.get(id).Z;
		float angleV = 0;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AnglePoses.get(id).Z;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	//Rotation
	//RotationPoint
	public float animRotLerpX(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AngleRotPoint.get(id).X;
		float angleV = data.OldFrameStats.DRP.get(id).X;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AngleRotPoint.get(id).X;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	
	public float animRotLerpY(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AngleRotPoint.get(id).Y;
		float angleV = data.OldFrameStats.DRP.get(id).Y;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AngleRotPoint.get(id).Y;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	
	public float animRotLerpZ(GlobalModelData data, float V, String id) {
		Animation anim = data.PlayingAnim;
		Lerp lerp = new Lerp();
		//return anim.AngleRotPoint.get(id).Z;
		float angleV = data.OldFrameStats.DRP.get(id).Z;
		if (anim.AnglePoses.containsKey(id)) angleV = anim.AngleRotPoint.get(id).Z;
		return (data.Frame == null ? angleV 
				: (data.Frame.Linear == true ?
						lerp.Linear(V, angleV, data.Frame.time) :
							lerp.Curve(V, angleV, data.Frame.time)));
	}
	//RotationPoint
	
}
