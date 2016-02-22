package com.github.hayarobi.simple_config.load;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.github.hayarobi.simple_config.tree.TreeNode;

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
		TreeNode mockNode = mock(TreeNode.class);
		when(mockNode.getChildren()).thenReturn(new ArrayList<TreeNode>());
		when(mockNode.getName()).thenReturn("sample");
		
		Field field = TestClass.class.getField("primitive1");
		when(mockNode.getValueAsString()).thenReturn("22");
		PropValueExtractor<?> actual = target.getExtractor(field, defaultProp );
		assertEquals(SingleStringExtractor.class, actual.getClass());
		assertEquals(Integer.valueOf(22), actual.extractValue(mockNode));
		
		field = TestClass.class.getField("string1");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(SingleStringExtractor.class, actual.getClass());
		assertEquals("22", actual.extractValue(mockNode));


		field = TestClass.class.getField("enumList");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(CollectionValueExtractor.class, actual.getClass());
		assertEquals(ArrayList.class, actual.extractValue(mockNode).getClass());

		field = TestClass.class.getField("enumSet");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(CollectionValueExtractor.class, actual.getClass());
		assertEquals(TreeSet.class, actual.extractValue(mockNode).getClass());

		field = TestClass.class.getField("enumMap");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(MapValueExtractor.class, actual.getClass());
		assertEquals(HashMap.class, actual.extractValue(mockNode).getClass());

		field = TestClass.class.getField("sortedMap");
		actual = target.getExtractor(field, defaultProp );
		assertEquals(MapValueExtractor.class, actual.getClass());
		assertEquals(TreeMap.class, actual.extractValue(mockNode).getClass());


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
	public int primitive1;
	public String string1;
	public List<EnumSample> enumList;
	public SortedSet<EnumSample> enumSet;
	public Map<EnumSample, String> enumMap;
	public SortedMap<Integer, EnumSample> sortedMap;
}