package com.github.hayarobi.simple_config.load;

import java.util.List;
import java.util.Map;

/**
 * 설정 파일에서 읽어온 설정을 항목에 대한 트리 노드 
 * @author sg13park
 *
 */
public interface RawConfig {
	public String getName();
	
	/**
	 * 해당 프로퍼티 이름을 가진 설정값으로 구성된 {@link RawConfig} 객체를 반환한다. 만약 설정 소스에 
	 * 관련된 게 하나도 없으면 내용이 비어있는 객체를 반환한다. 즉 null을 반환하는 일이 없다.
	 * @param propertyName 찾으려는 프로퍼티 이름.
	 * @return instance of {@link RawConfig} 
	 */
	public RawConfig findSubConfig(String propertyName);

//	public boolean existProperty(String propertyName);
	public String getPropertyStringValue(String propertyName) throws PropertyNotFoundException, InvalidPropertyTypeException;
	public List<String> getPropertyListValue(String propertyName) throws PropertyNotFoundException, InvalidPropertyTypeException;
	public Map<String, String> getPropertyMapValue(String propertyName) throws PropertyNotFoundException, InvalidPropertyTypeException;
}
