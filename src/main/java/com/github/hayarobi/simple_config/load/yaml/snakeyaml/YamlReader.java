package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.SourceReader;
import com.github.hayarobi.simple_config.load.yaml.NonRootRawConfig;
import com.github.hayarobi.simple_config.load.yaml.RootNodeRawConfig;
import com.github.hayarobi.simple_config.load.yaml.TreeNodeRawConfig;

public class YamlReader implements SourceReader {
	
	@Override
	public RawConfig read(InputStream inputStream) throws IOException {
		Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(), new NonImplicitResolver());
		Map<String, Object> map = (Map<String, Object>)yaml.load(inputStream);
		RootNodeRawConfig rootConfig = new RootNodeRawConfig();
		for (Entry<String, Object> entry : map.entrySet()) {
			TreeNodeRawConfig childConf = createChildConfig(rootConfig, entry.getKey(), entry.getValue());
			if( null != childConf ) {
				rootConfig.addChildConfig(childConf);
			}
		}
		return rootConfig;
	}

	private TreeNodeRawConfig createChildConfig(RootNodeRawConfig rootConfig, String key, Object value) {
		NonRootRawConfig rawConfig = null;
		if( Map.class.isInstance(value) ) {
			Map<String, Object> mapValue = (Map<String, Object>)value;
			rawConfig =  new NonRootRawConfig(rootConfig, key);
			for (Entry<String, Object> entry : mapValue.entrySet() ) {
				putToConf(rawConfig, entry.getKey(), entry.getValue());
			}
			
		}
		return rawConfig;
	}

	private void putToConf(NonRootRawConfig rawConfig, String propName, Object value) {
		if( Map.class.isInstance(value) ) {
			rawConfig.addMapValueProp(propName, (Map<String, Object>) value);
		} else if( Collection.class.isInstance(value) ) {
			rawConfig.addCollectionValueProp(propName, (Collection<Object>)value);
		} else {
			rawConfig.addSingleValueProp(propName, value);
		}
	}

}
