package com.github.hayarobi.simple_config.load.yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.RawConfig;

public class MapNodeConfig extends TreeNodeRawConfig implements RawConfig {
	private final Map<String, TreeNodeRawConfig> childrenMap;

	public MapNodeConfig() {
		super("");
		this.childrenMap = new HashMap<String, TreeNodeRawConfig>();
	}

	public TreeNodeRawConfig findChild(String confName) {
		return childrenMap.get(confName);
	}

	public void addChild(String childName, TreeNodeRawConfig child) {
		this.childrenMap.put(childName, child);
	}

	@Override
	public String getStringValue() throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_UNIT);
	}

	@Override
	public List<RawConfig> getChildrenAsList()
			throws InvalidPropertyTypeException {
		throw new InvalidPropertyTypeException(TYPE_LIST);
	}

	@Override
	public Map<String, RawConfig> getChildrenAsMap()
			throws InvalidPropertyTypeException {
		return new HashMap<String, RawConfig>(childrenMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Entry<String, TreeNodeRawConfig> child : childrenMap.entrySet()) {
			builder.append(child.getKey()).append(":");
			builder.append(child.getValue()).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		return builder.toString();
	}

}
