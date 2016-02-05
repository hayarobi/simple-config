package com.github.hayarobi.simple_config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.sample.DataConfig;

public class TestConfig {

	@Test
	public void findClasses() {
		Class<?> targetClass = DataConfig.class;
		
		Config anno = targetClass.getAnnotation(Config.class);
		assertNotNull(anno);
		System.out.println(anno.value());
		System.out.println(anno);
	}
}
