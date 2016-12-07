package com.github.hayarobi.simple_config.load.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.SourceReader;
import com.github.hayarobi.simple_config.load.yaml.snakeyaml.NonImplicitResolver;

public class YamlReader implements SourceReader {
	
	@Override
	public RawConfContainer read(InputStream inputStream) throws IOException {
		Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(), new NonImplicitResolver());
		Map<String, Object> map = (Map<String, Object>)yaml.load(inputStream);
		MapNodeConfig rootConfig = new MapNodeConfig();
		for (Entry<String, Object> entry : map.entrySet()) {
			// 설정 객체 특성상 최상위 객체는 Map만이 가능하다. 
			if( !Map.class.isInstance(entry.getValue()) ) {
				continue;
			}
			MapNodeConfig childConf = createMapNode((Map)entry.getValue());
			rootConfig.addChild(entry.getKey(), childConf);
		}
		return new YamlRCContainer(rootConfig);
	}

	private MapNodeConfig createMapNode(Map<String, Object> value) {
		MapNodeConfig conf = new MapNodeConfig();
		for (Entry<String, Object> entry : value.entrySet()) {
			conf.addChild(entry.getKey(), createNode(entry.getValue()));
		}
		return conf;
	}
	
	private ListNodeConfig createListNode(Collection<Object> value) {
		ListNodeConfig conf = new ListNodeConfig();
		for (Object element: value) {
			conf.addChild(createNode(element));
		}
		return conf;
	}
	
	private StringNodeConfig createStringNode(Object value) {
		return new StringNodeConfig(value.toString());
	}

	private TreeNodeRawConfig createNode(Object value) {
		if( Map.class.isInstance(value) ) {
			return createMapNode((Map)value);
		} else if( Collection.class.isInstance(value) ) {
			return createListNode((Collection)value);
		} else {
			return createStringNode(value);
		}
	}

}
