package com.github.hayarobi.simple_config.load;

public class PropertyNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3067754972077394374L;
	private final String propertyName;
	public PropertyNotFoundException(String propertyName) {
		super("Value of property "+propertyName+" was not found in source.");
		this.propertyName = propertyName;
	}
	
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
}
