package com.github.hayarobi.simple_config.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.github.hayarobi.simple_config.annotation.ConfIgnore;
import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;

@Config("list.and.date")
public class ListAndDateConfig {
	@ConfProperty("fruits")
	private ArrayList<String> fruitList;
	
	private ArrayList<String> toBeNull;

	private HashSet<Integer> magicNumbers;
	
	private Date fromTime;

	@ConfIgnore
	private List<String> ignoredField;

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
}
