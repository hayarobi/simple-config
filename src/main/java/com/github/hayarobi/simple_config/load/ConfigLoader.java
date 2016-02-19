package com.github.hayarobi.simple_config.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	private static final char PROP_SEPARATOR = '.';

	private Logger log = LoggerFactory.getLogger(ConfigLoader.class);
	
	private Map<String, String> props = null;
	private ValueParserMap parserMap = new ValueParserMap();
	private final String elementSeparator = ",";
	private final ConfProperty defaultProperty;
	
	public ConfigLoader(Map<String, String> properties) {
		this.props = new HashMap<String, String>(properties);
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
		// 현재는 기본 생성자가 있는 경우만 처리 가능하다.
		T configObject = createConfigObject(clazz);

		// 1. prefix를 찾는다
		String prefix = null;
		if (UNASSIGNED_PLACEHOLDER.equals(groupAnnotation.value())) {
			prefix = clazz.getCanonicalName();
		} else {
			prefix = groupAnnotation.value();
		}

		for (Field field : clazz.getDeclaredFields()) {
			String propName = prefix + PROP_SEPARATOR;
			if( null != field.getAnnotation(ConfIgnore.class) ) {
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} is ignored by @ConfIgnored annotation.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			ConfProperty propAnnotation = field.getAnnotation(ConfProperty.class);
			if (propAnnotation == null ) {
				propAnnotation = this.defaultProperty;
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has no @ConfProperty annotation, so default setting is applied.", clazz.getSimpleName(), field.getName());
				}
			}
			if( UNASSIGNED_PLACEHOLDER.equals(propAnnotation.value())) {
				propName += field.getName();
			} else {
				propName += propAnnotation.value();
			}
			if( log.isTraceEnabled() ) {
				log.trace("finding config value of field {}#{} by property name {}.", clazz.getSimpleName(), field.getName()
						, propName);
			}

			// abstract클래스나 인터페이스는 아직 허용하지 않음
			Class<?> fieldType = field.getType();
			if(   !fieldType.isPrimitive() &&
					( Modifier.isInterface(fieldType.getModifiers()) || Modifier.isAbstract(fieldType.getModifiers()) ) 
			) {
				throw new RuntimeException("field "+field.getName()+" is abstract class or interface, and is not supported yet.");
			}
			
			String value = props.get(propName);
			if (null != value) {
				if( log.isTraceEnabled() ) {
					log.trace("Found config value of field {}#{}: {}.", clazz.getSimpleName(), field.getName(), value);
				}
				if( Collection.class.isAssignableFrom(fieldType) ) {
					putCollectionValueTo(configObject, propAnnotation, field, value);
				} else {
					putValueTo(configObject, propAnnotation, field, value);
				}
			} else if( propAnnotation.required() ) {
				throw new RuntimeException("The value of required field "+field.getName()+" is missing.");
			}
		}

		return configObject;

	}

	/**
	 * @param clazz
	 * @return
	 */
	protected <T> T createConfigObject(Class<T> clazz) {
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
	private void putValueTo(Object configObject, ConfProperty propAnnotation, Field field, String value) {
		ValueParser<?> parser= null;
		Class<?> fieldType = field.getType();
		
		parser = selectParser(fieldType, propAnnotation);
		if( null == parser ) {
			throw new IllegalArgumentException("not supported field type "+fieldType.getName());
		}
		try {
			field.setAccessible(true);
			field.set(configObject, parser.parse(value));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} finally {
			field.setAccessible(false);
		}
	}


	/**
	 * 
	 * @param fieldType
	 * @param caseSensitive FIXME: 이 파라메터가 존재하는 방식이 안 이쁘다.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ValueParser<?> selectParser(Class<?> fieldType, ConfProperty propAnnotation) {
		ValueParser<?> parser;
		if( fieldType.isPrimitive() ) {
			parser = parserMap.getPrimitive(fieldType);
		} else if( fieldType.isEnum() ) {
			parser = new EnumParser(fieldType, propAnnotation.caseSensitive());
		} else {
			parser = parserMap.get(fieldType);					
		}
		return parser;
	}
	
	private void putCollectionValueTo(Object configObject, ConfProperty propAnnotation, Field field, String value) {
		Class<?> collectionType = field.getType();
		ParameterizedType genericType = (ParameterizedType)field.getGenericType();
		Class<?> elementType = (Class<?>)genericType.getActualTypeArguments()[0];
		// collection에는 primitive타입이 안 들어간다.
		ValueParser<?> parser= selectParser(elementType, propAnnotation);
		if( null == parser ) {
			throw new IllegalArgumentException("not supported collection element type "+elementType.getName());
		}
		String[] splitted = value.split(this.elementSeparator);

		try {
			Collection<?> collectionObject = createAndSetupCollection(collectionType, elementType, parser, splitted);
			field.setAccessible(true);
			field.set(configObject, collectionObject);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Failed to set config value to "+field.getName(),e);
		} finally {
			field.setAccessible(false);
		}

	}

	@SuppressWarnings("unchecked")
	private <T> Collection<T> createAndSetupCollection(Class<?> collectionType,
			Class<T> elementType, ValueParser<?> parser, String[] splitted) throws InstantiationException, IllegalAccessException {
		Collection<T> collectionObject = (Collection<T>)collectionType.newInstance();
		for (String string : splitted) {
			collectionObject.add((T)parser.parse(string.trim()));
		}
		return collectionObject;
	}

}

