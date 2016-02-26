package com.github.hayarobi.simple_config.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;

@Config("list.and.date")
public class MapAndAbstractionConfig {
	@ConfProperty("fruits")
	private List<String> fruitList;

	@ConfProperty("fruits")
	private Map<Long, String> fruitsMap;

	private SortedMap<String, Integer> magicNumbers;

	@ConfProperty("magicNumbers")
	private Set<Integer> abstractSet;

	@ConfProperty("magicNumbers")
	private SortedSet<Integer> sortedSet;

	/**
	 * @return the fruitList
	 */
	public List<String> getFruitList() {
		return fruitList;
	}

	/**
	 * @return the fruitsMap
	 */
	public Map<Long, String> getFruitsMap() {
		return fruitsMap;
	}

	/**
	 * @return the magicNumbers
	 */
	public SortedMap<String, Integer> getMagicNumbers() {
		return magicNumbers;
	}

	/**
	 * @return the abstractSet
	 */
	public Set<Integer> getAbstractSet() {
		return abstractSet;
	}

	/**
	 * @return the sortedSet
	 */
	public SortedSet<Integer> getSortedSet() {
		return sortedSet;
	}

}
