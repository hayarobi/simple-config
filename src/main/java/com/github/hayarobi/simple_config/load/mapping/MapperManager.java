package com.github.hayarobi.simple_config.load.mapping;

import static com.github.hayarobi.simple_config.load.ExtractHelper.UNASSIGNED_PLACEHOLDER;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import com.github.hayarobi.simple_config.annotation.CaseSensitive;
import com.github.hayarobi.simple_config.annotation.Ignored;
import com.github.hayarobi.simple_config.annotation.Name;
import com.github.hayarobi.simple_config.annotation.Required;
import com.github.hayarobi.simple_config.load.PropDescription;
import com.github.hayarobi.simple_config.load.inject.FieldInjector;
import com.google.common.base.Preconditions;

/**
 * Pojo 설정 객체 생성을 담당하는 팩토리 클래스. 
 * @author sg13park
 *
 */
public class MapperManager {
	private final static PropDescription DESC_TEMPLATE = 
			new PropDescription(UNASSIGNED_PLACEHOLDER,false, true);

	private Logger log = LoggerFactory.getLogger(MapperManager.class);
	private Map<Class<?>, ObjectMapper<?>> loaderMap;
	private Map<Class<?>, UnitValueMapper<?>> unitValueLoaderMap;
	
	// 몇몇 자주 사용하는 추상 컬렉션과 맵 클래스에 대한 콘크리트 클래스 매핑.  
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Collection>, Class<? extends Collection>> collectionMapping; 
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Map>, Class<? extends Map>> mapMapping; 

	public MapperManager() {
		super();
		initClassConversionMappings();
		loaderMap = new HashMap<Class<?>, ObjectMapper<?>>();
		initUnitValueLoaders();
	}

	@SuppressWarnings("rawtypes")
	private void initClassConversionMappings() {
		collectionMapping = new HashMap<Class<? extends Collection>, Class<? extends Collection>>();
		collectionMapping.put(Collection.class, ArrayList.class);
		collectionMapping.put(List.class, ArrayList.class);
		collectionMapping.put(Set.class, HashSet.class);
		collectionMapping.put(SortedSet.class, TreeSet.class);
		mapMapping = new HashMap<Class<? extends Map>, Class<? extends Map>>();
		mapMapping.put(Map.class, HashMap.class);
		mapMapping.put(SortedMap.class, TreeMap.class);
	}
	
	private void initUnitValueLoaders() {
		unitValueLoaderMap = new HashMap<Class<?>, UnitValueMapper<?>>();
		unitValueLoaderMap.put(String.class, new StringMapper());
		unitValueLoaderMap.put(Date.class, new DateMapper());

		// 숫자형은 기본 타입도 추가를 해야한다.0
		unitValueLoaderMap.put(Boolean.TYPE, new BooleanMapper());
		unitValueLoaderMap.put(Boolean.class, new BooleanMapper());
		unitValueLoaderMap.put(Byte.TYPE, new ByteMapper());
		unitValueLoaderMap.put(Byte.class, new ByteMapper());
		unitValueLoaderMap.put(Short.TYPE, new ShortMapper());
		unitValueLoaderMap.put(Short.class, new ShortMapper());
		unitValueLoaderMap.put(Integer.TYPE, new IntegerMapper());
		unitValueLoaderMap.put(Integer.class, new IntegerMapper());
		unitValueLoaderMap.put(Long.TYPE, new LongMapper());
		unitValueLoaderMap.put(Long.class, new LongMapper());
		unitValueLoaderMap.put(Float.TYPE, new FloatMapper());
		unitValueLoaderMap.put(Float.class, new FloatMapper());
		unitValueLoaderMap.put(Double.TYPE, new DoubleMapper());
		unitValueLoaderMap.put(Double.class, new DoubleMapper());
		
		loaderMap.putAll(unitValueLoaderMap);
	}

