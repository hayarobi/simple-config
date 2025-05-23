package com.github.hayarobi.simple_config.load;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.properties.PropertiesReader;
import com.github.hayarobi.simple_config.sample.DataConfig;
import com.github.hayarobi.simple_config.sample.EnumSample;
import com.github.hayarobi.simple_config.sample.EnumTestConfig;
import com.github.hayarobi.simple_config.sample.IMRequiredConf;
import com.github.hayarobi.simple_config.sample.ListAndDateConfig;
import com.github.hayarobi.simple_config.sample.MapAndAbstractionConfig;
import com.github.hayarobi.simple_config.sample.NodeInfo;
import com.github.hayarobi.simple_config.sample.OtherConfig;

public class ConfigLoaderTest {

	public static final String SAMPLE_LIST_PROPERTIES = "sample-list.properties";
	public static final String SAMPLECONF_PROPERTIES = "sampleconf.properties";

	private PropertiesReader reader;


	@BeforeEach
	public void setUp() throws Exception {
		this.reader = new PropertiesReader();
	}

	@AfterEach
	public void tearDown() throws Exception {
	}
	
	public void testConfigLoaderConstructor() throws IOException {
	
		ConfigLoader loader1 = new ConfigLoader(createRootConfig(SAMPLECONF_PROPERTIES), null);
		DataConfig dconf1 = loader1.loadConfig(DataConfig.class);
		OtherConfig sconf1 = loader1.loadConfig(OtherConfig.class);
		assertNotNull(dconf1);
		assertNotNull(sconf1);
	}

	@Test
	public void testGetConfig() {
		Map<String, String> baseMap = loadResourceToMap(SAMPLECONF_PROPERTIES);

		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);

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
		
