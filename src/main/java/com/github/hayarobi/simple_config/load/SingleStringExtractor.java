package com.github.hayarobi.simple_config.load;

import com.github.hayarobi.simple_config.tree.TreeNode;

public class SingleStringExtractor<T> implements PropValueExtractor<T> {
	private ValueParser<T> parser;

	public SingleStringExtractor(ValueParser<T> valueParser) {
		super();
		this.parser = valueParser;
	}

	@Override
	public T extractValue(TreeNode node) {
		return parser.parse(node.getValueAsString());
	}
}
