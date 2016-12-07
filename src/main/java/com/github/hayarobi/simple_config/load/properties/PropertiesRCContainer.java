package com.github.hayarobi.simple_config.load.properties;

import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.yaml.MapNodeConfig;

public class PropertiesRCContainer implements RawConfContainer {
	private PropRawConfig rootConf;

	PropertiesRCContainer(PropRawConfig rootConf) {
		this.rootConf = rootConf;
	}

	@Override
	public RawConfig findConfig(String confName) {
		String[] pathArr = PropertiesReader.splitPath(confName);
		PropRawConfig parent = rootConf;
		try {
			for (String string : pathArr) {
				parent = parent.getChildConfig(string);
			}
			return parent;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public RawConfig createEmptyConfig() {
		return new MapNodeConfig();
	}
}
