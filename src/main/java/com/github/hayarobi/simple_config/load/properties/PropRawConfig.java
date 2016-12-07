package com.github.hayarobi.simple_config.load.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.yaml.TreeNodeRawConfig;

public class PropRawConfig extends TreeNodeRawConfig {
	private boolean hasValue = false;
	private String valueString;

	private TreeMap<String, PropRawConfig> children = null;

	public PropRawConfig(String name) {
		super(name);
	}

	public void addChild(String name, PropRawConfig child) {
		if( null == children ) {
			children = new TreeMap<String, PropRawConfig>();
		}
		children.put(name, child);
	}

	public void setValueString(String value) {
		hasValue = true;
		valueString = value;
	}

	@Override
	public String getStringValue() throws InvalidPropertyTypeException {
		if( hasValue ) {
			return valueString;
		} else {
			throw new InvalidPropertyTypeException(TYPE_UNIT);
		}
	}


	@Override
	public List<RawConfig> getChildrenAsList()
			throws InvalidPropertyTypeException {
		if( null != children ) {
			return new ArrayList<RawConfig>(children.values());
		} else {
			// 프로퍼티 기반의 파일에서 empty map이냐 null이냐를 구분하는 방식 
			if( hasValue ) { 
				return new ArrayList<RawConfig>();
			} else {
				throw new InvalidPropertyTypeException(TYPE_LIST);
			}
		}
	}

	@Override
	public Map<String, RawConfig> getChildrenAsMap()
			throws InvalidPropertyTypeException {
		if( null != children ) {
			return new TreeMap<String, RawConfig>(children);
		} else {
			if( hasValue ) { 
				return new TreeMap<String, RawConfig>();
			} else {
				throw new InvalidPropertyTypeException(TYPE_MAP);
			}
		}
	}
	
	public boolean hasChild(String childName) {
		return null != children && children.containsKey(childName);
	}


	public PropRawConfig getChildConfig(String childName) throws IllegalArgumentException {
		if( ! hasChild(childName) ) {
			throw new IllegalArgumentException("No child "+childName);
		}
		return children.get(childName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (hasValue) {
			builder.append("<").append(valueString).append(">");
		}
		if (null != children && !children.isEmpty()) {
			builder.append("{");
			for (Entry<String, PropRawConfig> entry : children.entrySet()) {
				builder.append(entry.getKey()).append(":");
				builder.append(entry.getValue()).append(",");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append("}");
		}
		return builder.toString();
	}

}
