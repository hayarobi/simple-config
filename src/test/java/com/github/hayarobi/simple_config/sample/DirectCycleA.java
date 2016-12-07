package com.github.hayarobi.simple_config.sample;

public class DirectCycleA {
	private String name;
	
	private DitectCycleB b;

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the b
	 */
	public DitectCycleB getB() {
		return b;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DirectCycleA [name=");
		builder.append(name);
		builder.append(", b=");
		builder.append(b);
		builder.append("]");
		return builder.toString();
	}
	
	
}
