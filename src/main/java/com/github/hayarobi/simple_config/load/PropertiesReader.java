/**
 * 
 */
package com.github.hayarobi.simple_config.load;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author sg13park
 *
 */
public class PropertiesReader implements SourceReader {

	/* (non-Javadoc)
	 * @see com.github.hayarobi.simple_config.load.SourceReader#read(java.io.InputStream)
	 */
	@Override
	public Map<String, String> read(InputStream inputStream) throws IOException {
		Properties props = new Properties();
		props.load(inputStream);
		HashMap<String, String> map = new HashMap<String, String>((Map)props);
		return map;
	}

}
