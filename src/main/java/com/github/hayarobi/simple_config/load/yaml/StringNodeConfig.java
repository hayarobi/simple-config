package com.github.hayarobi.simple_config.load.yaml;

import java.util.List;
import java.util.Map;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.RawConfig;

public class StringNodeConfig extends TreeNodeRawConfig {
	private String stringValue;
	
	public StringNodeConfig(String stringValue) {
		super("");
		this.stringValue = stringValue;
	}

	@Override
	public String getStringValue() throws InvalidPropertyTypeException {
		return stringValue;
	}

	@Override
	public List<RawConfig> getChildrenAsList()
			throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_LIST);
	}

	@Override
	public Map<String, RawConfig> getChildrenAsMap()
			throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_MAP);
	}
	
	@Override
	public String toString() {
		return "<"+stringValue+">";
	}
}
