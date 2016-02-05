/**
 * 
 */
package com.github.hayarobi.simple_config.load;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * config source를 읽어서 속성값 map을 반환한다.
 * @author sg13park
 *
 */
public interface SourceReader {
	/**
	 * @param inputStream
	 * @return
	 * @throws IOException inputStream에서 읽기를 실패할 경우.
	 */
	public Map<String, String> read(InputStream inputStream) throws IOException;
}
