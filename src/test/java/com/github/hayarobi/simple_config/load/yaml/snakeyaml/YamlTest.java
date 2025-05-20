package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.validator.ValidateWith;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

public class YamlTest {
	public static final String SAMPLE_YAML = "yaml/sampleconf.yaml";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		
		Yaml yaml = new Yaml();
		InputStream is = YamlTest.class.getClassLoader().getResourceAsStream(SAMPLE_YAML);
		Map<String, Object> map = (Map<String, Object>)yaml.load(is);
		printMap(map, 0);
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			System.out.print(key + ": ");
			printValue(val, 0);
		}
		
		Resolver r = null;
	}

	/**
	 * @param key
	 * @param val
	 */
	private void printValue(Object val, int level) {
		if( Map.class.isInstance(val) ) {
			printMap((Map<String, Object>)val, level+1);
		} else if( Collection.class.isInstance(val) ) {
			printList((Collection<Object>)val, level+1);
		} else {
//			indent(level);
			System.out.print("("+val.getClass().getSimpleName()+")");
			System.out.println(val.toString());
		}
	}

	/**
	 * @param indent
	 */
	private void indent(int indent) {
		for(int i=0;i<indent;i++) {	System.out.print('\t'); }
	}

	private void printMap(Map<String, Object> map, int level) {
		System.out.println("("+map.getClass().getSimpleName()+")");
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			indent(level);
			System.out.print(key + ": ");
			printValue(val, level+1);
		}		
	}

	private void printList(Collection<Object> coll, int level) {
		System.out.println("("+coll.getClass().getSimpleName()+")");
		for (Object val : coll) {
			indent(level);
			System.out.print("- ");
			printValue(val, level+1);
		}		
	}

}

class OnlyStringResolver extends Resolver {
	@Override
	protected void addImplicitResolvers() {
		// 하나도 안 넣는다.
	}
}