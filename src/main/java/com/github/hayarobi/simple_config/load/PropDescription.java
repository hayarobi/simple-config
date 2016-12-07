package com.github.hayarobi.simple_config.load;

public class PropDescription implements Cloneable {
	String name;
	boolean required;
	boolean caseSensitive;

	public PropDescription(String name, boolean required,
			boolean caseSensitive) {
		super();
		this.name = name;
		this.required = required;
		this.caseSensitive = caseSensitive;
	}
	
	public PropDescription getCopy() {
		try {
			return (PropDescription)this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Something goes wrong.");
		}
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @return the caseSensitive
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropDescription [name=");
		builder.append(name);
		builder.append(", required=");
		builder.append(required);
		builder.append(", caseSensitive=");
		builder.append(caseSensitive);
		builder.append("]");
		return builder.toString();
	}
	
}