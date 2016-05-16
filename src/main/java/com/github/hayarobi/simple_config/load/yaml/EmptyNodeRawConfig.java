package com.github.hayarobi.simple_config.load.yaml;

import com.github.hayarobi.simple_config.load.RawConfig;

public class EmptyNodeRawConfig extends TreeNodeRawConfig {
	private final RootNodeRawConfig realRootNode;
	public EmptyNodeRawConfig(RootNodeRawConfig realRootNode, String name) {
		super(name);
		this.realRootNode = realRootNode;
	}

	@Override
	public RawConfig findSubConfig(String propertyName) {
		return realRootNode.findSubConfig(getName()+pathSeparatorChar+propertyName);
	}
}
