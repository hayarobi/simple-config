# simple-config
설정을 간단히 사용하기 위한 유틸리티 패키지
설정파일의 설정값을 annotation을 이용해 설정 객체에 저장함.
설정파일 포맷은 properties파일이나 yaml파일을 지원한다.

# 간단한 사용법
## simple-config 라이브러리를 추가한다.
Maven을 사용한다면 아래처럼 의존성을 추가한다. snakeyaml 패키지는 설정값 소스로 yaml을 사용할 때만 필요하다. properties파일로 읽는 경우라면 해당 패키지의 의존성을 빼도 된다.
```xml
<dependency>
    <groupId>com.github.hayarobi</groupId>
    <artifactId>simple-config</artifactId>
    <version>0.6</version>
</dependency>
<!-- This is optional only for reading yaml -->
<dependency>
	<groupId>org.yaml</groupId>
	<artifactId>snakeyaml</artifactId>
</dependency>
```

## @Config 어노테이션으로 설정 객체 클래스를 지정한다
어노테이션이 붙은 클래스만 설정값 매핑을 한다.
설정 기본값은 클래스 선언에서 한다.


```java
package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.Config;

@Config
public class DataConfig {
	private String url;
	private String user="nowhereman";
	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
}	
```

## 설정값을 담은 설정파일을 만든다.

패키지.클래스.필드 이름으로 구성한 설정 항목에 값을 지정한다. 여기서 설정한 값은 해당 설정 클래스의 필드에 값이 들어간다.

```properties
com.github.hayarobi.simple_config.sample.DataConfig.url=http://www.score.co.kr
com.github.hayarobi.simple_config.sample.DataConfig.user=tester
```

## ConfigServiceFactory클래스를 이용해 설정 서비스를 생성한다.
설정파일을 클래스패스 안에서 읽을 수도 있고, 경로 지정을 통해 파일을 읽을 수도 있다. 파일을 읽을 경우 craeteServiceFromFile 메서드를 사용하면 된다.
```java
  // ... code snippet
  ConfigService confService = new ConfigServiceFactory().createServiceFromResource("sampleconf.properties");
  // ...
```
## 서비스 객체에서 설정 객체를 가져와서 값을 사용한다.
타겟 클래스는 항상 @Config 어노테이션이 붙은 클래스만 가능하다.
```java
  // ... code snippet
  DataConfig dataConf = confService.getConfig(DataConfig.class);
  setConnInfo(dataConf.getUrl(), dataConf.getUser());
  // ...
```

# 덜 간단한 사용방법들 
## preload
simple-config는 기본적으로 늦은 초기화를 한다. ConfigService를 통해 특정 클래스의 설정 객체를 처음 요청할 때 객체를 생성하고 설정값들을 할당한다. 기본적으로 설정값은 읽기전용을 가정하였기 때문에 해당 객체는 내부에 캐시로 저장하고, 이후 동일한 설정객체를 요청할 경우 그것을 반환한다. 이 기본 동작 대신에 초기화 때 설정 객체를 미리 생성하는 것이 가능하다. spring framework와 비슷하게 미리 검색할 설정객체의 클래스가 선언된 패키지 목록을 인자로 넣는다. 패키지 목록은 쉼표(,)로 구분한 문자열이다. preload 기능을 활성화하려면 reflections 라이브러리를 의존성에 추가해야한다.

pom.xml에 아래 추가
```xml
<!-- only needed when preload enabled -->
<dependency>
	<groupId>org.reflections</groupId>
	<artifactId>reflections</artifactId>
</dependency>
```
```java
  // ... code snippet
  ConfigService confService = new ConfigServiceFactory().createServiceFromResource("sampleconf.properties", true, "com.github.hayarobi.exmaple.conf,com.others.conf");
  // ...
```

## 설정항목 접두어 변경
패키지.클래스 대신 접두어를 별도로 지정할 수 있다.
 
```java
@Config("conf.data")
public class DataConfig {
	private String url;
// ...
```

설정 파일에서는 아래처럼 값을 설정해 가져올 수 있다.
```
conf.data.url=http://www.score.co.kr
conf.data.user=tester
```

## 어노테이션으로 개설 설정항목에 대한 특성 변경하기
기본적으로는 설정 클래스의 모든 필드에 대해 기본설정을 적용한다. 속성에 아래의 어노테이션을 사용하여 기본 설정이 아닌 특성을 부여하는 것도 가능하다.
* Name : 설정항목 이름을 필드명 대신 다른 이름을 사용한다. 
* Required : 이 항목이 true이면 설정파일에서 반드시 값이 들어가 있어야한다. 아닐 경우 초기화 때 예외가 발생한다.
* CaseSensitive : enum 타입 속성만 적용됨. 이넘 항목의 값을 파싱할 때 대소문자를 구분할지 여부를 지정한다. 기본값은 true. 

