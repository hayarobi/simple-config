package com.github.hayarobi.simple_config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.load.PropertiesReader;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.SourceReader;
import com.github.hayarobi.simple_config.load.ValueExtractorManager;

/**
 * configService를 생성하기 위한 팩토리 클래스.
 * 
 * @author sg13park
 *
 */
public class ConfigServiceFactory {
	public ConfigServiceFactory() {
	}

	public ConfigService craeteServiceFromResource(String resourcePath) {
		InputStream inputStream = getInputstreamFromResource(resourcePath);
		RawConfig propMap;
		try {
			propMap = selectSourceReader(resourcePath).read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+resourcePath, e);
		}
		ValueExtractorManager vem = new ValueExtractorManager();
		ConfigLoader loader = new ConfigLoader(propMap, vem);
		return new LazyConfigService(loader);
	}
	
	public ConfigService craeteServiceFromFile(String filePath) {
		InputStream inputStream = getInputstreamFromFile(filePath);
		RawConfig propMap;
		try {
			propMap = selectSourceReader(filePath).read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("IO exception while reading config source "+filePath, e);
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
		// FIXME 일단은 properties리더만 있다. 
		return new PropertiesReader();
	}


}
