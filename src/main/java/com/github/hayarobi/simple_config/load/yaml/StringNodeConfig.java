package com.github.hayarobi.simple_config.load.yaml;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;

public class StringNodeConfig extends EmptyNodeRawConfig {
	private String stringValue;
	
	public StringNodeConfig(RootNodeRawConfig realRootNode, String name, String stringValue) {
		super(realRootNode, name);
		this.stringValue = stringValue;
	}

	@Override
	public String getPropertyStringValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		return stringValue;
	}
}
