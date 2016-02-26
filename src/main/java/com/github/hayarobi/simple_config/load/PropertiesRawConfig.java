package com.github.hayarobi.simple_config.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 * not thread-safe
 * @author sg13park
 *
 */
public class PropertiesRawConfig implements RawConfig {
	private static final char pathSeparatorChar = '.'; 
	
	private final String name;
	private Map<String, String> childProperties;
	public PropertiesRawConfig(String name, Map<String, String> originalProperty) {
		super();
		this.name = name;
		this.childProperties = originalProperty;
	}

	@Override
	public String getName() {
		return name;
	}

	public RawConfig findSubConfig(String propertyName) {
		String childPrefix = propertyName + pathSeparatorChar;
		int prefixLength = childPrefix.length();
		HashMap<String, String> propsMap = new HashMap<String, String>();
		for (Entry<String, String> entry : childProperties.entrySet()) {
			String key = entry.getKey();
			if( key.startsWith(childPrefix) && key.length() > prefixLength ) {
				propsMap.put(key.substring(prefixLength), entry.getValue());
			}
		}
		
		return new PropertiesRawConfig(propertyName, propsMap);
	}

	@Override
	public String getPropertyStringValue(String propertyName)
			throws PropertyNotFoundException {
		if( childProperties.containsKey(propertyName) ) {
			return childProperties.get(propertyName);
		} else {
			for (String key: childProperties.keySet()) {
				if( key.startsWith(propertyName) ) {
					throw new InvalidPropertyTypeException(propertyName);					
				}
			}
			throw new PropertyNotFoundException(propertyName);
		}
	}

	@Override
	public List<String> getPropertyListValue(String propertyName)
			throws PropertyNotFoundException {
		TreeSet<String> keySet = new TreeSet<String>();
		for (String key : childProperties.keySet()) {
			if( isDirectChild(propertyName, key) ) {
				keySet.add(key);
			}
		}
		if( keySet.size() == 0 ) {
			if( childProperties.containsKey(propertyName) ) {
				throw new InvalidPropertyTypeException(propertyName);									
			} else {
				throw new PropertyNotFoundException(propertyName);
			}
		}
		
		List<String> valueList = new ArrayList<String>(keySet.size());
		for (String key: keySet) {
			valueList.add(childProperties.get(key));
		}
		return valueList;
	}

	/**
	 * @param propertyName
	 * @param childPrefix
	 * @param prefixLength
	 * @param key
	 * @return
	 */
	private boolean isDirectChild(String propertyName, String key) {
		String childPrefix = propertyName + pathSeparatorChar;
		int propertyNamelength = propertyName.length();
		return key.startsWith(childPrefix) && key.length() > propertyNamelength+1 
				&& key.lastIndexOf(pathSeparatorChar) == propertyNamelength;
	}

	@Override
	public Map<String, String> getPropertyMapValue(String propertyName)
			throws PropertyNotFoundException {
		Map<String, String> valueMap = new HashMap<String, String>();
		for (Entry<String, String> entry : childProperties.entrySet()) {
			String key = entry.getKey();
			if( isDirectChild(propertyName, key) ) {
				valueMap.put(key.substring(propertyName.length()+1), entry.getValue());
			}
		}
		
		if( valueMap.size() == 0 ) {
			if( childProperties.containsKey(propertyName) ) {
				throw new InvalidPropertyTypeException(propertyName);									
			} else {
				throw new PropertyNotFoundException(propertyName);
			}
		}
		return valueMap;
	}

}
