package com.github.hayarobi.simple_config;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.ConfigLoader;
import com.github.hayarobi.simple_config.sample.DataConfig;
import com.github.hayarobi.simple_config.sample.EnumSample;
import com.github.hayarobi.simple_config.sample.EnumTestConfig;
import com.github.hayarobi.simple_config.sample.ListAndDateConfig;
import com.github.hayarobi.simple_config.sample.OtherConfig;

public class ConfigLoaderTest {

	public static final String SAMPLE_LIST_PROPERTIES = "sample-list.properties";
	public static final String SAMPLECONF_PROPERTIES = "sampleconf.properties";


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public void testConfigLoaderConstructors() throws IOException {
		Properties baseProp = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(SAMPLECONF_PROPERTIES);
			baseProp.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		Map<String, String> baseMap = new HashMap<String, String>((Map)baseProp);
		
		ConfigLoader loader1 = new ConfigLoader(baseMap);
		DataConfig dconf1 = loader1.loadConfig(DataConfig.class);
		OtherConfig sconf1 = loader1.loadConfig(OtherConfig.class);
		assertNotNull(dconf1);
		assertNotNull(sconf1);
		
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(SAMPLECONF_PROPERTIES);
		baseProp.load(inputStream);
		baseMap = new HashMap<String, String>((Map)baseProp);
		
		ConfigLoader loader2 = new ConfigLoader(baseMap);
		DataConfig dconf2 = loader1.loadConfig(DataConfig.class);
		OtherConfig sconf2 = loader1.loadConfig(OtherConfig.class);
		
		assertEquals(dconf1, dconf2);
		assertEquals(sconf1, sconf2);
		
		
	}

	@Test
	public void testGetConfig() {
		Properties fullProps = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(SAMPLECONF_PROPERTIES);
			fullProps.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		Map<String, String> baseMap = new HashMap<String, String>((Map)fullProps);


		ConfigLoader target = new ConfigLoader(baseMap);

		DataConfig dconf0 = target.loadConfig(DataConfig.class);
		assertNotNull(dconf0);
		assertEquals("http://www.score.co.kr", dconf0.getUrl());
		assertEquals("tester", dconf0.getUser());
		assertEquals("p@ssw0rd", dconf0.getPass());
		assertEquals(5, dconf0.getMaxConn());

		OtherConfig sconf0 = target.loadConfig(OtherConfig.class);
		assertNotNull(sconf0);
		assertEquals("http://localhost:6800/", sconf0.getUrl());
		assertEquals("/home/search/tmp/result", sconf0.getResultPath());
		assertEquals(30, sconf0.getNumberOfArticles());
		assertEquals(15, sconf0.getMaxConn());
		assertEquals(99.9, sconf0.getEra(), 0.00001);
		
		// required field가 빠진 경우
		Map<String, String> missingNotRequired = new HashMap<String, String>(baseMap);
		missingNotRequired.remove("com.github.hayarobi.simple_config.sample.DataConfig.user");
		missingNotRequired.remove("com.github.hayarobi.simple_config.sample.DataConfig.pass");
		missingNotRequired.remove("theothers.numatc");
		target = new ConfigLoader(missingNotRequired);
		DataConfig dconf1 = target.loadConfig(DataConfig.class);
		assertEquals(dconf0.getUrl(), dconf1.getUrl());
		assertNull(dconf1.getUser());
		assertNull(dconf1.getPass());
		assertEquals(dconf0.getMaxConn(), dconf1.getMaxConn());
		OtherConfig sconf1 = target.loadConfig(OtherConfig.class);
		// default 값이 나와야한다.
		assertEquals(300, sconf1.getNumberOfArticles());
		assertEquals(sconf0.getMaxConn(), sconf1.getMaxConn());

		// required field가 빠진 경우 
		Map<String, String> missingRequired = new HashMap<String, String>(baseMap);
		missingRequired.remove("theothers.result");
		target = new ConfigLoader(missingRequired);
		try {
			OtherConfig toException = target.loadConfig(OtherConfig.class);
			fail();
		}  catch (RuntimeException ex) {
			assertTrue(ex.getMessage().startsWith("The value of required field"));
			assertTrue(ex.getMessage().contains("is missing"));
		}
	}
	
	
	@Test
	public void testArrayAndDate() {
		Properties fullProps = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(SAMPLE_LIST_PROPERTIES);
			fullProps.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		Map<String, String> baseMap = new HashMap<String, String>((Map)fullProps);

		ConfigLoader target = new ConfigLoader(baseMap);

		ListAndDateConfig conf = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf.getFruitList());
		System.out.println("list1 : "+conf.getFruitList());
		assertEquals(3, conf.getFruitList().size());
		assertTrue(conf.getFruitList().contains("apple"));
		assertTrue(conf.getFruitList().contains("orange"));
		assertTrue(conf.getFruitList().contains("mango"));

		assertEquals(2, conf.getMagicNumbers().size());
		System.out.println("list2 : "+conf.getMagicNumbers());
		assertTrue(conf.getMagicNumbers().contains(15));
		assertTrue(conf.getMagicNumbers().contains(20));
		assertNull(conf.getToBeNull());

		assertNull(conf.getIgnoredField());
		assertNotNull(conf.getFromTime());
		System.out.println( "2015-03-02 12:34:56.111+05:00 and "+conf.getFromTime());

		Map<String, String> props1 = new HashMap<String, String>(baseMap);
		props1.put("list.and.date.fromTime", "2015-04-03 12:34:56.111");
		target = new ConfigLoader(props1);
		ListAndDateConfig conf1 = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf1.getFromTime());
		System.out.println( "2015-04-03 12:34:56.111 and "+conf1.getFromTime());

		props1.put("list.and.date.fromTime", "2015-05-04 12:34:56");
		target = new ConfigLoader(props1);
		conf1 = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf1.getFromTime());
		System.out.println( "2015-05-04 12:34:56 and "+conf1.getFromTime());
	}
	
	@Test
	public void testEnum() {
		Properties fullProps = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(SAMPLECONF_PROPERTIES);
			fullProps.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		Map<String, String> baseMap = new HashMap<String, String>((Map)fullProps);

		ConfigLoader target = new ConfigLoader(baseMap);

		EnumTestConfig actual = target.loadConfig(EnumTestConfig.class);
		assertNotNull(actual);
		
		assertEquals(EnumSample.EARTH, actual.getPlanet());
		assertEquals(RoundingMode.DOWN, actual.getRound());
		assertNotNull(actual.getLists());
		assertEquals(3, actual.getLists().size());
		assertEquals(EnumSample.VENUS, actual.getLists().get(0));
		assertEquals(EnumSample.MARS, actual.getLists().get(1));
		assertEquals(EnumSample.JUPITER, actual.getLists().get(2));
		
		// 잘못된 literal이 들어가 있을 경우.
		Map<String, String> wrongLiterals = new HashMap<String, String>(baseMap);
		wrongLiterals.put("enums.planet", "PLUTO");
		try {
			target = new ConfigLoader(wrongLiterals);
			actual = target.loadConfig(EnumTestConfig.class);
			fail();
		} catch(RuntimeException e) {
			System.out.println("Expected RuntimeException caused by IllegalArgumentException: "+e.getMessage());
			assertEquals(IllegalArgumentException.class,  e.getCause().getClass() );
		}
	}
}
