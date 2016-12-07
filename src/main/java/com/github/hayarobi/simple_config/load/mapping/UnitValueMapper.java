package com.github.hayarobi.simple_config.load.mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.hayarobi.simple_config.load.RawConfig;

/**
 * 단일 문자열을 파싱해 객체를 로드하는 클래스의 공통 부모.  
 *  
 * @author sg13park
 *
 * @param <T> 이 로더가 생성할 객체의 클래스
 */
public abstract class UnitValueMapper<T> implements ObjectMapper<T> {
	public T mapToObject(RawConfig node) {
		return parse(node.getStringValue());
	}

	public abstract T parse(String str);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("(Unit)");
		return builder.toString();
	}
	
	
}
class StringMapper extends UnitValueMapper<String> {
	public String parse(String str) {
		return str;
	}
}


class BooleanMapper extends UnitValueMapper<Boolean> {
	public Boolean parse(String str) {
		try {
			return Boolean.parseBoolean(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}


class ByteMapper extends UnitValueMapper<Byte> {
	public Byte parse(String str) {
		try {
			return Byte.parseByte(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class ShortMapper extends UnitValueMapper<Short> {
	public Short parse(String str) {
		try {
			return Short.parseShort(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class IntegerMapper extends UnitValueMapper<Integer> {
	public Integer parse(String str) {
		try {
			return Integer.parseInt(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class LongMapper extends UnitValueMapper<Long> {
	public Long parse(String str) {
		try {
			return Long.parseLong(str, 10);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class FloatMapper extends UnitValueMapper<Float> {
	public Float parse(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class DoubleMapper extends UnitValueMapper<Double> {
	public Double parse(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}

class DateMapper extends UnitValueMapper<Date> {
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
