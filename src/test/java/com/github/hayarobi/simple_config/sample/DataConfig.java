package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.Config;

/**
 * Overridden methods, such as {@link #equals(Object)} and {@link #hashCode()}, are for unit testing, not required in actual use. 
 * @author sg13park
 *
 */
@Config
public class DataConfig {
	private String url;
	private String user;
	private String pass;
	private int maxConn;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * @return the maxConn
	 */
	public int getMaxConn() {
		return maxConn;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxConn;
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataConfig other = (DataConfig) obj;
		if (maxConn != other.maxConn)
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
}
