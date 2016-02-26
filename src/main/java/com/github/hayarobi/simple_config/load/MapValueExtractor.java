package com.github.hayarobi.simple_config.load;

import java.util.Map;
import java.util.Map.Entry;

class MapValueExtractor<K, V> implements PropValueExtractor<Map<K, V>> {
	private Map<K, V> mapObject;
	private ValueParser<K> keyParser;
	private ValueParser<V> elementValueParser;
	
	public MapValueExtractor(Map<K, V> mapObject, ValueParser<K> keyParser, 
			ValueParser<V> valueParser) {
		super();
		this.mapObject = mapObject;
		this.keyParser = keyParser;
		this.elementValueParser = valueParser;
	}

	@Override
	public Map<K, V> extractValue(RawConfig node, String propertyName) {
		Map<String, String> children = node.getPropertyMapValue(propertyName);
		for (Entry<String, String> entry : children.entrySet() ) {
			mapObject.put(keyParser.parse(entry.getKey()),
					elementValueParser.parse(entry.getValue()));
		}
		return mapObject;
	}

}
