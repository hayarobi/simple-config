package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.Config;

@Config("cyclic")
public class CyclicPropsConfig {
	private DirectCycleA directA;
	
	private IndirectCycle indirect;

	/**
	 * @return the directA
	 */
	public DirectCycleA getDirectA() {
		return directA;
	}

	/**
	 * @return the indirect
	 */
	public IndirectCycle getIndirect() {
		return indirect;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CyclicPropsConfig [directA=");
		builder.append(directA);
		builder.append(", indirect=");
		builder.append(indirect);
		builder.append("]");
		return builder.toString();
	}
	
	
}
