package com.github.hayarobi.simple_config.load.inject;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.PropDescription;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.mapping.ObjectMapper;

public class FieldInjector<T> {
	private static Logger log = LoggerFactory.getLogger(FieldInjector.class);
	
	protected final PropDescription propDescription;
	protected final Field toInjectField;

	protected ObjectMapper<T> mapper;
	
	public FieldInjector(PropDescription propDescription, Field injectField
			, ObjectMapper<T> objectLoader) {
		super();
		this.propDescription = propDescription;
		this.toInjectField = injectField;
		this.mapper = objectLoader;
	}

	public PropDescription getPropDescription() {
		return propDescription;
	}

	public Field getField() {
		return toInjectField;
	}

	public void injectValue(Object to, RawConfig from) {
		try {
			Object value = createValueObject(from);		
			toInjectField.setAccessible(true);
			toInjectField.set(to, value);
			if( log.isTraceEnabled() ) {
				log.trace("Set config value {} to field {}", value, toInjectField.getName());
			}
		} catch (IllegalArgumentException e) {
			// TODO: 좀 더 구체적인 예외 클래스를 만들던지 메세지를 명확하게 하던지 하자.
			throw new RuntimeException("Failed to set config value to "+toInjectField.getName(),e);
		} catch (IllegalAccessException e) {
			// TODO: 좀 더 구체적인 예외 클래스를 만들던지 메세지를 명확하게 하던지 하자.
			throw new RuntimeException("Failed to set config value to "+toInjectField.getName(),e);
		} finally {
			toInjectField.setAccessible(false);
		}
	}

	protected Object createValueObject(RawConfig from) {
		return mapper.mapToObject(from);
	}
}
