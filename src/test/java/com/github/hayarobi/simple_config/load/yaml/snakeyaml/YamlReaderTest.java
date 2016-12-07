package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.yaml.MapNodeConfig;
import com.github.hayarobi.simple_config.load.yaml.TreeNodeRawConfig;
import com.github.hayarobi.simple_config.load.yaml.YamlRCContainer;
import com.github.hayarobi.simple_config.load.yaml.YamlReader;

public class YamlReaderTest {
	public static final String SAMPLE_YAML = "yaml/sampleconf.yaml";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() throws IOException {
		InputStream inputStream = YamlTest.class.getClassLoader().getResourceAsStream(SAMPLE_YAML);
		YamlReader target = new YamlReader();
		RawConfContainer rootContainer = target.read(inputStream);
		
		assertNotNull(rootContainer);
		assertTrue(rootContainer instanceof YamlRCContainer);
		
		RawConfig actual = null;
		
		actual = rootContainer.findConfig("list.and");
		assertNull(actual);
		
		actual = rootContainer.findConfig("list.and.date");
		assertNotNull(actual);
		assertTrue(actual instanceof MapNodeConfig);
		Map<String, RawConfig> map = actual.getChildrenAsMap();
		assertNotNull(map.get("fruits"));
		assertNotNull(map.get("magicNumbers"));
		assertNotNull(map.get("fromTime"));
		
		
		System.out.println(actual);
		System.out.println(map.get("fruits"));
		System.out.println(map.get("magicNumbers"));
		System.out.println(map.get("fromTime"));
	}

}
