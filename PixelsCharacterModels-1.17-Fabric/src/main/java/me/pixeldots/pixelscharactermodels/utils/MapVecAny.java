package me.pixeldots.pixelscharactermodels.utils;

import org.apache.commons.lang3.math.NumberUtils;

public class MapVecAny {

	public float[] nums;
	
	public MapVecAny() {}
	public MapVecAny(float[] nums) {
		this.nums = nums;
	}
	public MapVecAny(String[] nums) {
		float[] n = new float[nums.length];
		for (int i = 0; i < nums.length; i++) {
			if (isNumeric(nums[i])) n[i] = Float.parseFloat(nums[i]);
		}
		this.nums = n;
	}
	
	public MapVecAny Add(float[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i >= nums.length) break;
			nums[i] += values[i];
		}
		return this;
	}
	
	public MapVecAny Minus(float[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i >= nums.length) break;
			nums[i] -= values[i];
		}
		return this;
	}
	
	public boolean isNumeric(String s) {
		return NumberUtils.isCreatable(s);
	}
	
}
