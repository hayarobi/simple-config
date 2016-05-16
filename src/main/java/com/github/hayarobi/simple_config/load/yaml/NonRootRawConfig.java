package com.github.hayarobi.simple_config.load.yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;
import com.github.hayarobi.simple_config.load.RawConfig;

public class NonRootRawConfig extends TreeNodeRawConfig {
	private final RootNodeRawConfig realRootNode;
	private Map<String, Object> propsMap;
	public NonRootRawConfig(RootNodeRawConfig realRootNode, String name) {
		super(name);
		this.realRootNode = realRootNode;
		this.propsMap = new HashMap<String, Object>();
	}
	
	public void addSingleValueProp(String propName, Object value) {
		propsMap.put(propName, value.toString());
	}
	public void addCollectionValueProp(String propName, Collection<Object> value) {
		List<String> collectionValue = new ArrayList<String>(value.size());
		for (Object obj : value) {
			collectionValue.add(obj.toString());
		}
		propsMap.put(propName, collectionValue);
	}
	public void addMapValueProp(String propName, Map<String, Object> value) {
		Map<String, String> mapValue = new HashMap<String, String>();
		for (Entry<String, Object> entry : value.entrySet() ) {
			mapValue.put(entry.getKey(), entry.getValue().toString());
		}
		propsMap.put(propName, mapValue);
	}

	@Override
	public RawConfig findSubConfig(String propertyName) {
		return realRootNode.findSubConfig(getName()+pathSeparatorChar+propertyName);
	}
	
	@Override
	public String getPropertyStringValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		if( propsMap.containsKey(propertyName) ) {
			return (String)propsMap.get(propertyName);
		} else {
			throw new PropertyNotFoundException(propertyName);
		}
	}

	@Override
	public List<String> getPropertyListValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		if( propsMap.containsKey(propertyName) ) {
			return (List<String>)propsMap.get(propertyName);
			
		} else {
			throw new PropertyNotFoundException(propertyName);
		}
	}

	@Override
	public Map<String, String> getPropertyMapValue(String propertyName)
			throws PropertyNotFoundException, InvalidPropertyTypeException {
		if( propsMap.containsKey(propertyName) ) {
			return (Map<String, String>)propsMap.get(propertyName);
			
		} else {
			throw new PropertyNotFoundException(propertyName);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NonRootRawConfig [name=");
		builder.append(getName());
		builder.append("\n, propsMap=");
		builder.append(propsMap);
		builder.append("]");
		return builder.toString();
	}

	
}
