package com.github.hayarobi.simple_config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이 주석이 붙은 클래스는 설정을 담은 객체임을 알림.
 * @author Hayarobi Park
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
	/**
	 * 설정 객체 이름을 수동으로 지정. 이름이 없을 경우 해당 설정 클래스의 FQDN으로 찾는다.
	 * @return {@link String} 설정 소스의 이름
	 */
	public String value() default "[unassigned]";
	
	/**
	 * {@link Required} 주석이 안 달린 속성값들의 필수 지정 여부.  
	 *  
	 * @return true 주석이 없는 속성은 필수 속성으로 간주된다.
	 * @return false 주석이 없는 속성은 선택 속성으로 간주된다.  
	 */
	public boolean propRequired() default false;
}
