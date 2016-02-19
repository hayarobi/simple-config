package com.github.hayarobi.simple_config.load;

public interface ValueParser<T> {
	/**
	 * @param str
	 * @return
	 * @throws IllegalArgumentException 해당 타입으로 변환이 불가능한 값이 들어온 경우.
	 */
	public T parse(String str) throws IllegalArgumentException;
}