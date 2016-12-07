package com.github.hayarobi.simple_config.sample;

import java.util.List;

public class CycleByCollection {
	private List<InsideList> list;

	/**
	 * @return the list
	 */
	public List<InsideList> getList() {
		return list;
	}
	
}

class InsideList {
	private RefToTop rtt;

	/**
	 * @return the rtt
	 */
	public RefToTop getRtt() {
		return rtt;
	}
	
}

class RefToTop {
	private CycleByCollection top;

	/**
	 * @return the top
	 */
	public CycleByCollection getTop() {
		return top;
	}
	
}