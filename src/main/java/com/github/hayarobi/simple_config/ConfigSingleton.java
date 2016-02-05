package com.github.hayarobi.simple_config;

public class ConfigSingleton {
	private final static Object lock = new Object();
	private static volatile ConfigService theOneService;
	
	public static ConfigService getInstance() {
		if( null == theOneService) {
			throw new IllegalStateException();
		}
		return theOneService;
	}
	
	public static ConfigService initWithObject(ConfigService singleInstance) {
		if( null == singleInstance ) {
			throw new NullPointerException();
		}
		synchronized (lock) {
			if( null == theOneService ) {
				theOneService = singleInstance;
			} else {
				throw new IllegalStateException();
			}
		}
		return theOneService;
	}
}