	@SuppressWarnings("unchecked")
	synchronized public <T> ObjectMapper<T> getObjectMapper(Class<T> type, PropDescription defaultProp) throws IllegalArgumentException {
		// 일단은 extractorMap에 있는 것을 최우선으로 사용한다.
		ObjectMapper<T> extractor = (ObjectMapper<T>)loaderMap.get(type);
		if( extractor != null) {
			return extractor;
		}
		// collection과 map은 ObjectMapper를 만들지 않는다.
		Preconditions.checkArgument(!Map.class.isAssignableFrom(type));
		Preconditions.checkArgument(!Collection.class.isAssignableFrom(type));
		// 로더가 없으면 새로 만든다.
		// 아직 추상클래스나 인터페이스는 지원하지 않는다.
		if( Modifier.isInterface(type.getModifiers()) || Modifier.isAbstract(type.getModifiers()) ) {
			throw new IllegalArgumentException("Invalid config class "+type.getName()+". Interface or abstract class is not supported yet.");
		}

		return createAndRegisterPojoMapper(type, defaultProp);
		
	}
	
	synchronized public <T> ObjectMapper<T> getObjectMapper(Class<T> type) throws IllegalArgumentException {
		return getObjectMapper(type, DESC_TEMPLATE);
	}
	
	/**
	 * add custom extractor. not tested yet.
	 * @param type 처리할 필드 타입.
	 * @param mapper 해당 필드 타입에 맞는 값을 추출할 객체 
	 * @throws IllegalArgumentException thrown when other extractor for this type was already added. 
	 */
	synchronized public <T> void addCustomMapper(Class<T> type, ObjectMapper<T> mapper) throws IllegalArgumentException {
		if( loaderMap.containsKey(type) ) {
			throw new IllegalArgumentException("Type "+type.getName()+" was already added.");
		}
		
		if( log.isDebugEnabled() ) {
			log.debug("ObjectMapper {} for type {} is added.", mapper.toString(), type.getName());
		}
		loaderMap.put(type, mapper);
	}

