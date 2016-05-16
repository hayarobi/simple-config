package com.github.hayarobi.simple_config.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.annotation.ConfIgnore;
import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;

/**
 * 
 * @author sg13park
 *
 */
public class ConfigLoader {
	private static final String UNASSIGNED_PLACEHOLDER = "[unassigned]";

	private Logger log = LoggerFactory.getLogger(ConfigLoader.class);
	
	private RawConfig rootConfig = null;
	private ValueExtractorManager vem = null;
	private final String elementSeparator = ",";
	private final ConfProperty defaultProperty;
	
	public ConfigLoader(RawConfig rootConfig, ValueExtractorManager valueExtractorManager) {
		this.rootConfig = rootConfig;
		this.vem = valueExtractorManager;
		this.defaultProperty = getDefaultConfPropertyAnnotation();
	}

	private ConfProperty getDefaultConfPropertyAnnotation() {
		// NOTE: annotation 선언과 동일한 기본값을 반환해야한다.
		ConfProperty prop = new ConfProperty() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return ConfProperty.class;
			}
			
			@Override
			public String value() {
				return "[unassigned]";
			}
			
			@Override
			public boolean required() {
				return false;
			}
			
			@Override
			public boolean caseSensitive() {
				return true;
			}
		};
		return prop;
	}
	/**
	 * map의 값을 바탕으로 설정 객체를 생성해서 반환한다. 
	 * @param clazz
	 * @return
	 */
	public <T> T loadConfig(Class<T> clazz) {
		Config groupAnnotation = clazz.getAnnotation(Config.class);
		if (null == groupAnnotation) {
			throw new IllegalArgumentException("The class " + clazz.getName()
					+ " is not config class.");
		}
		// 1. rawConfig를 찾는다. 
		String prefix = null;
		if (UNASSIGNED_PLACEHOLDER.equals(groupAnnotation.value())) {
			prefix = clazz.getCanonicalName();
		} else {
			prefix = groupAnnotation.value();
		}
		RawConfig rawConfig = rootConfig.findSubConfig(prefix);

		// 현재는 기본 생성자가 있는 경우만 처리 가능하다.
		T configObject = createConfigObject(clazz, rawConfig);

		return configObject;

	}

	/**
	 * @param clazz
	 * @return
	 */
	private <T, ET> T createConfigObject(Class<T> clazz, RawConfig rawConfig) {
		T configObject = createEmptyObject(clazz);

		for (Field field : clazz.getDeclaredFields()) {
			if( null != field.getAnnotation(ConfIgnore.class) ) {
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} is ignored by @ConfIgnored annotation.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			// TODO: 좀 더 범용적인 형태를 반영할 수 있도록 바꾸어보자.
			if( field.getName().startsWith("$") ) {
				continue;
			}
			ConfProperty propAnnotation = field.getAnnotation(ConfProperty.class);
			if (propAnnotation == null ) {
				propAnnotation = this.defaultProperty;
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has no @ConfProperty annotation, so default setting is applied.", clazz.getSimpleName(), field.getName());
				}
			}
			injectValue(rawConfig, configObject, propAnnotation, field);
		}

		return configObject;
	}
	
	private <T> T createEmptyObject(Class<T> clazz) {
		T configObject = null;
		try {
			configObject = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Failed to create configObject for "
					+ clazz.getName()
					+ ": there is no default public constructor.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to create configObject for "
					+ clazz.getName()
					+ ": there is no default public constructor.", e);
		}
		return configObject;
	}

	/**
	 * @param configObject
	 * @param field
	 * @param value
	 */
	private <VT> void injectValue(RawConfig rawConfig, Object configObject, ConfProperty propAnnotation, Field field) {
		String propName = selectPropertyName(field, propAnnotation);
		try {
			PropValueExtractor<VT> valueExtractor = vem.getExtractor(field, propAnnotation);
			VT value = valueExtractor.extractValue(rawConfig, propName);
			field.setAccessible(true);
			field.set(configObject, value);
			if( log.isTraceEnabled() ) {
				log.trace("Set config value {} to field {}", value, field.getName());
			}
		} catch(PropertyNotFoundException e) {
			if( propAnnotation.required() ) {
				throw new RuntimeException("The value of required field "+field.getName()+" is missing.");
			} else {
				// leave this field, to remain default value.
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} finally {
			field.setAccessible(false);
		}
	}

	/**
	 * @param field
	 * @param propAnnotation
	 * @return
	 */
	protected String selectPropertyName(Field field, ConfProperty propAnnotation) {
		String propName;
		if( UNASSIGNED_PLACEHOLDER.equals(propAnnotation.value())) {
			propName = field.getName();
		} else {
			propName = propAnnotation.value();
		}
		if( log.isTraceEnabled() ) {
			log.trace("finding config value of field {} by property name {}.", field.getName(), propName);
		}
		return propName;
	}
}

