package com.github.hayarobi.simple_config.load;


public class EnumValueExtractor<T extends Enum<T>> implements PropValueExtractor<T> {
	private Class<T> enumClass;
	private boolean caseSensitive= true;

	public EnumValueExtractor(Class<T> enumClass, boolean caseSensitive) {
		super();
		this.enumClass = enumClass;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public T extractValue(RawConfig node, String propertyName) {
		String str = node.getPropertyStringValue(propertyName);
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
