/**
 * 
 */
package com.github.hayarobi.simple_config.load;

import java.io.IOException;
import java.io.InputStream;

/**
 * config source를 읽어서 속성값 map을 반환한다.
 * @author sg13park
 *
 */
public interface SourceReader {
	/**
	 * 바이트스트림을 읽어서 설정 데이터 트리구조를 반환한다. xml처럼 root는 하나만 있는 구조이다.
	 * @param inputStream 바이트 스트림
	 * @return {@link RawConfig} 인스턴스.
	 * @throws IOException inputStream에서 읽기를 실패할 경우.
	 */
	public RawConfContainer read(InputStream inputStream) throws IOException;
}
