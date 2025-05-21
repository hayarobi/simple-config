package com.github.hayarobi.simple_config.experiments;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class PropertiesExperiment {
	@Test
	public void testGetConfig() {
		Properties fullProps = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("samenames.properties");
			fullProps.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		
		
		Object frs = fullProps.get("list.and.date.fruits");
		System.out.println(fullProps.getProperty("list.and.date.fruits"));
		System.out.println(frs.toString());
	}
}
