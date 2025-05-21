package com.github.hayarobi.simple_config.load.mapping;

import com.github.hayarobi.simple_config.load.PropDescription;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.properties.PropertiesReader;
import com.github.hayarobi.simple_config.sample.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MapperManagerTest {
	public static final String SUBCONF_PROPERTIES = "subconf.properties";

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetOrCreateObjectLoader() {
		MapperManager target = new MapperManager();
		
		ObjectMapper<?> actual = target.getOrCreateObjectMapper(String.class);
		
		assertTrue(UnitValueMapper.class.isInstance(actual));
		ObjectMapper<?> actual2 = target.getOrCreateObjectMapper(String.class);
		assertSame(actual, actual2);
		
		ObjectMapper<ComplexConfig> ccActual = target.getOrCreateObjectMapper(ComplexConfig.class);
		assertTrue(PojoObjectMapper.class.isInstance(ccActual));
		System.out.println(ccActual);
	}

	@Test
	public final void testGetOrCreateObjectLoaderByType() throws NoSuchFieldException, SecurityException {
		Class<ComplexConfig> cClass = ComplexConfig.class;
		Field collectionField = cClass.getDeclaredField("loginList");
		
		MapperManager target = new MapperManager();

		ObjectMapper<?> actual = target.getOrCreateObjectMapper(collectionField.getGenericType());
		assertTrue(CollectionObjectMapper.class.isInstance(actual));
		
		Field enumField = cClass.getDeclaredField("enum1");
		ObjectMapper<?> enumactual = target.getOrCreateObjectMapper(enumField.getGenericType());
//		assertTrue(EnumObjectLoader.class.isInstance(actual));

		try {
		ObjectMapper<?> undefined = target.getOrCreateObjectMapper(ArrayList.class);
		}catch(IllegalStateException expectedEx) {}
	}

	@Test
	public void testCyclicConfig() throws IOException {
		RawConfContainer rcc = new PropertiesReader().read(
				getClass().getClassLoader().getResourceAsStream(SUBCONF_PROPERTIES));
		MapperManager target = new MapperManager();

		ObjectMapper<CyclicPropsConfig> mapper = target.getObjectMapper(CyclicPropsConfig.class);
		assertTrue(mapper instanceof PojoObjectMapper);
		
		CyclicPropsConfig dconf1 = mapper.mapToObject(rcc.findConfig("cyclic"));

		assertNotNull(dconf1);
		
		System.out.println(dconf1.toString());
		
		assertNotNull(dconf1.getDirectA());
		assertNotNull(dconf1.getIndirect());
		IndirectCycle indirect = dconf1.getIndirect();
		assertNotNull(indirect.getMid());
		System.out.println(indirect.getMid().getLoop());
	}
	
	@Test
	public void testNestedCollection() throws IOException {
		RawConfContainer rcc = new PropertiesReader().read(
				getClass().getClassLoader().getResourceAsStream(SUBCONF_PROPERTIES));
		MapperManager target = new MapperManager();

		ObjectMapper<MapCollectionMap> mapper = target.getObjectMapper(MapCollectionMap.class);
		assertTrue(mapper instanceof PojoObjectMapper);
		
		MapCollectionMap dconf1 = mapper.mapToObject(rcc.findConfig("mcm"));

		assertNotNull(dconf1);
		assertNotNull(dconf1.getCmap());
		Map<String, List<Map<Double, String>>> member = dconf1.getCmap();
		
		assertEquals(3, member.size());
		List<Map<Double, String>> listA = member.get("a");
		assertNotNull(listA);
		assertEquals(3, listA.size());
		
		System.out.println(member);		
	}
	
	private TestClass dummyField;

	@Test
	public void testAddCustomExtractor() throws NoSuchFieldException, SecurityException {
		PropDescription defaultProp = new PropDescription("[unassigned]", false, false);

		ObjectMapper<TestClass> mockExtractor = mock(ObjectMapper.class);
		
		TestClass valueStub = new TestClass();		
		MapperManager target = new MapperManager();
		
		// 동일타입 중복 추가 방지.
		target.addCustomMapper(TestClass.class, mockExtractor);
		try {
			target.addCustomMapper(TestClass.class, mockExtractor);
			fail();
		} catch (IllegalArgumentException expectedException ) {
		}

		// 커스텀 타입을 제대로 반환하는지 조사.
		Field sampleField = this.getClass().getDeclaredField("dummyField");
		ObjectMapper actual = target.getObjectMapper(sampleField.getType(), defaultProp);
		assertSame(mockExtractor, actual);
	}
	
}
