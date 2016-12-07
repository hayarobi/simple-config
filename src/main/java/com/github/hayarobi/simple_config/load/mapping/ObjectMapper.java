package com.github.hayarobi.simple_config.load.mapping;

import com.github.hayarobi.simple_config.load.RawConfig;

public interface ObjectMapper<T> {
	public T mapToObject(RawConfig node);
}
