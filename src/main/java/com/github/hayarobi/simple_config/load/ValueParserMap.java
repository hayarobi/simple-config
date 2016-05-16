package com.github.hayarobi.simple_config.load;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			return Short.parseShort(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class IntegerParser implements ValueParser<Integer> {
	public Integer parse(String str) {
		try {
			return Integer.parseInt(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class LongParser implements ValueParser<Long> {
	public Long parse(String str) {
		try {
			return Long.parseLong(str, 10);
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
