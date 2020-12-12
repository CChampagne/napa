package org.cch.napa.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.mapper.ResultSetAccessor;

/**
 * 
 */
public abstract class AbstractResultSetGetter implements ResultSetAccessor{
	protected abstract Object getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException;

	/**
	 * @see ResultSetAccessor#getValueFromResultSet(java.sql.ResultSet, java.lang.String)
	 */
	public Object getValueFromResultSet(ResultSet resultSet,
			String columnName) throws PersistenceException {
		try {
			return getValue(resultSet, columnName);
		} catch (SQLException e) {
			throw new org.cch.napa.exceptions.SQLException("Error while getting value from resultSet", e);
		}
	}
	
}