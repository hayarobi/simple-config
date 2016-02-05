# simple-config
Annotation based simpl configuration helper libbrary

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
	@ConfProperty(value="fruits", separator=";")
	private ArrayList<String> fruitList;

  // enum literal should be case sensitive by default
	@ConfProperty(required=true, caseSensitive=false) 
	private EnumSample planet;
// ...
```

# Supported Data Types
1. all primitive types and wrapping classes of them. 
2. String
3. java.util.Date
4. concrete class which implements Collection interface, and having default public constructor.
5. enum types
