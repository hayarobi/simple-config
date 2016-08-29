# simple-config
Annotation based simpl configuration helper libbrary

As of version 0.4, it support properties and yaml file as config source file.

Note: It's too hard work, for me, to make English manual. Translation from README.kr.md to this file is always welcome.

# Simple usage
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
## change config property prefix
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

## Using Collection
Collection is supported. see Korean manual if you can read Korean.

## Using Map
Map is also supported. 


# Supported Data Types
1. all primitive types and wrapping classes of them. 
2. String
3. java.util.Date
5. enum types
4. Class which implements Collection interface, concrete class having default public constructor or well known abstract class or interface such as List, Set or SortedSet.
5. Map, SortedMap or other concrete implementation of Map having default public constructor.
 
