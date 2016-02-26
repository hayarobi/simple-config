package com.github.hayarobi.simple_config.load;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertiesRawConfingTest {
	public static final String SAMPLE_LIST_PROPERTIES = "sample-list.properties";
	public static final String SAMPLECONF_PROPERTIES = "sampleconf.properties";

	private Map<String, String> sampleMap;
	
	@Before
	public void setUp() throws Exception {
		Properties baseProp = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(SAMPLE_LIST_PROPERTIES);
			baseProp.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload config: "
					+ e.getMessage(), e);
		}
		sampleMap = new HashMap<String, String>((Map)baseProp);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindSubConfig() {
		PropertiesRawConfig target = new PropertiesRawConfig("", sampleMap);
		RawConfig actual = target.findSubConfig("list.and.date");
		assertNotNull(actual);
		
		assertEquals("list.and.date", actual.getName());
		assertNotNull(actual.getPropertyListValue("fruits"));
		assertNotNull(actual.getPropertyMapValue("fruits"));
		assertNotNull(actual.getPropertyListValue("magicNumbers"));
		assertNotNull(actual.getPropertyMapValue("magicNumbers"));
		assertNotNull(actual.getPropertyStringValue("fromTime"));
		
		actual = target.findSubConfig("list.and");
		assertNotNull(actual);
		
		actual = target.findSubConfig("NoOthers");
		assertNotNull(actual);
		// value가 아무것도 없다는 것을 확인할 체크해야하는데, 현재 달리 테스트할 방법이 없다.
	}

	@Test
	public final void testGetPropertyStringValue() {
		PropertiesRawConfig root = new PropertiesRawConfig("", sampleMap);
		RawConfig target= root.findSubConfig("list.and.date");

		try {
			target.getPropertyStringValue("fruits");
			fail();
		} catch(InvalidPropertyTypeException expected) {
			assertEquals("fruits", expected.getPropertyName());
		}
		try {
			target.getPropertyStringValue("magicNumbers");
			fail();
		} catch(InvalidPropertyTypeException expected) {
			assertEquals("magicNumbers", expected.getPropertyName());
		}
		assertNotNull(target.getPropertyStringValue("fromTime"));
		assertEquals("2015-03-02 12:34:56.111+05:00", target.getPropertyStringValue("fromTime"));
	}

	@Test
	public final void testGetPropertyListValue() {
		PropertiesRawConfig root = new PropertiesRawConfig("", sampleMap);
		RawConfig target= root.findSubConfig("list.and.date");

		assertNotNull(target.getPropertyListValue("fruits"));
		assertNotNull(target.getPropertyListValue("magicNumbers"));
		try {
			target.getPropertyListValue("fromTime");
			fail();
		} catch(InvalidPropertyTypeException expected) {
			assertEquals("fromTime", expected.getPropertyName());
		}
		List<String> actual = target.getPropertyListValue("fruits");
		assertEquals(3, actual.size());
		assertTrue(actual.contains("apple"));
		assertTrue(actual.contains("orange"));
		assertTrue(actual.contains("mango"));
		actual = target.getPropertyListValue("magicNumbers");
		assertEquals(2, actual.size());
		assertTrue(actual.contains("15"));
		assertTrue(actual.contains("20"));
	}

	@Test
	public final void testGetPropertyMapValue() {
		PropertiesRawConfig root = new PropertiesRawConfig("", sampleMap);
		RawConfig target= root.findSubConfig("list.and.date");

		assertNotNull(target.getPropertyMapValue("fruits"));
		assertNotNull(target.getPropertyMapValue("magicNumbers"));
		try {
			target.getPropertyMapValue("fromTime");
			fail();
		} catch(InvalidPropertyTypeException expected) {
			assertEquals("fromTime", expected.getPropertyName());
		}
		Map<String, String> actual = target.getPropertyMapValue("fruits");
		assertEquals(3, actual.size());
		assertEquals("apple", actual.get("1"));
		assertEquals("orange", actual.get("2"));
		assertEquals("mango", actual.get("3"));
		actual = target.getPropertyMapValue("magicNumbers");
		assertEquals(2, actual.size());
		assertEquals("15", actual.get("first"));
		assertEquals("20", actual.get("last"));
	}

}
