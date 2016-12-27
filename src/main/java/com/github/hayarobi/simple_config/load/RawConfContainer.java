package com.github.hayarobi.simple_config.load;

/**
 * raw 설정을 포함하는 관리용 객체
 * @author Hayarobi Park
 *
 */
public interface RawConfContainer {
	/**
	 * RawConfig를 반환한다. 설정 소스에 해당 부분이 없으면 값이 비어있는 설정을 반환한다.
	 * @param confName 설정 객체 이름
	 * @return confName에 해당하는 {@link RawConfig} 객체
	 */
	public RawConfig findConfig(String confName);

	public RawConfig createEmptyConfig();
}
