package com.github.hayarobi.simple_config.load;



public class ExtractHelper {
	public static final String UNASSIGNED_PLACEHOLDER = "[unassigned]";

	public static <T> T createObject(Class<T> objectClass) {
		T configObject = null;
		try {
			configObject = objectClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Failed to create configObject for "
					+ objectClass.getName()
					+ ": there is no default public constructor.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to create configObject for "
					+ objectClass.getName() + ": Constructor is not public", e);
		} catch (SecurityException e) {
			throw new RuntimeException("Failed to create configObject for "
					+ objectClass.getName() + ": security exception.", e);
		}
		return configObject;
	}

}
