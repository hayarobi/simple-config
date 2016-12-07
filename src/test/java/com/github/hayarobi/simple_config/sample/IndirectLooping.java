package com.github.hayarobi.simple_config.sample;

public class IndirectLooping {
	private IndirectCycle backToTop;

	/**
	 * @return the backToTop
	 */
	public IndirectCycle getBackToTop() {
		return backToTop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndirectLooping [backToTop=");
		builder.append(backToTop);
		builder.append("]");
		return builder.toString();
	}
	
	
}