package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.load.RawConfContainer;
import com.github.hayarobi.simple_config.load.RawConfig;
import com.github.hayarobi.simple_config.load.properties.PropertiesReader;
import com.github.hayarobi.simple_config.load.yaml.MapNodeConfig;
import com.github.hayarobi.simple_config.load.yaml.YamlRCContainer;
import com.github.hayarobi.simple_config.load.yaml.YamlReader;
import com.github.hayarobi.simple_config.sample.ListAndDateConfig;

public class YamlReaderTest {
	public static final String SAMPLE_YAML = "yaml/sampleconf.yaml";

	public static final String SAMPLE_YAML_LIST = "yaml/sample-list.yaml";
	
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

	@Test
	public void testList() throws IOException {

		InputStream propInputStream = YamlTest.class.getClassLoader().getResourceAsStream("sample-list.properties");
		PropertiesReader propTarget = new PropertiesReader();
		RawConfContainer propContainer = propTarget.read(propInputStream);
		ConfigLoader propLoader = new ConfigLoader(propContainer, null);
		
		InputStream inputStream = YamlTest.class.getClassLoader().getResourceAsStream(SAMPLE_YAML_LIST);
		YamlReader yamlTarget = new YamlReader();
		RawConfContainer yamlContainer = yamlTarget.read(inputStream);
		
		ConfigLoader yamlLoader = new ConfigLoader(yamlContainer, null);

		// 두 설정 소스가 같은 결과를 만들어 내야한다.
		ListAndDateConfig yamlC = yamlLoader.loadConfig(ListAndDateConfig.class);
		System.out.println(yamlC.toString());
		assertEquals(yamlC, propLoader.loadConfig(ListAndDateConfig.class));

		
	}
}
