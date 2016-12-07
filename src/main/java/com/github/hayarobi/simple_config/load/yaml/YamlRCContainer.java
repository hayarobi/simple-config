package com.github.hayarobi.simple_config.load.yaml;

import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;

public class YamlRCContainer implements RawConfContainer {
	private MapNodeConfig rootConf;

	YamlRCContainer(MapNodeConfig rootConf) {
		this.rootConf = rootConf;
	}

	@Override
	public RawConfig findConfig(String confName) {
		TreeNodeRawConfig child = rootConf.findChild(confName);
		if( null == child ) {
			return null;
		} else {
			return child;
		}
	}

	@Override
	public RawConfig createEmptyConfig() {
		return new MapNodeConfig();
	}
}
