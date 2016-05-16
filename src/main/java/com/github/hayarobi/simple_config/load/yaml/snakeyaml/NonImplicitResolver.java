package com.github.hayarobi.simple_config.load.yaml.snakeyaml;

import org.yaml.snakeyaml.resolver.Resolver;

/**
 * 스칼라 값은 무조건 스트링으로만 받는다. 변환은 자체 변환기로 한다.
 * @author sg13park
 *
 */
public class NonImplicitResolver extends Resolver {
		@Override
		protected void addImplicitResolvers() {
			// 하나도 안 넣는다.
		}
}
