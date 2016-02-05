package com.github.hayarobi.simple_config.sample;

import java.math.RoundingMode;
import java.util.ArrayList;

import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;

@Config("enums")
public class EnumTestConfig {
	@ConfProperty(required=true)
	private EnumSample planet;
	private RoundingMode round = RoundingMode.HALF_UP;
	
	private ArrayList<EnumSample> lists;
	
	public ArrayList<EnumSample> getLists() {
		return lists;
	}
	public EnumSample getPlanet() {
		return planet;
	}
	public RoundingMode getRound() {
		return round;
	}
	
	
}
