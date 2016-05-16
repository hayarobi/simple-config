package com.github.hayarobi.simple_config.load.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;
import com.github.hayarobi.simple_config.load.RawConfig;

/**
 * not thread-safe
 * @author sg13park
 *
 */
public abstract class TreeNodeRawConfig implements RawConfig {
	protected static final char pathSeparatorChar = '.';
	
	private final String name;
	
	
	public TreeNodeRawConfig(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}


	@Override
	public String getPropertyStringValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		throw new PropertyNotFoundException(propertyName);
	}

	@Override
	public List<String> getPropertyListValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		throw new PropertyNotFoundException(propertyName);
	}

	@Override
	public Map<String, String> getPropertyMapValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		throw new PropertyNotFoundException(propertyName);
	}
}
