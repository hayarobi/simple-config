package com.github.hayarobi.simple_config.load.preload;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.ConfigService;
import com.github.hayarobi.simple_config.load.ConfigLoader;

/**
 * 설정을 저장 관리하는 클래스.
 * @author sg13park
 *
 */
public class PreloadConfigService implements ConfigService {
	private Logger log = LoggerFactory.getLogger(PreloadConfigService.class);
	
	private ConfigLoader loader;
	private ConcurrentHashMap<Class<Object>, Object> confMap;
	
	public PreloadConfigService(ConfigLoader loader, String[] scanPackages) {
		this.loader = loader;
		this.confMap = new ConcurrentHashMap<Class<Object>, Object>();
		
		init(scanPackages);
	}

	@SuppressWarnings("unchecked")
	private void init(String[] scanPackages) {
		if( log.isDebugEnabled() ) {
			log.debug("Preloading config objects");
		}
		ConfigClassScanner confScanner = new ConfigClassScanner();
		for (String packageName : scanPackages) {
			if( packageName.isEmpty() ) {
				continue;
			}
			List<Class<?>> confClasses = confScanner.findConfigClassesBelow(packageName);
			for (Class<?> clazz : confClasses) {
				Object confObj = loader.loadConfig(clazz);				
				confMap.put((Class<Object>)clazz, (Object)confObj);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getConfig(Class<T> clazz) {
		T confObj = (T)confMap.get(clazz);
		if( null == confObj ) {
				throw new IllegalArgumentException("The class " + clazz.getName()
						+ " is not config class.");
		}
		return confObj;
	}
	
}
