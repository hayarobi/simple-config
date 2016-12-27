package com.github.hayarobi.simple_config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.SourceReader;
import com.github.hayarobi.simple_config.load.SourceType;
import com.github.hayarobi.simple_config.load.preload.ConfigClassScanner;
import com.github.hayarobi.simple_config.load.preload.PreloadConfigService;
import com.github.hayarobi.simple_config.load.properties.PropertiesReader;
import com.github.hayarobi.simple_config.load.yaml.YamlReader;

/**
 * configService를 생성하기 위한 팩토리 클래스.
 * 
 * @author Hayarobi Park
 *
 */
public class ConfigServiceFactory {
	private Logger log = LoggerFactory.getLogger(ConfigServiceFactory.class);
	public ConfigServiceFactory() {
	}

	public ConfigService createServiceFromResource(String resourcePath, boolean preload, String preloadPackges) {
		InputStream inputStream = getInputstreamFromResource(resourcePath);
		RawConfContainer propMap;
		try {
			propMap = selectSourceReader(resourcePath).read(inputStream);
			
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+resourcePath, e);
		}
		if(log.isTraceEnabled()) {
			log.trace("Loaded resource {} :\n {}", resourcePath, propMap);
		}
		ConfigLoader loader = new ConfigLoader(propMap, null);
		return createService(preload, loader, preloadPackges);
	}

	private ConfigService createService(boolean preload, ConfigLoader loader, String preloadPackges) {
		if( preload ) {
			String[] packagesToScan = preloadPackges.split(",");
			ConfigClassScanner confScanner = new ConfigClassScanner();
			PreloadConfigService service = new PreloadConfigService();
			service.initWith(loader, confScanner, packagesToScan);
			return service;
		} else {
			return new LazyConfigService(loader);
		}
	}

	public ConfigService createServiceFromResource(String resourcePath) {
		return createServiceFromResource(resourcePath, false, null);
	}
	
	public ConfigService createServiceFromFile(String filePath, boolean preload, String preloadPackges) {
		InputStream inputStream = getInputstreamFromFile(filePath);
		RawConfContainer rawConfigContainer;
		try {
			rawConfigContainer = selectSourceReader(filePath).read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+filePath, e);
		}
		if(log.isTraceEnabled()) {
			log.trace("Loaded file {} :\n {}", filePath, rawConfigContainer);
		}
		ConfigLoader loader = new ConfigLoader(rawConfigContainer, null);
		return createService(preload, loader, preloadPackges);
	}

	public ConfigService createServiceFromFile(String filePath) {
		return createServiceFromFile(filePath, false, null);
	}
	
	private InputStream getInputstreamFromResource(String resourcePath) {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
		if (null == inputStream) {
			throw new IllegalArgumentException("resource was not found. " + resourcePath);
		}
		return inputStream;
	}


	private InputStream getInputstreamFromFile(String filePath) {
		InputStream is;
		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found. " + filePath);
		}
		return is;
	}

	private SourceReader selectSourceReader(String path) {
		int index = path.lastIndexOf('.');
		if( index > 0 && index < path.length() - 1 ) {
			String extension = path.substring(index+1).toUpperCase() ;
			try {
			SourceType type = SourceType.valueOf(extension);
			switch (type) {
			case YAML:
				return new YamlReader();
			case PROPERTIES:
			default:
				return new PropertiesReader();
			}
			} catch(IllegalArgumentException e) {
				
			}
		}
		throw new IllegalArgumentException("Not supported source type "+path);
	}


}
