package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.yaml.TreeNodeRawConfig;
import com.github.hayarobi.simple_config.load.yaml.snakeyaml.YamlReader;

public class YamlReaderTest {
	public static final String SAMPLE_YAML = "yaml/sampleconf.yaml";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() throws IOException {
		InputStream inputStream = YamlTest.class.getClassLoader().getResourceAsStream(SAMPLE_YAML);
		YamlReader target = new YamlReader();
		RawConfig rootConfig = target.read(inputStream);
		
		assertNotNull(rootConfig);
		assertTrue(rootConfig instanceof TreeNodeRawConfig);
		
		RawConfig actual = null;
		
		actual = rootConfig.findSubConfig("list.and");
		assertNotNull(actual);
		assertEquals("list.and", actual.getName());
		
		RawConfig subActual = actual.findSubConfig("date");
		assertNotNull(subActual);
		assertEquals("list.and.date", subActual.getName());
		assertNotNull(subActual.getPropertyListValue("fruits"));
		assertNotNull(subActual.getPropertyMapValue("magicNumbers"));
		assertNotNull(subActual.getPropertyStringValue("fromTime"));
		
		actual = rootConfig.findSubConfig("list.and.date");
		assertNotNull(actual);
		assertEquals("list.and.date", actual.getName());
		assertNotNull(actual.getPropertyListValue("fruits"));
		assertNotNull(actual.getPropertyMapValue("magicNumbers"));
		assertNotNull(actual.getPropertyStringValue("fromTime"));
		
		System.out.println(actual);
		assertEquals(actual, subActual);
	}

}
