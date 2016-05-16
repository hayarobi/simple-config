package com.github.hayarobi.simple_config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.hayarobi.simple_config.ConfigService;
import com.github.hayarobi.simple_config.ConfigServiceFactory;
import com.github.hayarobi.simple_config.sample.DataConfig;
import com.github.hayarobi.simple_config.sample.OtherConfig;

public class ConfigServiceFactoryTest {
	public static final String SAMPLECONF_PROPERTIES = "sampleconf.properties";
	public static final String SAMPLE_LIST_PROPERTIES = "sample-list.properties";
	public static final String SAMPLECONF_YAML = "yaml/sampleconf.yaml";

	public static final String DIR_PREFIX = "src/test/resources/";
	
	@Test
	public void testCraeteServiceFromResource() {
		ConfigServiceFactory target = new ConfigServiceFactory();
		
		ConfigService actual = target.craeteServiceFromResource(SAMPLECONF_PROPERTIES);
		assertNotNull(actual);
		
		DataConfig dconf0 = actual.getConfig(DataConfig.class);
		assertNotNull(dconf0);
		assertEquals("http://www.score.co.kr", dconf0.getUrl());
		assertEquals("tester", dconf0.getUser());
		assertEquals("p@ssw0rd", dconf0.getPass());
		assertEquals(5, dconf0.getMaxConn());

		OtherConfig sconf0 = actual.getConfig(OtherConfig.class);
		assertNotNull(sconf0);
		assertEquals("http://localhost:6800/", sconf0.getUrl());
		assertEquals("/home/search/tmp/result", sconf0.getResultPath());
		assertEquals(30, sconf0.getNumberOfArticles());
		assertEquals(15, sconf0.getMaxConn());
		assertEquals(99.9, sconf0.getEra(), 0.00001);

		try {
			target.craeteServiceFromResource("nothing.properties");
			fail();
		} catch(IllegalArgumentException expectedEx) {
			// TODO: 예외를 정하고 나면 바뀌어야할 것 같다.
		}
	}


	@Test
	public void testCraeteServiceWithYaml() {
		ConfigServiceFactory target = new ConfigServiceFactory();
		
		ConfigService actual = target.craeteServiceFromResource(SAMPLECONF_YAML);
		assertNotNull(actual);
		
		DataConfig dconf0 = actual.getConfig(DataConfig.class);
		assertNotNull(dconf0);
		assertEquals("http://www.score.co.kr", dconf0.getUrl());
		assertEquals("tester", dconf0.getUser());
		assertEquals("p@ssw0rd", dconf0.getPass());
		assertEquals(5, dconf0.getMaxConn());

		OtherConfig sconf0 = actual.getConfig(OtherConfig.class);
		assertNotNull(sconf0);
		assertEquals("http://localhost:6800/", sconf0.getUrl());
		assertEquals("/home/search/tmp/result", sconf0.getResultPath());
		assertEquals(30, sconf0.getNumberOfArticles());
		assertEquals(15, sconf0.getMaxConn());
		assertEquals(99.9, sconf0.getEra(), 0.00001);

		try {
			target.craeteServiceFromResource("nothing.properties");
			fail();
		} catch(IllegalArgumentException expectedEx) {
			// TODO: 예외를 정하고 나면 바뀌어야할 것 같다.
		}
	}
	
	@Test
	public void testCraeteServiceFromResourceWithPreload() {
		ConfigServiceFactory target = new ConfigServiceFactory();
		
		ConfigService actual = target.craeteServiceFromResource(SAMPLECONF_PROPERTIES, true, "com.github.hayarobi.simple_config");
		assertNotNull(actual);
		
		DataConfig dconf0 = actual.getConfig(DataConfig.class);
		assertNotNull(dconf0);
		assertEquals("http://www.score.co.kr", dconf0.getUrl());
		assertEquals("tester", dconf0.getUser());
		assertEquals("p@ssw0rd", dconf0.getPass());
		assertEquals(5, dconf0.getMaxConn());

		OtherConfig sconf0 = actual.getConfig(OtherConfig.class);
		assertNotNull(sconf0);
		assertEquals("http://localhost:6800/", sconf0.getUrl());
		assertEquals("/home/search/tmp/result", sconf0.getResultPath());
		assertEquals(30, sconf0.getNumberOfArticles());
		assertEquals(15, sconf0.getMaxConn());
		assertEquals(99.9, sconf0.getEra(), 0.00001);

		try {
			target.craeteServiceFromResource("nothing.properties");
			fail();
		} catch(IllegalArgumentException expectedEx) {
			// TODO: 예외를 정하고 나면 바뀌어야할 것 같다.
		}
	}
	@Test
	public void testCraeteServiceFromFile() {
		ConfigServiceFactory target = new ConfigServiceFactory();
		ConfigService expected = target.craeteServiceFromResource(SAMPLECONF_PROPERTIES);
		ConfigService actual = target.craeteServiceFromFile(DIR_PREFIX+SAMPLECONF_PROPERTIES);
		assertNotNull(actual);

		DataConfig expDconf = expected.getConfig(DataConfig.class);
		DataConfig actDconf = actual.getConfig(DataConfig.class);
		assertEquals(expDconf, actDconf);
		
		try {
			target.craeteServiceFromFile("No/Dir/"+SAMPLECONF_PROPERTIES);
			fail();
		} catch(IllegalArgumentException expectedEx) {
			// TODO: 예외를 정하고 나면 바뀌어야할 것 같다.
		}
	}
	
	@Test
	public void testCreateYamlService() {
		ConfigServiceFactory target = new ConfigServiceFactory();
		ConfigService expected = target.craeteServiceFromResource(SAMPLECONF_YAML);
		ConfigService actual = target.craeteServiceFromFile(DIR_PREFIX+SAMPLECONF_YAML);
		assertNotNull(actual);

		DataConfig expDconf = expected.getConfig(DataConfig.class);
		DataConfig actDconf = actual.getConfig(DataConfig.class);
		assertEquals(expDconf, actDconf);
		
		try {
			target.craeteServiceFromFile("No/Dir/"+SAMPLECONF_YAML);
			fail();
		} catch(IllegalArgumentException expectedEx) {
			// TODO: 예외를 정하고 나면 바뀌어야할 것 같다.
		}

	}

}
