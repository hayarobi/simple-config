package com.github.hayarobi.simple_config.sample;

public class DitectCycleB {
	private int numValue;
	
	private DirectCycleA aAndCycle;

	
	
	/**
	 * @return the numValue
	 */
	public int getNumValue() {
		return numValue;
	}



	/**
	 * @return the aAndCycle
	 */
	public DirectCycleA getaAndCycle() {
		return aAndCycle;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DitectCycleB [numValue=");
		builder.append(numValue);
		builder.append(", aAndCycle=");
		builder.append(aAndCycle);
		builder.append("]");
		return builder.toString();
	}
	
	
}