```java
// ...
@Config("data")
class SampleConfig
	@Name("fruits")
	private List<String> fruitList;

  // enum literal should be case sensitive by default
	@Required
	@CaseSensitive(false) 
	private EnumSample planet;
	
	@Ignored
	private String ignored;
// ...
```

## 특정 필드는 사용하지 않기
@Ignored 어노테이션으로 해당 필드는 사용하지 않는 것으로 처리한다.
주의 : 설정 클래스의 멤버 필드 중에서 지원하지 않는 타입이 있을 경우 반드시 Ignored처리를 해야한다. 그렇지 않으면 초기화 때 예외가 발생할 것이다.

## enum형 사용하기
simple-config는 설정 타입으로 enum을 지원한다.
자바 스펙에서는 enum 항목이 대소문자를 구분한다. 만일 java enum이 대소문자만 다른 여러 항목을 가지고 있는데 이 속성이 false인 경우 설정객체에는 모두 하나의 값으로만 매핑이 될 것이다.

## POJO 멤버 객체
설정 객체의 멤버 변수로 일반적인 자바 클래스 객체를 지정할 수 있다. 컬렉션과 맵의 원소로도 사용이 가능하다. 해당 객체를 생성할 때는 위에 서술한 어노테이션이 반영되서 객체를 생성한다. 다만 @Config 어노테이션의 prefix는 동작하지 않는다. 
```java
// ...
@Config("nested")
class NestedPOJOConfig {
	private SubCategory1 cat1;
	private SubCategory2 cat1;
	
	private List<SubElement> subList; 	
// ...
}
```

## Collection 사용하기
Collection 인터페이스를 구현한 클래스를 지원한다. public 스코프의 기본 생성자(파라메터가 없는 생성자)를 가지고 있는 클래스만 지원한다. List, Set등 몇몇 널리 알려진 추상 클래스는 ArrayList와 HashSet처럼 각각 적절한 콘크리트 클래스로 대체되서 값을 할당한다. 

프로퍼티파일에서 값을 설정하는 방법 속성 항목 뒤에 추가 구분용 속성을 부여한 속성값을 모두 읽어서 그 값들을 컬렉션에 넣는다. 구분 속성에 구두점(.)은 허용하지 않는다. 입력순서는 문자열 비교 순서대로 들어간다. 그래서 프로퍼티의 경우 콜렉션과 맵의 형식이 동일하다. 원소는 설정 클래스가 가질 수 있는 모든 타입이 가능하다. 심지어 List<Map<String, HashSet<Pojo>>> 처럼 컬렉션과 맵이 중첩된 것도 가능하지만 정신건강상 권장하지는 않는다.
예시) people이 List<Person> 타입으로 지정되어 있을 경우 
```
list.sample.people.1.name=John Doe
list.sample.people.1.age=19
list.sample.people.9.name=Mary Sue
list.sample.people.9.age=17
list.sample.people.10.name=James Kook
list.sample.people.10.age=48
```
문자열 형태의 비교로 인해 1,10,9 순서인 John Doe, James Kook, Mary Sue 순으로 저장된다. 

## Map 사용하기
프로퍼티 파일에서 값을 설정하는 방법은 컬렉션과 큰 차이가 나지 않는다. 이름쪽의 마지막 구분속성값이 key가 된다. 위 리스트 예제를 그대로 사용할 경우 map에는 1=apple, 9=orange, 10=grape 세 개의 엔트리가 저장이 될 것이다. 추상 컬렉션과 마찬가지로 Map과 SortedMap은 각각 HashMap과 TreeMap으로 대체되서 객체가 만들어진다. 맵의 키에는 단일 문자열에서 변환 가능한 숫자나 스트링 enum타입만 가능하고, 값은 컬렉션의 원소처럼 설정 클래스가 가질 수 있는 모든 타입이 가능하다. 
```
map.sample.cpu.core=intel
map.sample.cpu.zen=amd
map.sample.cpu.exynos=samsung
map.sample.cpu.snapdragon=qualcomm
```

# 사용 가능한 JAVA 타입
1. primitive type과 그것의 래핑클래스 전부다. 예) int-Integer , float-Float 
2. String
3. java.util.Date 
4. enum형
5. Pojo (generic은 지원하지 않음)
5. Collection 인터페이스를 구현한 클래스. (상세 사용법은 위쪽 참조)
6. Map 클래스
