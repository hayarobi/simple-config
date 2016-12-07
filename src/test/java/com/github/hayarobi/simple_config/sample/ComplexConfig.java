package com.github.hayarobi.simple_config.sample;

import java.util.List;
import java.util.Map;

import com.github.hayarobi.simple_config.annotation.Config;

@Config("complex")
public class ComplexConfig {
	private String str;
	
	private PojoSub1 sub1;
	
	private EnumSample enum1;
	
	private List<LoginInfo> loginList;
	
	private Map<Integer, NodeInfo> nodeMap;

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	/**
	 * @return the sub1
	 */
	public PojoSub1 getSub1() {
		return sub1;
	}

	/**
	 * @return the enum1
	 */
	public EnumSample getEnum1() {
		return enum1;
	}

	/**
	 * @return the loginList
	 */
	public List<LoginInfo> getLoginList() {
		return loginList;
	}

	/**
	 * @return the nodeMap
	 */
	public Map<Integer, NodeInfo> getNodeMap() {
		return nodeMap;
	}
}
