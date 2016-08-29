package com.github.hayarobi.simple_config.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.annotation.CaseSensitive;
import com.github.hayarobi.simple_config.annotation.Ignored;
import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Name;
import com.github.hayarobi.simple_config.annotation.Required;

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
	
	public ConfigLoader(RawConfig rootConfig, ValueExtractorManager valueExtractorManager) {
		this.rootConfig = rootConfig;
		this.vem = valueExtractorManager;
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
		PropDescription defaultProperty = new PropDescription(UNASSIGNED_PLACEHOLDER, 
				groupAnnotation.propRequired(), true);
		defaultProperty.required = groupAnnotation.propRequired();

		// 현재는 기본 생성자가 있는 경우만 처리 가능하다.
		T configObject = createConfigObject(clazz, rawConfig, defaultProperty);

		return configObject;

	}

	/**
	 * @param clazz
	 * @param defaultPropDescription TODO
	 * @return
	 */
	private <T, ET> T createConfigObject(Class<T> clazz, RawConfig rawConfig, PropDescription defaultPropDescription) {
		T configObject = createEmptyObject(clazz);

		for (Field field : clazz.getDeclaredFields()) {
			if( null != field.getAnnotation(Ignored.class) ) {
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} is ignored by @ConfIgnored annotation.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			// TODO: 좀 더 범용적인 형태를 반영할 수 있도록 바꾸어보자.
			if( field.getName().startsWith("$") ) {
				continue;
			}
			PropDescription propDescription = defaultPropDescription.getCopy();			
			Name annoName = field.getAnnotation(Name.class);
			if (annoName != null ) {
				propDescription.name = annoName.value();
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has @Name annotation, so use prop name {} instead.", clazz.getSimpleName(), field.getName(), annoName.value());
				}
			}
			Required annoReq = field.getAnnotation(Required.class);
			if (annoReq != null ) {
				propDescription.required = annoReq.value();
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has @Required annotation {} .", clazz.getSimpleName(), field.getName(), annoReq.value());
				}
			}
			CaseSensitive annoCS = field.getAnnotation(CaseSensitive.class);
			if (annoCS != null ) {
				propDescription.caseSensitive = annoCS.value();
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has @CaseSensitive annotation {} .", clazz.getSimpleName(), field.getName(), annoCS.value());
				}
			}
			injectValue(rawConfig, configObject, propDescription, field);
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
	private <VT> void injectValue(RawConfig rawConfig, Object configObject, PropDescription propDescription, Field field) {
		String propName = selectPropertyName(field, propDescription);
		try {
			PropValueExtractor<VT> valueExtractor = vem.getExtractor(field, propDescription);
			VT value = valueExtractor.extractValue(rawConfig, propName);
			field.setAccessible(true);
			field.set(configObject, value);
			if( log.isTraceEnabled() ) {
				log.trace("Set config value {} to field {}", value, field.getName());
			}
		} catch(PropertyNotFoundException e) {
			if( propDescription.required ) {
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
	protected String selectPropertyName(Field field, PropDescription propAnnotation) {
		String propName;
		if( UNASSIGNED_PLACEHOLDER.equals(propAnnotation.name)) {
			propName = field.getName();
		} else {
			propName = propAnnotation.name;
		}
		if( log.isTraceEnabled() ) {
			log.trace("finding config value of field {} by property name {}.", field.getName(), propName);
		}
		return propName;
	}

	static class PropDescription implements Cloneable {
		String name;
		boolean required;
		boolean caseSensitive;

		public PropDescription(String name, boolean required,
				boolean caseSensitive) {
			super();
			this.name = name;
			this.required = required;
			this.caseSensitive = caseSensitive;
		}
		
		public PropDescription getCopy() {
			try {
				return (PropDescription)this.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException("Something goes wrong.");
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("PropDescription [name=");
			builder.append(name);
			builder.append(", required=");
			builder.append(required);
			builder.append(", caseSensitive=");
			builder.append(caseSensitive);
			builder.append("]");
			return builder.toString();
		}
		
	}

}
