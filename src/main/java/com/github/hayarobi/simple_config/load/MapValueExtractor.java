package com.github.hayarobi.simple_config.load;

import java.util.List;
import java.util.Map;

import com.github.hayarobi.simple_config.tree.TreeNode;

class MapValueExtractor<K, V> implements PropValueExtractor<Map<K, V>> {
	private Map<K, V> mapObject;
	private ValueParser<K> keyParser;
	private PropValueExtractor<V> elementValueExtractor;
	
	public MapValueExtractor(Map<K, V> mapObject, ValueParser<K> keyParser, 
			PropValueExtractor<V> valueExtractor) {
		super();
		this.mapObject = mapObject;
		this.keyParser = keyParser;
		this.elementValueExtractor = valueExtractor;
	}

	@Override
	public Map<K, V> extractValue(TreeNode node) {
		List<TreeNode> children = node.getChildren();
		for (TreeNode child : children) {
			mapObject.put(keyParser.parse(child.getName()),
					elementValueExtractor.extractValue(child));
		}
		return mapObject;
	}

}
