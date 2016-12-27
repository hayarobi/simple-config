package com.github.hayarobi.simple_config.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import com.github.hayarobi.simple_config.annotation.Config;
import com.github.hayarobi.simple_config.annotation.Ignored;
import com.github.hayarobi.simple_config.annotation.Name;

@Config("list.and.date")
public class ListAndDateConfig {
	@Name("fruits")
	private ArrayList<String> fruitList;
	
	private ArrayList<String> toBeNull;

	private HashSet<Integer> magicNumbers;
	
	private Date fromTime;

	@Ignored
	private List<String> ignoredField;

	@Name("complex")
	private List<OtherConfig> pojoList;
	
	private List<Fruit> nullList;
	
	private List<NodeInfo> emptyList;
	
	/**
	 * @return the fruitList
	 */
	public List<String> getFruitList() {
		return fruitList;
	}

	/**
	 * @return the magicNumbers
	 */
	public HashSet<Integer> getMagicNumbers() {
		return magicNumbers;
	}

	/**
	 * @return the toBeNull
	 */
	public ArrayList<String> getToBeNull() {
		return toBeNull;
	}

	/**
	 * @return the fromTime
	 */
	public Date getFromTime() {
		return fromTime;
	}

	public List<String> getIgnoredField() {
		return ignoredField;
	}

	/**
	 * @return the pojoList
	 */
	public List<OtherConfig> getPojoList() {
		return pojoList;
	}

	/**
	 * @return the nullList
	 */
	public List<Fruit> getNullList() {
		return nullList;
	}

	/**
	 * @return the emptyList
	 */
	public List<NodeInfo> getEmptyList() {
		return emptyList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((emptyList == null) ? 0 : emptyList.hashCode());
		result = prime * result
				+ ((fromTime == null) ? 0 : fromTime.hashCode());
		result = prime * result
				+ ((fruitList == null) ? 0 : fruitList.hashCode());
		result = prime * result
				+ ((ignoredField == null) ? 0 : ignoredField.hashCode());
		result = prime * result
				+ ((magicNumbers == null) ? 0 : magicNumbers.hashCode());
		result = prime * result
				+ ((nullList == null) ? 0 : nullList.hashCode());
		result = prime * result
				+ ((pojoList == null) ? 0 : pojoList.hashCode());
		result = prime * result
				+ ((toBeNull == null) ? 0 : toBeNull.hashCode());
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
		ListAndDateConfig other = (ListAndDateConfig) obj;
		if (emptyList == null) {
			if (other.emptyList != null) {
				return false;
			}
		} else if (!emptyList.equals(other.emptyList)) {
			return false;
		}
		if (fromTime == null) {
			if (other.fromTime != null) {
				return false;
			}
		} else if (!fromTime.equals(other.fromTime)) {
			return false;
		}
		if (fruitList == null) {
			if (other.fruitList != null) {
				return false;
			}
		} else if (!fruitList.equals(other.fruitList)) {
			return false;
		}
		if (ignoredField == null) {
			if (other.ignoredField != null) {
				return false;
			}
		} else if (!ignoredField.equals(other.ignoredField)) {
			return false;
		}
		if (magicNumbers == null) {
			if (other.magicNumbers != null) {
				return false;
			}
		} else if (!magicNumbers.equals(other.magicNumbers)) {
			return false;
		}
		if (nullList == null) {
			if (other.nullList != null) {
				return false;
			}
		} else if (!nullList.equals(other.nullList)) {
			return false;
		}
		if (pojoList == null) {
			if (other.pojoList != null) {
				return false;
			}
		} else if (!pojoList.equals(other.pojoList)) {
			return false;
		}
		if (toBeNull == null) {
			if (other.toBeNull != null) {
				return false;
			}
		} else if (!toBeNull.equals(other.toBeNull)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListAndDateConfig [fruitList=");
		builder.append(fruitList);
		builder.append(", toBeNull=");
		builder.append(toBeNull);
		builder.append(", magicNumbers=");
		builder.append(magicNumbers);
		builder.append(", fromTime=");
		builder.append(fromTime);
		builder.append(", ignoredField=");
		builder.append(ignoredField);
		builder.append(", pojoList=");
		builder.append(pojoList);
		builder.append(", nullList=");
		builder.append(nullList);
		builder.append(", emptyList=");
		builder.append(emptyList);
		builder.append("]");
		return builder.toString();
	}
	
	
}
