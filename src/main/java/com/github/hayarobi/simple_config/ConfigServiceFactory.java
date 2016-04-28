package com.github.hayarobi.simple_config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.load.PropertiesReader;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.SourceReader;
import com.github.hayarobi.simple_config.load.ValueExtractorManager;
import com.github.hayarobi.simple_config.load.preload.PreloadConfigService;

/**
 * configService를 생성하기 위한 팩토리 클래스.
 * 
 * @author sg13park
 *
 */
public class ConfigServiceFactory {
	private Logger log = LoggerFactory.getLogger(ConfigServiceFactory.class);
	public ConfigServiceFactory() {
	}

	public ConfigService craeteServiceFromResource(String resourcePath, boolean preload, String preloadPackges) {
		InputStream inputStream = getInputstreamFromResource(resourcePath);
		RawConfig propMap;
		try {
			propMap = selectSourceReader(resourcePath).read(inputStream);
			
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+resourcePath, e);
		}
		if(log.isTraceEnabled()) {
			log.trace("Loaded resource {} :\n {}", resourcePath, propMap);
		}
		ValueExtractorManager vem = new ValueExtractorManager();
		ConfigLoader loader = new ConfigLoader(propMap, vem);
		return createService(preload, loader, preloadPackges);
	}

	private ConfigService createService(boolean preload, ConfigLoader loader, String preloadPackges) {
		if( preload ) {
			String[] packagesToScan = preloadPackges.split(",");
			return new PreloadConfigService(loader, packagesToScan);
		} else {
			return new LazyConfigService(loader);
		}
	}

	public ConfigService craeteServiceFromResource(String resourcePath) {
		return craeteServiceFromResource(resourcePath, false, null);
	}
	
	public ConfigService craeteServiceFromFile(String filePath) {
		InputStream inputStream = getInputstreamFromFile(filePath);
		RawConfig propMap;
		try {
			propMap = selectSourceReader(filePath).read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+filePath, e);
		}
		if(log.isTraceEnabled()) {
			log.trace("Loaded file {} :\n {}", filePath, propMap);
		}
		ValueExtractorManager vem = new ValueExtractorManager();
		ConfigLoader loader = new ConfigLoader(propMap, vem);
		return new LazyConfigService(loader);
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

	private SourceReader selectSourceReader(String resourcePath) {
		// 확장자로부터 적절한 리더 객체를 선정.
		// FIXME 일단은 properties리더만 있다. 
		return new PropertiesReader();
	}


}
