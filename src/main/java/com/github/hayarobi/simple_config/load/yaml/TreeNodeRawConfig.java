package com.github.hayarobi.simple_config.load.yaml;

import com.github.hayarobi.simple_config.load.RawConfig;

/**
 * 나중에 날려버리는 게 좋을 것 같은 클래스다.
 * not thread-safe
 * @author sg13park
 *
 */
public abstract class TreeNodeRawConfig implements RawConfig {	
	public TreeNodeRawConfig(String name) {
		super();
	}
}