		// no config class
		try {
			ConfigLoader nothing = target.loadConfig(ConfigLoader.class);
			fail();
		} catch(IllegalArgumentException expected ) {
			assertTrue(expected.getMessage().endsWith("is not config class."));
		}
		// required field가 빠진 경우
		Map<String, String> missingNotRequired = new HashMap<String, String>(baseMap);
		missingNotRequired.remove("com.github.hayarobi.simple_config.sample.DataConfig.user");
		missingNotRequired.remove("com.github.hayarobi.simple_config.sample.DataConfig.pass");
		missingNotRequired.remove("theothers.numatc");
		target = new ConfigLoader(reader.createRCContainerFromMap(missingNotRequired), null);
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
		target = new ConfigLoader(reader.createRCContainerFromMap(missingRequired), null);
		try {
			OtherConfig toException = target.loadConfig(OtherConfig.class);
			fail();
		}  catch (RuntimeException ex) {
			assertTrue(ex.getMessage().startsWith("The value of required field"));
			assertTrue(ex.getMessage().contains("is missing"));
		}
	}

	/**
	 * @param propResourceName 
	 * @return
	 */
	protected RawConfContainer createRootConfig(String propResourceName) {
		
		Map<String, String> baseMap = loadResourceToMap(propResourceName);
		return reader.createRCContainerFromMap(baseMap);
	}

	/**
	 * @param propResourceName
	 * @return
	 */
	protected Map<String, String> loadResourceToMap(String propResourceName) {
		Properties fullProps = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(propResourceName);
			fullProps.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		Map<String, String> baseMap = new HashMap<String, String>((Map)fullProps);
		return baseMap;
	}
	
	
	@Test
	public void testArrayAndDate() {
		Map<String, String> baseMap = loadResourceToMap(SAMPLE_LIST_PROPERTIES);

		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);

		ListAndDateConfig conf = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf.getFruitList());
		System.out.println("list1 : "+conf.getFruitList());
		assertEquals(3, conf.getFruitList().size());
		assertEquals("apple", conf.getFruitList().get(0));
		assertEquals("orange", conf.getFruitList().get(1));
		assertEquals("mango", conf.getFruitList().get(2));

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
		target = new ConfigLoader(reader.createRCContainerFromMap(props1), null);
		ListAndDateConfig conf1 = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf1.getFromTime());
		System.out.println( "2015-04-03 12:34:56.111 and "+conf1.getFromTime());

		props1.put("list.and.date.fromTime", "2015-05-04 12:34:56");
		target = new ConfigLoader(reader.createRCContainerFromMap(props1), null);
		conf1 = target.loadConfig(ListAndDateConfig.class);
		assertNotNull(conf1.getFromTime());
		System.out.println( "2015-05-04 12:34:56 and "+conf1.getFromTime());
	}
	
	@Test
	public void testListAndDate2() {
		Map<String, String> baseMap = loadResourceToMap(SAMPLE_LIST_PROPERTIES);

		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);

		ListAndDateConfig conf = target.loadConfig(ListAndDateConfig.class);

		assertNull(conf.getNullList());
		List<NodeInfo> empty = conf.getEmptyList();
		assertNotNull(empty);
		assertTrue(empty.isEmpty());
		
		List<OtherConfig> pojoList = conf.getPojoList();
		assertNotNull(pojoList);
		assertEquals(4, pojoList.size());
		System.out.println("PojoList "+pojoList.toString());
	}
	
	@Test
	public void testEnum() {
		Map<String, String> baseMap = loadResourceToMap(SAMPLECONF_PROPERTIES);
		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);


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
			target = target = new ConfigLoader(reader.createRCContainerFromMap(wrongLiterals), null);
			actual = target.loadConfig(EnumTestConfig.class);
			fail();
		} catch(RuntimeException e) {
			System.out.println("Expected RuntimeException caused by IllegalArgumentException: "+e.getMessage());
			assertEquals(IllegalArgumentException.class,  e.getCause().getClass() );
		}
	}
	
	@Test
	public void testMap() {
		Map<String, String> baseMap = loadResourceToMap(SAMPLE_LIST_PROPERTIES);
		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);

		MapAndAbstractionConfig conf = target.loadConfig(MapAndAbstractionConfig.class);
		assertNotNull(conf.getFruitList());
		System.out.println("list1 : "+conf.getFruitList());
		assertEquals(3, conf.getFruitList().size());
		assertEquals("apple", conf.getFruitList().get(0));
		assertEquals("orange", conf.getFruitList().get(1));
		assertEquals("mango", conf.getFruitList().get(2));

		Map<Long, String> actualMap = conf.getFruitsMap();
		assertNotNull(actualMap);
		System.out.println("map : "+ actualMap);
		assertEquals(3, actualMap.size());
		assertEquals("apple", actualMap.get(1L));
		assertEquals("orange", actualMap.get(2L));
		assertEquals("mango", actualMap.get(3L));

		SortedMap<String, Integer> numbersMap = conf.getMagicNumbers();
		assertEquals(2, numbersMap.size());
		System.out.println("sortedMap : "+numbersMap);
		assertTrue(numbersMap.containsKey("first"));
		assertTrue(numbersMap.containsKey("last"));
		assertEquals(Integer.valueOf(15), numbersMap.get("first"));
		assertEquals(Integer.valueOf(20), numbersMap.get("last"));

	}
	
	@Test
	public void testRequiredPropConfClass() {
		Map<String, String> baseMap = new HashMap<String, String>();
		baseMap.put("IMRequiredConf.trueAnnoInt", "4");
		ConfigLoader target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);
		
		try {
			target.loadConfig(IMRequiredConf.class);
			fail();
		} catch(RuntimeException expectedEx) {
			System.out.println("Expected exception : "+expectedEx.getMessage());
		}

		baseMap.put("IMRequiredConf.noAnnoInt", "2");
		target = new ConfigLoader(reader.createRCContainerFromMap(baseMap), null);
		IMRequiredConf actual = target.loadConfig(IMRequiredConf.class);
		assertEquals(2, actual.getNoAnnoInt());
	}
}
