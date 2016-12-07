package com.github.hayarobi.simple_config.load;

import java.util.List;
import java.util.Map;

/**
 * 설정 파일에서 읽어온 설정을 항목에 대한 트리 노드 
 * @author sg13park
 *
 */
public interface RawConfig {
	public static final String TYPE_UNIT = "UnitValue";
	public static final String TYPE_LIST = "List";
	public static final String TYPE_MAP = "Map";

	/**
	 * 자식 노드를 반환한다. 
	 * @param propertyName
	 * @return
	 * @throws PropertyNotFoundException 해당 이름의 자식 노드가 없을 경우 
	 */
	public String getStringValue() throws InvalidPropertyTypeException;
	public List<RawConfig> getChildrenAsList() throws InvalidPropertyTypeException;
	public Map<String, RawConfig> getChildrenAsMap() throws InvalidPropertyTypeException;
}
