package com.github.hayarobi.simple_config.experiments;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.sample.DataConfig;
import org.junit.jupiter.api.Test;

public class TestConfig {

	@org.junit.jupiter.api.Test
	public void findClasses() {
		Class<?> targetClass = DataConfig.class;
		
		Config anno = targetClass.getAnnotation(Config.class);
		assertNotNull(anno);
		System.out.println(anno.value());
		System.out.println(anno);
	}
}
