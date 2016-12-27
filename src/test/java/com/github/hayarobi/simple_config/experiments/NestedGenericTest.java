package com.github.hayarobi.simple_config.experiments;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NestedGenericTest {
	private Logger log = LoggerFactory.getLogger(NestedGenericTest.class);
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		Class<NestedGeneric> clazz = NestedGeneric.class;
		
		for (Field field : clazz.getDeclaredFields()) {
			// Mocking framework와의 충돌을 회피하기 위한 조치.
			// TODO: 좀 더 범용적인 형태를 반영할 수 있도록 바꾸어보자.
			if( field.getName().startsWith("$") ) {
				continue;
			}
			Type tempType = field.getGenericType();
			System.out.println("Field : "+field.getName()+" , "+field.getType().getSimpleName());
			if( ! ParameterizedType.class.isInstance(tempType) ) {
				continue;
			}
			ParameterizedType genericType = (ParameterizedType)tempType;
			System.out.println("Field : "+field.getName());
			for (Type type : genericType.getActualTypeArguments()) {
				System.out.println(type.getTypeName());
			}
			
		}
	}

}

class NestedGeneric {
	private Date simpleObject;
	
	private List<Map<String, HashSet<Integer>>> sample;
	
	private PojoGeneric<Integer> pojoGeneric;
	
	public void setSample(List<Map<String, HashSet<Integer>>> sample) {
		this.sample = sample;
	}
	
	public List<Map<String, HashSet<Integer>>> getSample() {
		return sample;
	}

	/**
	 * @return the simpleObject
	 */
	public Date getSimpleObject() {
		return simpleObject;
	}

	/**
	 * @param simpleObject the simpleObject to set
	 */
	public void setSimpleObject(Date simpleObject) {
		this.simpleObject = simpleObject;
	}

	/**
	 * @return the pojoGeneric
	 */
	public PojoGeneric<Integer> getPojoGeneric() {
		return pojoGeneric;
	}

	/**
	 * @param pojoGeneric the pojoGeneric to set
	 */
	public void setPojoGeneric(PojoGeneric<Integer> pojoGeneric) {
		this.pojoGeneric = pojoGeneric;
	}
	
}

class PojoGeneric<T> {
	private T genericMember;
	private String stringMember;
	/**
	 * @return the genericMember
	 */
	public T getGenericMember() {
		return genericMember;
	}
	/**
	 * @param genericMember the genericMember to set
	 */
	public void setGenericMember(T genericMember) {
		this.genericMember = genericMember;
	}
	/**
	 * @return the stringMember
	 */
	public String getStringMember() {
		return stringMember;
	}
	/**
	 * @param stringMember the stringMember to set
	 */
	public void setStringMember(String stringMember) {
		this.stringMember = stringMember;
	}
	
	
}