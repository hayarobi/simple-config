package com.github.hayarobi.simple_config.load;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ObjectInputStream.GetField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.sample.EnumSample;

public class ValueExtractorManagerTest {

	private static ConfProperty defaultProp = getDefaultConfPropertyAnnotation();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testValueExtractorManager() {
		ValueExtractorManager target = new ValueExtractorManager();
	}

	@Test
	public final void testGetExtractor() throws NoSuchFieldException, SecurityException {
		ValueExtractorManager target = new ValueExtractorManager();
		RawConfig mockConfig = mock(RawConfig.class);
		when(mockConfig.getPropertyListValue(anyString())).thenReturn(new ArrayList<String>());
		when(mockConfig.getPropertyMapValue(anyString())).thenReturn(new HashMap<String, String>());
		when(mockConfig.getName()).thenReturn("sample");
		
		Field field = TestClass.class.getDeclaredField("primitive1");
		when(mockConfig.getPropertyStringValue(anyString())).thenReturn("22");
		PropValueExtractor<?> actual = target.getExtractor(field, defaultProp );
		assertEquals(SingleStringExtractor.class, actual.getClass());
		assertEquals(Integer.valueOf(22), actual.extractValue(mockConfig, "primitive1"));
		
		field = TestClass.class.getDeclaredField("string1");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(SingleStringExtractor.class, actual.getClass());
		assertEquals("22", actual.extractValue(mockConfig, "string1"));


		field = TestClass.class.getDeclaredField("enumList");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(CollectionValueExtractor.class, actual.getClass());
		assertEquals(ArrayList.class, actual.extractValue(mockConfig, "enumList").getClass());

		field = TestClass.class.getDeclaredField("enumSet");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(CollectionValueExtractor.class, actual.getClass());
		assertEquals(TreeSet.class, actual.extractValue(mockConfig, "enumSet").getClass());

		field = TestClass.class.getDeclaredField("enumMap");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(MapValueExtractor.class, actual.getClass());
		assertEquals(HashMap.class, actual.extractValue(mockConfig, "enumMap").getClass());

		field = TestClass.class.getDeclaredField("sortedMap");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(MapValueExtractor.class, actual.getClass());
		assertEquals(TreeMap.class, actual.extractValue(mockConfig, "sortedMap").getClass());


	}

	private TestClass dummyField;
	@Test
	public void testAddCustomExtractor() throws NoSuchFieldException, SecurityException {
		PropValueExtractor<TestClass> mockExtractor = mock(PropValueExtractor.class);
		PropValueExtractor<String> mockStringExtractor = mock(PropValueExtractor.class);
		
		TestClass valueStub = new TestClass();		
		ValueExtractorManager target = new ValueExtractorManager();
		
		// 동일타입 중복 추가 방지.
		target.addCustomExtractor(TestClass.class, mockExtractor);
		try {
			target.addCustomExtractor(TestClass.class, mockExtractor);
			fail();
		} catch (IllegalArgumentException expectedException ) {
		}

		target.addCustomExtractor(String.class, mockStringExtractor);

		// 커스텀 타입을 제대로 반환하는지 조사.
		Field sampleField = this.getClass().getDeclaredField("dummyField");
		PropValueExtractor actual = target.getExtractor(sampleField, defaultProp);
		assertSame(mockExtractor, actual);
		// 커스텀 추출기를 등록할 경우 기본타입에 우선하는지 조사 
		sampleField = TestClass.class.getDeclaredField("string1");
		actual = target.getExtractor(sampleField, defaultProp );
		assertSame(mockStringExtractor, actual);
	}
	
	private static ConfProperty getDefaultConfPropertyAnnotation() {
		// NOTE: annotation 선언과 동일한 기본값을 반환해야한다.
		ConfProperty prop = new ConfProperty() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return ConfProperty.class;
			}
			
			@Override
			public String value() {
				return "[unassigned]";
			}
			
			@Override
			public boolean required() {
				return false;
			}
			
			@Override
			public boolean caseSensitive() {
				return true;
			}
		};
		return prop;
	}

}


@Config
class TestClass {
	private int primitive1;
	private String string1;
	private List<EnumSample> enumList;
	private SortedSet<EnumSample> enumSet;
	private Map<EnumSample, String> enumMap;
	private SortedMap<Integer, EnumSample> sortedMap;
	/**
	 * @return the primitive1
	 */
	public int getPrimitive1() {
		return primitive1;
	}
	/**
	 * @return the string1
	 */
	public String getString1() {
		return string1;
	}
	/**
	 * @return the enumList
	 */
	public List<EnumSample> getEnumList() {
		return enumList;
	}
	/**
	 * @return the enumSet
	 */
	public SortedSet<EnumSample> getEnumSet() {
		return enumSet;
	}
	/**
	 * @return the enumMap
	 */
	public Map<EnumSample, String> getEnumMap() {
		return enumMap;
	}
	/**
	 * @return the sortedMap
	 */
	public SortedMap<Integer, EnumSample> getSortedMap() {
		return sortedMap;
	}
}