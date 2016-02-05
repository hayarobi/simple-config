package com.github.hayarobi.simple_config;

import java.util.Map;

/**
 * configService를 생성하기 위한 빌더 클래스.
 * @author sg13park
 *
 */
public class ConfigServiceBuilder {
	public static ConfigServiceBuilder create() {
		// TODO: 구현하자
		throw new RuntimeException("Not implemented yet.");
	}

	private Map<String, String> propMap;

	private ConfigServiceBuilder() {
	}

	public ConfigServiceBuilder setResource(String resourcePath) {
		// TODO: 구현하자
		throw new RuntimeException("Not implemented yet.");
	}
	public ConfigServiceBuilder setFile(String resource) {
		// TODO: 구현하자
		throw new RuntimeException("Not implemented yet.");
	}
	public ConfigServiceBuilder setPropMap(Map<String, String> propMap) {
		// TODO: 구현하자
		throw new RuntimeException("Not implemented yet.");
	}

	public <T> ConfigServiceBuilder addNewType(Class<T> type, ValueParser<T> valueParser) {
		// TODO: 구현하자
		throw new RuntimeException("Not implemented yet.");
	}
	
	public ConfigService build() {
		// TODO: 구현하자.
		throw new RuntimeException("Not implemented yet.");
	}
}
