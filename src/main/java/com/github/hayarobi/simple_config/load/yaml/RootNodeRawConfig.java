package com.github.hayarobi.simple_config.load.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.hayarobi.simple_config.load.RawConfig;

public class RootNodeRawConfig extends TreeNodeRawConfig {
	private Map<String, TreeNodeRawConfig> childConfigs;

	public RootNodeRawConfig() {
		super("");
		childConfigs = new LinkedHashMap<String, TreeNodeRawConfig>();
	}

	public TreeNodeRawConfig addChildConfig(TreeNodeRawConfig config) {
		childConfigs.put(config.getName(), config);
		return this;
	}

	@Override
	public RawConfig findSubConfig(String propertyName) {
		if( childConfigs.containsKey(propertyName) ) {
			return childConfigs.get(propertyName);
		} else {
			return new EmptyNodeRawConfig(this, propertyName);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RootNodeRawConfig [childConfigs=");
		builder.append(childConfigs);
		builder.append("]");
		return builder.toString();
	}
}
