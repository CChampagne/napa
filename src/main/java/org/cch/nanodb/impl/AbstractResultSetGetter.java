package org.cch.nanodb.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.mapper.ResultSetAccessor;

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
			throw new org.cch.nanodb.exceptions.SQLException("Error while getting value from resultSet", e);
		}
	}
	
}