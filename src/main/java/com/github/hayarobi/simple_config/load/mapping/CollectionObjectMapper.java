package com.github.hayarobi.simple_config.load.mapping;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ExtractHelper;
import com.github.hayarobi.simple_config.load.RawConfig;

/**
 * 단일 문자열을 바로 객체로 추출하는 클래스.
 * 
 * @author sg13park
 *
 */
public class CollectionObjectMapper<E> implements ObjectMapper<Collection<E>> {
	private static Logger log = LoggerFactory
			.getLogger(CollectionObjectMapper.class);
	private Class<Collection<E>> collectionClass;
	private ObjectMapper<E> elementLoader;

	public CollectionObjectMapper(Class<Collection<E>> collClass,
			ObjectMapper<E> elementLoader) {
		collectionClass = collClass;
		this.elementLoader = elementLoader;
	}

	@Override
	public Collection<E> mapToObject(RawConfig node) {
		Collection<E> collectionObject = ExtractHelper
				.createObject(collectionClass);
		// element type에서 클래스 추출
		List<RawConfig> children = node.getChildrenAsList();
		for (RawConfig child : children) {
			collectionObject.add(elementLoader.mapToObject(child));
		}
		return collectionObject;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(collectionClass.getSimpleName());
		builder.append("Loader(Collection<");
		builder.append(elementLoader);
		builder.append(">");
		return builder.toString();
	}
}
