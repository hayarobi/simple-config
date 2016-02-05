package com.github.hayarobi.simple_config.sample;

import com.github.hayarobi.simple_config.annotation.ConfProperty;
import com.github.hayarobi.simple_config.annotation.Config;

@Config("theothers")
public class OtherConfig {
	private String url;

	@ConfProperty(value="result", required=true)
	private String resultPath;

	@ConfProperty("numatc")
	private int numberOfArticles = 300;
	private int maxConn;
	private float era;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the resultPath
	 */
	public String getResultPath() {
		return resultPath;
	}
	/**
	 * @return the numberOfArticles
	 */
	public int getNumberOfArticles() {
		return numberOfArticles;
	}
	/**
	 * @return the maxConn
	 */
	public int getMaxConn() {
		return maxConn;
	}
	/**
	 * @return the era
	 */
	public float getEra() {
		return era;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(era);
		result = prime * result + maxConn;
		result = prime * result + numberOfArticles;
		result = prime * result
				+ ((resultPath == null) ? 0 : resultPath.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OtherConfig other = (OtherConfig) obj;
		if (Float.floatToIntBits(era) != Float.floatToIntBits(other.era)) {
			return false;
		}
		if (maxConn != other.maxConn) {
			return false;
		}
		if (numberOfArticles != other.numberOfArticles) {
			return false;
		}
		if (resultPath == null) {
			if (other.resultPath != null) {
				return false;
			}
		} else if (!resultPath.equals(other.resultPath)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}
	
	
}
