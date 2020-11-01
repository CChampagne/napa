package org.cch.nanodb;

/**
 * class wrapping null value while keeping information about the target type
 * @author Christophe Champagne
 *
 */
public final class SQLNull {

	private int sqlType;
	/**
	 * 
	 * @param sqlType SQL type as defined in 
	 */
	public SQLNull(int sqlType) {
		this.sqlType = sqlType;
	}

	/**
	 * @return the sqlType
	 */
	public int getSqlType() {
		return sqlType;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NULL value for JDBC sql type " + sqlType;
	}
	


}
