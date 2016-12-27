# simple-config
Annotation based simpl configuration helper libbrary

It support properties and yaml file as config source file.

# Simple usage
## add dependency on simple-config 
When using maven, edit pom.xml like this: 
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

## Declare config Class with @Config annotation
```java
package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.Config;

@Config
public class DataConfig {
	private String url;
	private String user;
	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
}	
```
## create config file
> sampleconf.properties
```properties
com.github.hayarobi.simple_config.sample.DataConfig.url=http://www.score.co.kr
com.github.hayarobi.simple_config.sample.DataConfig.user=tester
```
## Initialize config service with config file
```java
  // ... code snippet
  ConfigService confService = new ConfigServiceFactory().craeteServiceFromResource("sampleconf.properties");
  // ...
```
## get config and use it.
```java
  // ... code snippet
  DataConfig dataConf = confService.getConfig(DataConfig.class);
  setConnInfo(dataConf.getUrl(), dataConf.getUser());
  // ...
```

# Less simple usage
## preload
 You can preload config objects. You can pass preloaded package names with comma separated string. You should add dependency on reflections package.
 pom.xml
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

## change config property prefix
You can change and shorten config name instead of FQDN of class.
```java
@Config("conf.data")
public class DataConfig {
	private String url;
// ...
```
in properties file
```
conf.data.url=http://www.score.co.kr
conf.data.user=tester
```

## change characteristics of properties with annotation
Changing behaviors of config fields is possible by using annotation: @Name, @Required, @CaseSensitive and @Ignored
```java
// ...
@Config
class SampleConfig
	@Name("fruits")
	private ArrayList<String> fruitList;

  // enum literal should be case sensitive by default
	@Required
	@CaseSensitive(false) 
	private EnumSample planet;
// ...
```

## Using enum type
You can set enum types just like string. and can change case sensitivity of enum fields by using @CaseSensitive annotation. (sensitive is default)

## Using Pojo as config field
It's possbie, but generic class is not supported.
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

## Using Collection
Collection is supported. 
It can be the concrete class that has default public constructor, or well known abstract class or interface such as List, Set or SortedSet. These abstract classes will be set to concrete classes such as ArrayList, HashSet, TreeSet. The class of element can be any class that simple-config supports. It's possible to be like 'List<Map<String, HashSet<Pojo>>>', but not recommended.

yaml
```yaml
list.sample:
    people:
        - name=John Doe
          age=19
        - name=James Kook
          age=48
        - name=Mary Sue
          age=17
```
properties
```
list.sample.people.1.name=John Doe
list.sample.people.1.age=19
list.sample.people.9.name=Mary Sue
list.sample.people.9.age=17
list.sample.people.10.name=James Kook
list.sample.people.10.age=48
```


## Using Map
Map is also supported. Its characteristic is similar to that of collections. It also possible for well known abstract Map classes.

```
map.sample.cpu.core=intel
map.sample.cpu.zen=amd
map.sample.cpu.exynos=samsung
map.sample.cpu.snapdragon=qualcomm
```


# Supported Data Types
1. all primitive types and wrapping classes of them. 
2. String
3. java.util.Date
4. enum types
5. Pojo (not containing generic)
6. Class which implements Collection interface
7. Map, SortedMap or other concrete implementation of Map having default public constructor.
 
