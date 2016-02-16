package com.github.hayarobi.simple_config;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	Map<String, String> props = null;
	ValueParserMap parserMap = new ValueParserMap();
	private String elementSeparator;
	
	public ConfigLoader(Map<String, String> properties) {
		this.props = new HashMap<String, String>(properties);
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
			boolean required = false;
			boolean enumCaseSensitive = true;
			ConfIgnore ignoreAnnotation = field.getAnnotation(ConfIgnore.class);
			if( null != ignoreAnnotation ) {
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} is ignored by @ConfIgnored annotation.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			ConfProperty propAnnotation = field.getAnnotation(ConfProperty.class);
			if (propAnnotation == null
					|| UNASSIGNED_PLACEHOLDER.equals(propAnnotation.value())) {
				propName += field.getName();
				elementSeparator = ",";
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} has no @ConfProperty annotation, so default setting is applied.", clazz.getSimpleName(), field.getName());
				}
			} else {
				propName += propAnnotation.value();
				required = propAnnotation.required();
				elementSeparator = propAnnotation.separator();
				enumCaseSensitive = propAnnotation.caseSensitive();
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
					putCollectionValueTo(configObject, enumCaseSensitive, field, value, elementSeparator);
				} else {
					putValueTo(configObject, enumCaseSensitive, field, value);
				}
			} else if( required ) {
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
	private void putValueTo(Object configObject, boolean enumCaseSensitive, Field field, String value) {
		ValueParser<?> parser= null;
		Class<?> fieldType = field.getType();
		
		parser = selectParser(fieldType, enumCaseSensitive);
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
	private ValueParser<?> selectParser(Class<?> fieldType, boolean caseSensitive) {
		ValueParser<?> parser;
		if( fieldType.isPrimitive() ) {
			parser = parserMap.getPrimitive(fieldType);
		} else if( fieldType.isEnum() ) {
			parser = new EnumParser(fieldType, caseSensitive);
		} else {
			parser = parserMap.get(fieldType);					
		}
		return parser;
	}
	
	private void putCollectionValueTo(Object configObject, boolean enumCaseSensitive, Field field, String value, String seperator) {
		Class<?> collectionType = field.getType();
		ParameterizedType genericType = (ParameterizedType)field.getGenericType();
		Class<?> elementType = (Class<?>)genericType.getActualTypeArguments()[0];
		// collection에는 primitive타입이 안 들어간다.
		ValueParser<?> parser= selectParser(elementType, enumCaseSensitive);
		if( null == parser ) {
			throw new IllegalArgumentException("not supported collection element type "+elementType.getName());
		}
		String[] splitted = value.split(seperator);

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

class ValueParserMap {
	private Map<Class<?>, ValueParser<?>> parserMap;
	private Map<Class<?>, ValueParser<?>> primitiveParserMap;

	ValueParserMap() {
		parserMap = new HashMap<Class<?>, ValueParser<?>>();
		parserMap.put(String.class, new StringParser());
		parserMap.put(Short.class, new ShortParser());
		parserMap.put(Integer.class, new IntegerParser());
		parserMap.put(Long.class, new LongParser());
		parserMap.put(Float.class, new FloatParser());
		parserMap.put(Double.class, new DoubleParser());
		parserMap.put(Date.class, new DateParser());
		
		primitiveParserMap = new HashMap<Class<?>, ValueParser<?>>();
		primitiveParserMap.put(Short.TYPE, new ShortParser());
		primitiveParserMap.put(Integer.TYPE, new IntegerParser());
		primitiveParserMap.put(Long.TYPE, new LongParser());
		primitiveParserMap.put(Float.TYPE, new FloatParser());
		primitiveParserMap.put(Double.TYPE, new DoubleParser());
	}
	
	public ValueParser<?> get(Class<?> clazz) {
		return parserMap.get(clazz);
	}
	
	public ValueParser<?> getPrimitive(Class<?> clazz) {
		return primitiveParserMap.get(clazz);
	}

}
interface ValueParser<T> {
	/**
	 * @param str
	 * @return
	 * @throws IllegalArgumentException 해당 타입으로 변환이 불가능한 값이 들어온 경우.
	 */
	public T parse(String str) throws IllegalArgumentException;
}

class EnumParser<T extends Enum<T>> implements ValueParser<T> {
	private Class<T> enumClass;
	private boolean caseSensitive;
	
	public EnumParser(Class<T> enumClass, boolean caseSensitive) {
		super();
		this.enumClass = enumClass;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public T parse(String str) {
		if( caseSensitive ) {
			T enumValue = (T)Enum.valueOf(enumClass, str);
			return enumValue;
		} else {
			for (T enumValue : enumClass.getEnumConstants()  ) {
				if( enumValue.name().compareToIgnoreCase(str) == 0 ) {
					return enumValue;
				}
			}
			throw new IllegalArgumentException("");
		}
	}
	
}

class StringParser implements ValueParser<String> {
	public String parse(String str) {
		return str;
	}
}

class ShortParser implements ValueParser<Short> {
	public Short parse(String str) {
		try {
			return Short.decode(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class IntegerParser implements ValueParser<Integer> {
	public Integer parse(String str) {
		try {
			return Integer.decode(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class LongParser implements ValueParser<Long> {
	public Long parse(String str) {
		try {
			return Long.decode(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class FloatParser implements ValueParser<Float> {
	public Float parse(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class DoubleParser implements ValueParser<Double> {
	public Double parse(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class DateParser implements ValueParser<Date> {
	private static final String UTC_TIMEZONE = "Z";
	public static final String INPUT_DATE_PATTERN = "([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})(\\.[0-9]{3})?([+-][0-9]{2}:[0-9]{2})?";
	private static final Pattern pat = Pattern.compile(INPUT_DATE_PATTERN);
	private static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSSX";
	private static final String FORMAT_TZ = "yyyy-MM-dd HH:mm:ssX";
	private static final String FORMAT_MILLY = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String FORMAT_NOTHING  = "yyyy-MM-dd HH:mm:ss";

	public Date parse(String str) {
		Matcher matcher = pat.matcher(str);
		if (!matcher.matches() ) {
			throw new IllegalArgumentException("Invalid date format :"+str);
		}
		String formatString = null;
		// with milisseconds
        if( matcher.group(2) != null ) {
            if( matcher.group(3) != null ) {
            	formatString = FORMAT_FULL;
            } else {
            	formatString = FORMAT_MILLY;
            }
        } else {
            if( matcher.group(3) != null ) {
            	formatString = FORMAT_TZ;
            } else {
            	formatString = FORMAT_NOTHING;
            }
        }

		try {
			return new SimpleDateFormat(formatString).parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Something is failed:"+str);
		}
	}

}
