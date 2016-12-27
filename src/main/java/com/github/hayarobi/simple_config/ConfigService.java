package com.github.hayarobi.simple_config;

/**
 * 설정을 저장 관리하는 서비스 객체의 인터페이스
 * @author Hayarobi Park
 *
 */
public interface ConfigService {
	public <T> T getConfig(Class<T> clazz);
}
