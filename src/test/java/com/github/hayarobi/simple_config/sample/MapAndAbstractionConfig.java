package com.github.hayarobi.simple_config.sample;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Name;

@Config("list.and.date")
public class MapAndAbstractionConfig {
	@Name("fruits")
	private List<String> fruitList;

	@Name("fruits")
	private Map<Long, String> fruitsMap;

	private SortedMap<String, Integer> magicNumbers;

	@Name("magicNumbers")
	private Set<Integer> abstractSet;

	@Name("magicNumbers")
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
