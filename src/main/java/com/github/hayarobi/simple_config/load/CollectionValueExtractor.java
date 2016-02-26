package com.github.hayarobi.simple_config.load;

import java.util.Collection;
import java.util.List;

class CollectionValueExtractor<E> implements PropValueExtractor<Collection<E>> {
	private Collection<E> collectionObject;
	private ValueParser<E> elementValueParser;
	
	public CollectionValueExtractor(Collection<E> collectionObject,
			ValueParser<E> valueParser) {
		super();
		this.collectionObject = collectionObject;
		this.elementValueParser = valueParser;
	}

	@Override
	public Collection<E> extractValue(RawConfig node, String propertyName) {
		List<String> children = node.getPropertyListValue(propertyName);
		for (String child : children) {
			collectionObject.add(elementValueParser.parse(child));
		}
		return collectionObject;
	}

}
