package com.github.hayarobi.simple_config.load.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ExtractHelper;
import com.github.hayarobi.simple_config.load.PropDescription;
import com.github.hayarobi.simple_config.load.PropertyNotFoundException;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.inject.FieldInjector;

public class PojoObjectMapper<T> implements ObjectMapper<T> {

	private static Logger log = LoggerFactory.getLogger(PojoObjectMapper.class);
	
	private final Class<T> objectClass;
	private List<FieldInjector> fieldInjectorList;
	
	public PojoObjectMapper(Class<T> objectClass) {
		super();
		this.objectClass = objectClass;
		this.fieldInjectorList = new ArrayList<FieldInjector>();
	}

	public void addFieldInjector(FieldInjector injector) {
		fieldInjectorList.add(injector);
	}
	
	@Override
	public T mapToObject(RawConfig node) {
		T returnObject = ExtractHelper.createObject(objectClass);
		Map<String, RawConfig> childrenMap = node.getChildrenAsMap();
		for (FieldInjector fieldInjector : fieldInjectorList) {
			PropDescription description = fieldInjector.getPropDescription();
			String propName=description.getName();
			if( ExtractHelper.UNASSIGNED_PLACEHOLDER.equals(propName) ) {
				propName = fieldInjector.getField().getName();
			}
			try {				
				RawConfig childNode = childrenMap.get(propName);
				if( null == childNode ) {
					throw new PropertyNotFoundException(propName);
				}
				fieldInjector.injectValue(returnObject, childNode);
			} catch(PropertyNotFoundException e) {
				if( description.isRequired() ) {
					throw new RuntimeException("The value of required field "+propName+" is missing.");
				}
			}
		}
		return returnObject;
	}
	

	/**
	 * @param configObject
	 * @param field
	 * @param value
	 */
	private void injectValue(Object configObject, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(configObject, value);
			if( log.isTraceEnabled() ) {
				log.trace("Set config value {} to field {}", value, field.getName());
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} finally {
			field.setAccessible(false);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(objectClass.getSimpleName());
		builder.append("Loader(Pojo)");
		return builder.toString();
	}

}
