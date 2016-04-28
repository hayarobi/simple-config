package com.github.hayarobi.simple_config.load.preload;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ConfigClassScannerTest {

	private static final String SAMPLE_PACK_SUB = "com.github.hayarobi.simple_config.sample";
	private static final String SAMPLE_PACKAGE = "com.github.hayarobi";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindClasses() {
		ConfigClassScanner target = new ConfigClassScanner();
		
		List<Class<?>> actual = target.findConfigClassesBelow(SAMPLE_PACKAGE);
		assertTrue( 0 < actual.size() );
		for (Class<?> clazz : actual) {
			assertTrue( clazz.getCanonicalName().startsWith(SAMPLE_PACKAGE));
		}
		
		
		ArrayList<Class<?>> expected = new ArrayList<Class<?>>();
		for (Class<?> clazz : actual) {
			if( clazz.getCanonicalName().startsWith(SAMPLE_PACK_SUB) ) {
				expected.add(clazz);
			}
		}
		
		actual = target.findConfigClassesBelow(SAMPLE_PACK_SUB);
		
		assertEquals(expected.size(), actual.size());
		for (Class<?> clazz : expected) {
			assertTrue(actual.contains(clazz));
		}
		
		actual = target.findConfigClassesBelow("com.github.hayarobi.nothing");
		assertEquals(0, actual.size());
		

	}

}
