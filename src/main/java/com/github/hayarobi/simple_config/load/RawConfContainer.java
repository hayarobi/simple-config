package com.github.hayarobi.simple_config.load;

public interface RawConfContainer {
	/**
	 * RawConfig를 반환한다. 설정 소스에 해당 부분이 없으면 값이 비어있는 설정을 반환한다.
	 * @param confName
	 * @return
	 */
	public RawConfig findConfig(String confName);

	public RawConfig createEmptyConfig();
}
