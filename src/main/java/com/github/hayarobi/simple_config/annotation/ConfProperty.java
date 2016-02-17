package com.github.hayarobi.simple_config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 설정 객체의 세부 속성을 가리킨다.
 * @author sg13park
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfProperty {
	public String value() default "[unassigned]";
	/**
	 * default is false.
	 * @return
	 */
	public boolean required() default false;
	
		
	/**
	 * only for Enum type. default is true
	 * @return
	 */
	public boolean caseSensitive() default true;
}
