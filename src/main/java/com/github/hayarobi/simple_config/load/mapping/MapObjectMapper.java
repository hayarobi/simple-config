package com.github.hayarobi.simple_config.load.mapping;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ExtractHelper;
import com.github.hayarobi.simple_config.load.RawConfig;


/**
 * 단일 문자열을 바로 객체로 추출하는 클래스. 
 * @author Hayarobi Park
 *
 */
public class MapObjectMapper<K, V> implements ObjectMapper<Map<K, V>> {
	private static Logger log = LoggerFactory.getLogger(MapObjectMapper.class);
	
	private Class<Map<K,V>> mapClass;
	private UnitValueMapper<K> keyParser;
	private ObjectMapper<V> valueLoader;

	public MapObjectMapper(Class<Map<K,V>> mapClass, 
			UnitValueMapper<K> keyLoader, ObjectMapper<V> valueLoader) {
		this.mapClass = mapClass;
		this.keyParser = keyLoader;
		this.valueLoader = valueLoader;
	}

	@Override
	public Map<K, V> mapToObject(RawConfig node) {
		Map<K,V> mapObject = ExtractHelper.createObject(mapClass); 
		if( log.isTraceEnabled() ) {
			log.trace("Mapping raw config to Map Object: from "+node.toString());
		}
		// element type에서 클래스 추출
		Map<String, RawConfig> children = node.getChildrenAsMap();
		for (Entry<String, RawConfig> entry: children.entrySet()) {
			RawConfig child = entry.getValue();
			mapObject.put(keyParser.parse(entry.getKey()), valueLoader.mapToObject(child));
		}
		return mapObject;
	}
}
