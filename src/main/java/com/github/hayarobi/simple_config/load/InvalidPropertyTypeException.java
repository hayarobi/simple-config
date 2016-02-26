package com.github.hayarobi.simple_config.load;

/**
 * collection(혹은 map)을 단일값으로 읽어오려고 할 때. 혹은 그 반대 상황.
 * @author sg13park
 *
 */
public class InvalidPropertyTypeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3067754972077394374L;
	private final String propertyName;
	public InvalidPropertyTypeException(String propertyName) {
		super("Type of property "+propertyName+" is wrong.");
		this.propertyName = propertyName;
	}
	
	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
}
