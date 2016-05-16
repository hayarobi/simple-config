package com.github.hayarobi.simple_config.load.yaml;

import java.util.Map;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;

public class MapNodeConfig extends EmptyNodeRawConfig {
	private final Map<String, String> mapValue;
	
	public MapNodeConfig(RootNodeRawConfig realRootNode, String name, Map<String, String> mapValue) {
		super(realRootNode, name);
		this.mapValue = mapValue;
	}
	
	@Override
	public Map<String, String> getPropertyMapValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		return mapValue;
	}
}
