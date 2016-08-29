package com.github.hayarobi.simple_config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * only for Enum type. default is true
 * enum타입에서만 의미가 있다. enum 값을 파싱할 때 대소문자를 구분할지 여부.
 *  
 * @author hayarobipark
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CaseSensitive {
	public boolean value() default true;
}
