package com.github.hayarobi.simple_config.load.yaml;

import com.github.hayarobi.simple_config.load.DateUtils;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.SourceReader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class YamlReader implements SourceReader {
	
	@Override
	public RawConfContainer read(InputStream inputStream) throws IOException {
		Yaml yaml = new Yaml();
		Map<String, Object> map = yaml.load(inputStream);
		MapNodeConfig rootConfig = new MapNodeConfig();
		for (Entry<String, Object> entry : map.entrySet()) {
			// 설정 객체 특성상 최상위 객체는 Map만이 가능하다. 
			if( !(entry.getValue() instanceof Map)) {
				continue;
			}
			@SuppressWarnings("unchecked") MapNodeConfig childConf =
					createMapNode((Map<String, Object>)entry.getValue());
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

	private StringNodeConfig createDateString(Date value) {
		return new StringNodeConfig(DateUtils.format(value));
	}
	
	private StringNodeConfig createStringNode(Object value) {
		return new StringNodeConfig(value.toString());
	}

	@SuppressWarnings("unchecked")
    private TreeNodeRawConfig createNode(Object value) {
		if(value instanceof Map) {
			return createMapNode((Map<String, Object>)value);
		} else if(value instanceof Collection) {
			return createListNode((Collection<Object>)value);
		} else if(value instanceof Date) {
			return createDateString((Date)value);
		} else {
			return createStringNode(value);
		}
	}

}