	/**
	 * {@link Field#getGenericType()} 메서드가 반환하는 {@link Type} 객체를 인자로 받는다. 
	 * 
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	synchronized <T> ObjectMapper<T> getOrCreateObjectMapper(Type type) throws IllegalArgumentException {
		Class<T> typeClass;
		if( ParameterizedType.class.isInstance(type) ) {
			// generic
			ParameterizedType genericType = (ParameterizedType)type;
			// TODO: DO generic..
			typeClass = (Class<T>)genericType.getRawType();
			if( Collection.class.isAssignableFrom(typeClass) ) {
				return (ObjectMapper<T>)createCollectionMapper(typeClass, genericType.getActualTypeArguments()[0]);
			} else if( Map.class.isAssignableFrom(typeClass) ) {
				Type keyClass = genericType.getActualTypeArguments()[0];
				if( !Class.class.isInstance(keyClass) ) {
					throw new IllegalArgumentException("Map type must have unit type key");
				}
				return (ObjectMapper<T>)createMapMapper(typeClass, (Class)keyClass, genericType.getActualTypeArguments()[1]);
			} else {
				// 일반적인 generic pojo일텐데, 현재 버전에서는 미지원 예정
				throw new IllegalArgumentException("Generic class is not supported yet:"+type.getTypeName());
			}
		} else if(Class.class.isInstance(type) ) {
			typeClass = (Class<T>)type;
			// 일단은 extractorMap에 있는 것을 최우선으로 사용한다.
			ObjectMapper<T> extractor = (ObjectMapper<T>)loaderMap.get(typeClass);
			if( extractor != null) {
				return extractor;
			}
			// 아직 추상클래스나 인터페이스는 지원하지 않는다.
			if( Modifier.isInterface(typeClass.getModifiers()) || Modifier.isAbstract(typeClass.getModifiers()) ) {
				throw new IllegalArgumentException("Invalid config class "+typeClass.getName()+". Interface or abstract class is not supported yet.");
			}
			// Enum type은 enum처리를 한다.
			if( typeClass.isEnum() ) {
				return createAndRegisterEnumMapper(typeClass, false);
			} else if(isMapOrCollectionClass(typeClass) ) {
				throw new IllegalStateException("Collection and Map class can be only field");
			} else {
				// FIXME: 어떤 설정 클래스가 다른 설정 클래스의 멤버로 들어가 있어서 먼저 등록이 될 경우 설정이 안 먹히는 문제가 있다.
				return createAndRegisterPojoMapper(typeClass, DESC_TEMPLATE);
			}
		} else {
			throw new IllegalArgumentException("Not supported class type "+type.getClass().getSimpleName()+type.getTypeName());
		}
	}

	private <T> boolean isMapOrCollectionClass(Class<T> typeClass) {
		return Collection.class.isAssignableFrom(typeClass) 
				|| Map.class.isAssignableFrom(typeClass);
	}

	@SuppressWarnings("unchecked")
	protected <T> UnitValueMapper<T> getUnitValueMapper(Class<T> type) throws IllegalArgumentException {
		UnitValueMapper<T> extractor = (UnitValueMapper<T>)unitValueLoaderMap.get(type);
		if( extractor != null) {
			return extractor;
		} else if( type.isEnum() ){
			return createAndRegisterEnumMapper(type, true);
		} else {
			throw new IllegalStateException("Invalid map key type "+type.getSimpleName());
		}
	}
	
	private EnumObjectMapper createAndRegisterEnumMapper(Class clazz, boolean caseSensitive) {
		// 구조적인 이슈로 enumMapper는 매번 새로 만든다.
		return new EnumObjectMapper(clazz, caseSensitive);
	}

	private <T> ObjectMapper<T> createAndRegisterPojoMapper(Class<T> clazz, PropDescription fieldDefault) {
		PojoObjectMapper<T> customMapper = new PojoObjectMapper<T>(clazz);
		// 먼저 등록을 하는 이유는 무한루프를 방지하기 위함.
		if( log.isDebugEnabled() ) {
			log.debug("ObjectMapper {} for type {} is added.", customMapper.toString(), clazz.getSimpleName());
		}
		loaderMap.put(clazz, customMapper);
		try {
		for (Field field : clazz.getDeclaredFields()) {
			if( null != field.getAnnotation(Ignored.class) ) {
				if( log.isTraceEnabled() ) {
					log.trace("field {}#{} is ignored by @ConfIgnored annotation.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			// Mocking framework와의 충돌을 회피하기 위한 조치.
			// TODO: 좀 더 범용적인 형태를 반영할 수 있도록 바꾸어보자.
			if( field.getName().startsWith("$") || Modifier.isStatic(field.getModifiers()) ) {
				if( log.isTraceEnabled() ) {
					log.trace("static or special purpose field {}#{} is ignored.", clazz.getSimpleName(), field.getName());
				}
				continue;
			}
			// create fields
			FieldInjector injector = null;
			Class<?> fieldClass = (Class<?>)field.getType();
			PropDescription propDescription = getPropDescriptionFor(clazz, field, fieldDefault);
			injector = createFieldInjector(field, propDescription);
//			if( Collection.class.isAssignableFrom(fieldClass) ) {
//				injector = createCollectionInjector(field, fieldClass, propDescription);
//			} else if( Map.class.isAssignableFrom(fieldClass) ) {
//				injector = createMapInjector(field, fieldClass, propDescription);
//			} else if( fieldClass.isEnum() ) {
//				injector = createEnumInjector(field, fieldClass, propDescription);
//			} else {
//				injector = createPojoInjector(field, fieldClass, propDescription);
//			}
			customMapper.addFieldInjector(injector);
		}
		} catch(RuntimeException e) {
			loaderMap.remove(clazz);
			throw e;
		}

		return customMapper;
	}

	/**
	 * @param fieldClass
	 * @param propDescription
	 */
	private <T, E> CollectionObjectMapper<E> createCollectionMapper(Class<T> fieldClass, Type elementType) {
		Class<Collection<E>> collectionClass ;
		// 타입이 interface이거나 abstract class이면 그에 대응하는 구체 클래스로 대체하여 생성한다. 
		if( Modifier.isInterface(fieldClass.getModifiers()) || Modifier.isAbstract(fieldClass.getModifiers()) ) {
			Class<?> substituteType = collectionMapping.get(fieldClass);
			if( null == substituteType ) {
				throw new IllegalArgumentException("Not supported collection type "+fieldClass.getSimpleName());
			}
			collectionClass = (Class<Collection<E>>)substituteType;
		} else {
			collectionClass = (Class<Collection<E>>)fieldClass;
		}
		// TODO: 
		ObjectMapper<E> elementMapper = getOrCreateObjectMapper(elementType);
		return new CollectionObjectMapper<E>(collectionClass, elementMapper);
	}
	
