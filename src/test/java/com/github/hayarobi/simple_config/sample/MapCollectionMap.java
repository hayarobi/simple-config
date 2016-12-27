package com.github.hayarobi.simple_config.sample;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Name;

@Config("mcm")
public class MapCollectionMap {
	@Name("triple")
	private SortedMap<String, List<Map<Double,String>>> cmap;

	/**
	 * @return the cmap
	 */
	public SortedMap<String, List<Map<Double, String>>> getCmap() {
		return cmap;
	}
	
	
	
}
