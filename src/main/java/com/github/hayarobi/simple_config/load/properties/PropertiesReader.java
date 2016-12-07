/**
 * 
 */
package com.github.hayarobi.simple_config.load.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.SourceReader;

/**
 * @author sg13park
 *
 */
public class PropertiesReader implements SourceReader {
	/* (non-Javadoc)
	 * @see com.github.hayarobi.simple_config.load.SourceReader#read(java.io.InputStream)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public RawConfContainer read(InputStream inputStream) throws IOException {
		Properties props = new Properties();
		props.load(inputStream);
		return createRCContainerFromMap((Map)props);
	}

	public PropertiesRCContainer createRCContainerFromMap(Map<String, String> baseMap) {
		SortedMap<String, String> map = new TreeMap<String, String>(baseMap);
		PropRawConfig root = new PropRawConfig("");
		for (Entry<String, String> ent : map.entrySet() ) {
			String[] pathes = splitPath(ent.getKey());
			getNode(root, pathes).setValueString(ent.getValue());
		}
		return new PropertiesRCContainer(root);		
	}
	private PropRawConfig getNode(PropRawConfig parent, String[] pathes) {
		for (String string : pathes) {
			parent = getOrCreateChild(parent, string);
		}
		return parent;
	}
	
	private PropRawConfig getOrCreateChild(PropRawConfig parent, String childName) {
		if( parent.hasChild(childName) ) {
			return (PropRawConfig)parent.getChildConfig(childName);
		} else {
			PropRawConfig child = new PropRawConfig(childName);
			parent.addChild(childName, child);
			return child;
		}
	}

	private static final String pathPatterString = "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*";
	private static final Pattern pat = Pattern.compile(pathPatterString);
	public static String[] splitPath(String key) {
		Matcher mat = pat.matcher(key);
		if( ! mat.matches() ) {
			throw new IllegalArgumentException("Invalid path string");
		}
		return key.split("\\.");
	}

}
