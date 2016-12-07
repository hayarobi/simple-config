package com.github.hayarobi.simple_config.sample;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import com.github.hayarobi.simple_config.annotation.Config;

@Config
public class TestClass {
	private int primitive1;
	private String string1;
	private List<EnumSample> enumList;
	private SortedSet<EnumSample> enumSet;
	private Map<EnumSample, String> enumMap;
	private SortedMap<Integer, EnumSample> sortedMap;
	
	/**
	 * @return the primitive1
	 */
	public int getPrimitive1() {
		return primitive1;
	}
	/**
	 * @return the string1
	 */
	public String getString1() {
		return string1;
	}
	/**
	 * @return the enumList
	 */
	public List<EnumSample> getEnumList() {
		return enumList;
	}
	/**
	 * @return the enumSet
	 */
	public SortedSet<EnumSample> getEnumSet() {
		return enumSet;
	}
	/**
	 * @return the enumMap
	 */
	public Map<EnumSample, String> getEnumMap() {
		return enumMap;
	}
	/**
	 * @return the sortedMap
	 */
	public SortedMap<Integer, EnumSample> getSortedMap() {
		return sortedMap;
	}
}