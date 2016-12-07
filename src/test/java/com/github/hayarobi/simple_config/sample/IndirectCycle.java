package com.github.hayarobi.simple_config.sample;

public class IndirectCycle {
	private MidClass mid;

	private int cyclicValue;
	
	/**
	 * @return the mid
	 */
	public MidClass getMid() {
		return mid;
	}

	/**
	 * @return the cyclicValue
	 */
	public int getCyclicValue() {
		return cyclicValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndirectCycle [mid=");
		builder.append(mid);
		builder.append(", cyclicValue=");
		builder.append(cyclicValue);
		builder.append("]");
		return builder.toString();
	}
}