		/**
	 * @param fieldClass
	 * @param propDescription
	 */
	private <T, K, V> MapObjectMapper<K, V> createMapMapper(Class<T> fieldClass, Class<K> keyType
			, Type valueType) {
		Class<Map<K, V>> mapClass ;
		// 타입이 interface이거나 abstract class이면 그에 대응하는 구체 클래스로 대체하여 생성한다. 
		if( Modifier.isInterface(fieldClass.getModifiers()) || Modifier.isAbstract(fieldClass.getModifiers()) ) {
			Class<?> substituteType = mapMapping.get(fieldClass);
			if( null == substituteType ) {
				throw new IllegalArgumentException("Not supported map type "+fieldClass.getSimpleName());
			}
			mapClass = (Class<Map<K, V>>)substituteType;
		} else {
			mapClass = (Class<Map<K, V>>)fieldClass;
		}
		// TODO: 
		UnitValueMapper<K> keyMapper = getUnitValueMapper(keyType);
		ObjectMapper<V> valueMapper = getOrCreateObjectMapper(valueType);
		return new MapObjectMapper<K,V>(mapClass, keyMapper, valueMapper);
	}

	private <T> FieldInjector createPojoInjector(Field field, Class<T> fieldClass,
			PropDescription propDescription) {
		ObjectMapper<T> fieldMapper = getObjectMapper(fieldClass);
		return new FieldInjector<T>(propDescription, field, fieldMapper);
	}

	private <T> FieldInjector createFieldInjector(Field field,
			PropDescription propDescription) {
		ObjectMapper<T> fieldMapper = getOrCreateObjectMapper(field.getGenericType());
		return new FieldInjector<T>(propDescription, field, fieldMapper);
	}

	private <T> PropDescription getPropDescriptionFor(Class<T> clazz, Field field, PropDescription defaultPropDescription) {
		PropDescription propDescription = defaultPropDescription.getCopy();			
		Name annoName = field.getAnnotation(Name.class);
		if (annoName != null ) {
			propDescription.setName(annoName.value());
			if( log.isTraceEnabled() ) {
				log.trace("field {}#{} has @Name annotation, so use prop name {} instead.", clazz.getSimpleName(), field.getName(), annoName.value());
			}
		}
		Required annoReq = field.getAnnotation(Required.class);
		if (annoReq != null ) {
			propDescription.setRequired(annoReq.value());
			if( log.isTraceEnabled() ) {
				log.trace("field {}#{} has @Required annotation {} .", clazz.getSimpleName(), field.getName(), annoReq.value());
			}
		}
		CaseSensitive annoCS = field.getAnnotation(CaseSensitive.class);
		if (annoCS != null ) {
			propDescription.setCaseSensitive(annoCS.value());
			if( log.isTraceEnabled() ) {
				log.trace("field {}#{} has @CaseSensitive annotation {} .", clazz.getSimpleName(), field.getName(), annoCS.value());
			}
		}
		return propDescription;
	}

}
