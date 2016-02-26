package com.github.hayarobi.simple_config.load;


public class SingleStringExtractor<T> implements PropValueExtractor<T> {
	private ValueParser<T> parser;

	public SingleStringExtractor(ValueParser<T> valueParser) {
		super();
		this.parser = valueParser;
	}

	@Override
	public T extractValue(RawConfig node, String propertyName) {
		return parser.parse(node.getPropertyStringValue(propertyName));
	}
}
