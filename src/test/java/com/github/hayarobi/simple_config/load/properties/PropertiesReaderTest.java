package com.github.hayarobi.simple_config.load.properties;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.load.InvalidPropertyTypeException;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;

public class PropertiesReaderTest {
	public static final String SAMPLE_LIST_PROPERTIES = "sample-list.properties";
	public static final String SAMPLECONF_PROPERTIES = "sampleconf.properties";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSplitPath() {
		String[] actual = PropertiesReader.splitPath("a.b.c");
		assertEquals(3, actual.length);
		assertEquals("a", actual[0]);
		assertEquals("b", actual[1]);
		assertEquals("c", actual[2]);

		try {
			PropertiesReader.splitPath("tailing.is.missing.");
		} catch(IllegalArgumentException expectedEx) {}

		try {
			PropertiesReader.splitPath(".head.is.missing.");
		} catch(IllegalArgumentException expectedEx) {}

		try {
			PropertiesReader.splitPath("Invalid.de^s.char%cter");
		} catch(IllegalArgumentException expectedEx) {}

	}
	
	@Test
	public void testRead() throws IOException {
		PropertiesReader target = new PropertiesReader();
		RawConfContainer container = target.read(getClass().getClassLoader().getResourceAsStream(SAMPLECONF_PROPERTIES));
						
		assertNotNull(container.findConfig("com"));
		assertNotNull(container.findConfig("theothers"));
		assertNotNull(container.findConfig("enums"));
		assertNotNull(container.findConfig("IMRequiredConf"));
		
		RawConfig conf = container.findConfig("theothers");
		// 모드 단일 설정은 없기 때문에 string value가져오기는 예외가 떠야한다.
		assertTrue(PropRawConfig.class.isInstance(conf));
		try {
			conf.getStringValue();
			fail();
		} catch(InvalidPropertyTypeException expectedEx) {}
	
		PropRawConfig child1 = (PropRawConfig)conf;

		for (Entry<String,RawConfig> ent: child1.getChildrenAsMap().entrySet()) {
			RawConfig grandChild = ent.getValue();
			System.out.println(ent.getKey()+": "+grandChild.getStringValue());
			// 프로퍼티 파일은 구조상 예외가 발생하지 않는다. 안 그러면 null과 empty list를 구분할 방법이 없다.
//			try {
//				grandChild.getChildrenAsList();
//				fail();
//			} catch(InvalidPropertyTypeException expectedEx) {}
//			try {
//				grandChild.getChildrenAsMap();
//				fail();
//			} catch(InvalidPropertyTypeException expectedEx) {}			
		}
	}
}
