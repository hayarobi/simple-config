package com.github.hayarobi.simple_config.load.mapping;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.RawConfig;

/**
 * 단일 문자열을 바로 객체로 추출하는 클래스.
 * 
 * @author Hayarobi Park
 *
 * @param <T>
 */
public class EnumObjectMapper<T extends Enum<T>> extends UnitValueMapper<T> implements ObjectMapper<T> {
	private Logger log = LoggerFactory.getLogger(EnumObjectMapper.class);
	private EnumParser<T> parser;

	public EnumObjectMapper(Class<T> enumClass, boolean caseSensitive) {
		if (caseSensitive) {
			parser = new CaseSensitiveParser(enumClass);
		} else {
			parser = new CaseInsensitiveParser(enumClass);
		}
	}

	@Override
	public T mapToObject(RawConfig node) {
		return parser.parse(node.getStringValue());
	}

	@Override
	public T parse(String str) {
		return parser.parse(str);
	}

	interface EnumParser<T extends Enum<T>> {
		T parse(String str);
	}

	class CaseSensitiveParser<E extends Enum<E>> implements EnumParser<E> {
		private Class<E> enumClass;

		public CaseSensitiveParser(Class<E> enumClass) {
			super();
			this.enumClass = enumClass;
		}

		@Override
		public E parse(String str) {
			return (E) Enum.valueOf(enumClass, str);
		}
	}

	static class CaseInsensitiveParser<E extends Enum<E>> implements
			EnumParser<E> {
		private Class<E> enumClass;
		private Map<String, E> lowercaseMap;

		public CaseInsensitiveParser(Class<E> enumClass) {
			super();
			this.enumClass = enumClass;
			lowercaseMap = new HashMap<String, E>();
			for (E enumValue : enumClass.getEnumConstants()) {
				lowercaseMap.put(enumValue.name().toLowerCase(), enumValue);
			}
		}

		@Override
		public E parse(String str) {
			E eval = lowercaseMap.get(str.toLowerCase());
			if (null != eval) {
				return eval;
			} else {
				throw new IllegalArgumentException("No enum literal " + str
						+ " was found in " + enumClass.getSimpleName()
						+ "(case insensitive).");
			}
		}
	}
}
