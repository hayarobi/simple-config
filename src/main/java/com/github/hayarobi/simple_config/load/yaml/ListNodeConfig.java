package com.github.hayarobi.simple_config.load.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.RawConfig;

public class ListNodeConfig extends TreeNodeRawConfig {
	private final List<TreeNodeRawConfig> children;

	public ListNodeConfig() {
		super("");
		this.children = new ArrayList<TreeNodeRawConfig>();
	}

	public void addChild(TreeNodeRawConfig child) {
		children.add(child);
	}
	
	@Override
	public String getStringValue() throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_UNIT);
	}

	@Override
	public List<RawConfig> getChildrenAsList()
			throws InvalidPropertyTypeException {
		return new ArrayList<RawConfig>(children);
	}

	@Override
	public Map<String, RawConfig> getChildrenAsMap()
			throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_MAP);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (TreeNodeRawConfig child : children) {
			builder.append(child).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		return builder.toString();
	}
}
