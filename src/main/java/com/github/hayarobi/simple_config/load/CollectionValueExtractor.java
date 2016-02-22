package com.github.hayarobi.simple_config.load;

import java.util.Collection;
import java.util.List;

import com.github.hayarobi.simple_config.tree.TreeNode;

class CollectionValueExtractor<E> implements PropValueExtractor<Collection<E>> {
	private Collection<E> collectionObject;
	private PropValueExtractor<E> elementValueParser;
	
	public CollectionValueExtractor(Collection<E> collectionObject,
			PropValueExtractor<E> valueParser) {
		super();
		this.collectionObject = collectionObject;
		this.elementValueParser = valueParser;
	}

	@Override
	public Collection<E> extractValue(TreeNode node) {
		List<TreeNode> children = node.getChildren();
		for (TreeNode child : children) {
			collectionObject.add(elementValueParser.extractValue(child));
		}
		return collectionObject;
	}

}
