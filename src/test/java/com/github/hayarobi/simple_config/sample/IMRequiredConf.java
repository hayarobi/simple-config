package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Required;

@Config(value="IMRequiredConf", propRequired=true)
public class IMRequiredConf {

	private int noAnnoInt;
	@Required(true)
	private int trueAnnoInt;
	@Required(false)
	private int falseAnnoInt;
	/**
	 * @return the noAnnoInt
	 */
	public int getNoAnnoInt() {
		return noAnnoInt;
	}
	/**
	 * @return the trueAnnoInt
	 */
	public int getTrueAnnoInt() {
		return trueAnnoInt;
	}
	/**
	 * @return the falseAnnoInt
	 */
	public int getFalseAnnoInt() {
		return falseAnnoInt;
	}
	
	
}
