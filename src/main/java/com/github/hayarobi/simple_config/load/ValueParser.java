package com.github.hayarobi.simple_config.load;

/**
 * 단일 문자열을 객체로 파싱하여 객체를 생성한다. 
 * @author sg13park
 *
 * @param <T>
 */
public interface ValueParser<T> {
	/**
	 * @param str
	 * @return
	 * @throws IllegalArgumentException 해당 타입으로 변환이 불가능한 값이 들어온 경우.
	 */
	public T parse(String str) throws IllegalArgumentException;
}