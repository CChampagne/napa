package org.cch.napa.entity.impl;

import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.mapper.QueryValueAccessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 */
public abstract class AbstractQueryValueAccessor implements QueryValueAccessor {
	protected abstract Object getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException;
	protected abstract void setValue(PreparedStatement statement, int index, Object value) throws SQLException, PersistenceException;
	protected final int sqlType;

	public AbstractQueryValueAccessor(int sqlType) {
		this.sqlType = sqlType;
	}

	/**
	 * @see QueryValueAccessor#getValueFromResultSet(java.sql.ResultSet, java.lang.String)
	 */
	public Object getValueFromResultSet(ResultSet resultSet,
			String columnName) throws PersistenceException {
		try {
			return getValue(resultSet, columnName);
		} catch (SQLException e) {
			throw new org.cch.napa.exceptions.SQLException("Error while getting value from resultSet", e);
		}
	}

	public void assignToStatement(PreparedStatement statement, int index, Object value) throws PersistenceException {
		try {
			if (value == null)	{
				statement.setNull(index, sqlType);
			} else {
				setValue(statement, index, value);
			}
		} catch (SQLException e) {
			throw new org.cch.napa.exceptions.SQLException("Error while getting value from resultSet", e);
		}
	}
}