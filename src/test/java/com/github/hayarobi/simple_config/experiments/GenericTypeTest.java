package com.github.hayarobi.simple_config.experiments;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GenericTypeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() throws NoSuchFieldException, SecurityException {
		Class t = SamplePojo.class;
		Field sgField = t.getDeclaredField("sg");
		Field igField = t.getDeclaredField("ig");
		Field dateField = t.getDeclaredField("date");
		ParameterizedType sgP = (ParameterizedType)sgField.getGenericType();
		Type rawType = sgP.getRawType();
//		System.out.println("Owner type: "+sgP.getOwnerType().getTypeName());
		System.out.println("raw type class : "+rawType.getClass()+" : "+rawType.toString());
		System.out.println("arg type: "+Arrays.toString(sgP.getActualTypeArguments()));
		System.out.println(sgField.toString());
		System.out.println(sgField.getGenericType());
		
		System.out.println("String: "+String.class.getGenericInterfaces().getClass());
		
		Class i = InterfaceGeneric.class;
		ParameterizedType igP = (ParameterizedType)igField.getGenericType();
		Type irawType = igP.getRawType();
		System.out.println("raw type class : "+irawType.getClass()+" : "+irawType.toString());
		System.out.println("arg type: "+Arrays.toString(igP.getActualTypeArguments()));
		
		Type actual = dateField.getGenericType();
		System.out.println("Date.getGenericType : "+actual.getTypeName());
	}

}
class SamplePojo {
	SampleGeneric<Integer, List<String>> sg;
	InterfaceGeneric<Double> ig;
	Date date;
	
	/**
	 * @return the sg
	 */
	public SampleGeneric<Integer, List<String>> getSg() {
		return sg;
	}
	/**
	 * @param sg the sg to set
	 */
	public void setSg(SampleGeneric<Integer, List<String>> sg) {
		this.sg = sg;
	}
	/**
	 * @return the ig
	 */
	public InterfaceGeneric<Double> getIg() {
		return ig;
	}
	/**
	 * @param ig the ig to set
	 */
	public void setIg(InterfaceGeneric<Double> ig) {
		this.ig = ig;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}


class SampleGeneric<T,S> {
	T t;
	S s;
	/**
	 * @param t the t to set
	 */
	public void setT(T t) {
		this.t = t;
	}
	/**
	 * @param s the s to set
	 */
	public void setS(S s) {
		this.s = s;
	}
	
}

interface InterfaceGeneric<T> {
	public void test(T arg);
}