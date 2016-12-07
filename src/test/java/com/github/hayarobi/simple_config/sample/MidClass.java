package com.github.hayarobi.simple_config.sample;

public class MidClass {
	private IndirectLooping loop;

	/**
	 * @return the loop
	 */
	public IndirectLooping getLoop() {
		return loop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MidClass [loop=");
		builder.append(loop);
		builder.append("]");
		return builder.toString();
	}
	
	
}