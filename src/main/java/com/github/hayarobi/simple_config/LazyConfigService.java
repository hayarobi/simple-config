package com.github.hayarobi.simple_config;

import java.util.concurrent.ConcurrentHashMap;

import com.github.hayarobi.simple_config.load.ConfigLoader;

/**
 * 설정을 저장 관리하는 클래스.
 * @author Hayarobi Park
 *
 */
public class LazyConfigService implements ConfigService {
	private ConfigLoader loader;
	private ConcurrentHashMap<Class<Object>, Object> confMap;
	
	public LazyConfigService(ConfigLoader loader) {
		this.loader = loader;
		this.confMap = new ConcurrentHashMap<Class<Object>, Object>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getConfig(Class<T> clazz) {
		T confObj = (T)confMap.get(clazz);
		synchronized (this) {
			if( null == confObj ) {
				confObj = loader.loadConfig(clazz);				
				confMap.put((Class<Object>)clazz, (Object)confObj);
			}
		}
		return confObj;
	}
	
}
