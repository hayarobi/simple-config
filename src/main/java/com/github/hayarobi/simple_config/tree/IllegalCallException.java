package com.github.hayarobi.simple_config.tree;

public class IllegalCallException extends RuntimeException {
	public IllegalCallException(String nodename, String callName) {
		super("Illegal call to node "+nodename+": "+callName);
	}
}
