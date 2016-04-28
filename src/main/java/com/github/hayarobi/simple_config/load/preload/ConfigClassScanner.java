package com.github.hayarobi.simple_config.load.preload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.github.hayarobi.simple_config.annotation.Config;

public class ConfigClassScanner {
	public List<Class<?>> findConfigClassesBelow(String parentPackage) {
		Reflections reflection = new Reflections(new ConfigurationBuilder()
	     .setUrls(ClasspathHelper.forPackage(parentPackage))
	     .setScanners(new SubTypesScanner(), 
	                  new TypeAnnotationsScanner())
	     .filterInputsBy(new FilterBuilder().includePackage(parentPackage)));
				
		Set<Class<?>> classSet = reflection.getTypesAnnotatedWith(Config.class);
		
		return new ArrayList<Class<?>>(classSet);
	}
}
