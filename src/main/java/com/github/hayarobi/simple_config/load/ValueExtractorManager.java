package com.github.hayarobi.simple_config.load;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hayarobi.simple_config.load.ConfigLoader.PropDescription;


/**
 * {@link PropValueExtractor}를 관리하는 객체. 
 * @author sg13park
 *
 */
public class ValueExtractorManager {
	private Logger log = LoggerFactory.getLogger(ValueExtractorManager.class);
	private Map<Class<?>, PropValueExtractor<?>> extractorMap;
	private ValueParserMap singleValueParserMap;
	
	public ValueExtractorManager() {
		super();
		extractorMap = new HashMap<Class<?>, PropValueExtractor<?>>();
		singleValueParserMap = new ValueParserMap();
		initCollectionMappings();
	}

	/**
	 * add custom extractor. not tested yet.
	 * @param type 처리할 필드 타입.
	 * @param extractor 해당 필드 타입에 맞는 값을 추출할 객체 
	 * @throws IllegalArgumentException thrown when other extractor for this type was already added. 
	 */
	public <T> void addCustomExtractor(Class<T> type, PropValueExtractor<T> extractor) throws IllegalArgumentException {
		if( extractorMap.containsKey(type) ) {
			throw new IllegalArgumentException("Type "+type.getName()+" was already added.");
		}
		
		if( log.isDebugEnabled() ) {
			log.debug("value extractor {} for type {} is added.", extractor.toString(), type.getName());
		}
		extractorMap.put(type, extractor);
	}
	
	@SuppressWarnings("unchecked")
	public <T> PropValueExtractor<T> getExtractor(Field field, PropDescription property) throws IllegalArgumentException {
		Class<T> type = (Class<T>)field.getType();
		// 일단은 extractorMap에 있는 것을 최우선으로 사용한다.
		PropValueExtractor<T> extractor = findUnitTypeExtractor(type, property);
		if( extractor != null) {
			return extractor;
		}	
		// 그게 없을 경우 해당 타입이 map이면 map용 extractor를 반환한다.
		
		extractor = tryCreateMapExtractor(field, type, property);
		if( extractor != null) {
			return extractor;
		}	
		// 해당 타입이 collection 
		extractor = tryCreateCollectionExtractor(field, type, property);
		if( extractor != null) {
			return extractor;
		} else {
			throw new IllegalArgumentException("Not supported field type "+type.getName());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Collection>, Class<? extends Collection>> collectionMapping; 
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Map>, Class<? extends Map>> mapMapping; 

	private void initCollectionMappings() {
		collectionMapping = new HashMap<Class<? extends Collection>, Class<? extends Collection>>();
		collectionMapping.put(Collection.class, ArrayList.class);
		collectionMapping.put(List.class, ArrayList.class);
		collectionMapping.put(Set.class, HashSet.class);
		collectionMapping.put(SortedSet.class, TreeSet.class);
		mapMapping = new HashMap<Class<? extends Map>, Class<? extends Map>>();
		mapMapping.put(Map.class, HashMap.class);
		mapMapping.put(SortedMap.class, TreeMap.class);
	}
	
	/**
	 * @param type
	 * @param extractor
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> PropValueExtractor<T> tryCreateCollectionExtractor(Field field, Class<T> type, PropDescription property) {
		if( !Collection.class.isAssignableFrom(type) ) {
			return null;
		}
		if( Modifier.isInterface(type.getModifiers()) || Modifier.isAbstract(type.getModifiers()) ) {
			Class<?> substituteType = collectionMapping.get(type);
			if( null == substituteType ) {
				return null;
			}
			type = (Class<T>)substituteType;
		}
		ParameterizedType genericType = (ParameterizedType)field.getGenericType();
		Class<?> elementType = (Class<?>)genericType.getActualTypeArguments()[0];
		ValueParser<?> elementValueExtractor = findValueParser(elementType, property.caseSensitive);
		if( null == elementValueExtractor ) {
			throw new IllegalArgumentException("Not supported collection element type "+type.getName());
		}
		Collection<?> collectionObject = null;
		try {
			collectionObject = (Collection<?>)type.newInstance();
		} catch(InstantiationException ex) {
			throw new IllegalArgumentException("Can't create collection object. No default construcor. "+type.getName(), ex);
		} catch (IllegalAccessException ex) {
			throw new IllegalArgumentException("Can't create collection object. "+type.getName(), ex);
		}
		PropValueExtractor<T> extractor = new CollectionValueExtractor(collectionObject, elementValueExtractor);

		return extractor;
	}
	

	/**
	 * @param type
	 * @param extractor
	 * @return
	 */
	private <T> PropValueExtractor<T> tryCreateMapExtractor(Field field, Class<T> type, PropDescription property) {
		if( !Map.class.isAssignableFrom(type) ) {
			return null;
		}
		if( Modifier.isInterface(type.getModifiers()) || Modifier.isAbstract(type.getModifiers()) ) {
			Class<?> substituteType = mapMapping.get(type);
			if( null == substituteType ) {
				return null;
			}
			type = (Class<T>)substituteType;
		} else {
		}
		ParameterizedType genericType = (ParameterizedType)field.getGenericType();
		Class<?> keyType = (Class<?>)genericType.getActualTypeArguments()[0];
		Class<?> valueType = (Class<?>)genericType.getActualTypeArguments()[1];
		ValueParser<?> keyParser = findValueParser(keyType, property.caseSensitive);
		if( null == keyParser ) {
			throw new IllegalArgumentException("Not supported map key type. "+keyType.getName());
		}
		ValueParser<?> elementValueExtractor = findValueParser(valueType, property.caseSensitive);
		if( null == elementValueExtractor ) {
			throw new IllegalArgumentException("Not supported map value type. "+valueType.getName());
		}
		Map<?, ?> mapObject = null;
		try {
			mapObject = (Map<?, ?>)type.newInstance();
		} catch(InstantiationException ex) {
			throw new IllegalArgumentException("Can't create collection object. No default constructor. "+type.getName(), ex);
		} catch (IllegalAccessException ex) {
			throw new IllegalArgumentException("Can't create collection object. "+type.getName(), ex);
		}
		PropValueExtractor<T> extractor = new MapValueExtractor(mapObject, keyParser, elementValueExtractor);

		return extractor;

	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> PropValueExtractor<T> findUnitTypeExtractor(Class<T> type, PropDescription property) {
		// 일단은 extractorMap에 있는 것을 최우선으로 사용한다.
		PropValueExtractor<T> extractor = (PropValueExtractor<T>)extractorMap.get(type);
		if( null != extractor ) {
			return extractor;
		}
		
		ValueParser<?> singleValueParser = findValueParser(type, property.caseSensitive);
		if( null == singleValueParser ) {
			return null;
		}		return (SingleStringExtractor<T>)new SingleStringExtractor(singleValueParser);
	}

	/**
	 * @param destType
	 * @return
	 */
	protected <T> ValueParser<?> findValueParser(Class<T> destType, boolean enumCaseSensitive) {
		ValueParser<?> singleValueParser = null;
		if( destType.isPrimitive() ) {
			singleValueParser = singleValueParserMap.getPrimitive(destType);
		} else {
			singleValueParser = singleValueParserMap.get(destType);			
		}
		if( singleValueParser == null && destType.isEnum() ) {
			singleValueParser = new EnumParser(destType, enumCaseSensitive);
		}		
		return singleValueParser;
	}
}
