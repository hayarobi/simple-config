package com.github.hayarobi.simple_config.load.yaml;

import java.util.List;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;

public class ListNodeConfig extends EmptyNodeRawConfig {
	private final List<String> listValue;
	
	public ListNodeConfig(RootNodeRawConfig realRootNode, String name, List<String> listValue) {
		super(realRootNode, name);
		this.listValue = listValue;
	}
	
	@Override
	public List<String> getPropertyListValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		return listValue;
	}
}
