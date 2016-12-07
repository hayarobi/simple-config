package com.github.hayarobi.simple_config.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Ignored;
import com.github.hayarobi.simple_config.annotation.Name;

@Config("list.and.date")
public class ListAndDateConfig {
	@Name("fruits")
	private ArrayList<String> fruitList;
	
	private ArrayList<String> toBeNull;

	private HashSet<Integer> magicNumbers;
	
	private Date fromTime;

	@Ignored
	private List<String> ignoredField;

	@Name("complex")
	private List<OtherConfig> pojoList;
	
	@Name("complex")
	private TreeMap<Integer, OtherConfig> pojoMap;
	
	private List<Fruit> nullList;
	
	private List<NodeInfo> emptyList;
	
	/**
	 * @return the fruitList
	 */
	public List<String> getFruitList() {
		return fruitList;
	}

	/**
	 * @return the magicNumbers
	 */
	public HashSet<Integer> getMagicNumbers() {
		return magicNumbers;
	}

	/**
	 * @return the toBeNull
	 */
	public ArrayList<String> getToBeNull() {
		return toBeNull;
	}

	/**
	 * @return the fromTime
	 */
	public Date getFromTime() {
		return fromTime;
	}

	public List<String> getIgnoredField() {
		return ignoredField;
	}

	/**
	 * @return the pojoList
	 */
	public List<OtherConfig> getPojoList() {
		return pojoList;
	}

	/**
	 * @return the pojoMap
	 */
	public TreeMap<Integer, OtherConfig> getPojoMap() {
		return pojoMap;
	}

	/**
	 * @return the nullList
	 */
	public List<Fruit> getNullList() {
		return nullList;
	}

	/**
	 * @return the emptyList
	 */
	public List<NodeInfo> getEmptyList() {
		return emptyList;
	}
	
	
}
