/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.mapper.ResultsetAccessor;
import com.sap.ip.me.api.logging.Severities;

/**
 * 
 */
public abstract class AbstractResultsetGetter implements ResultsetAccessor{
	protected abstract Object getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException;

	/**
	 * @see com.ibm.next.mam.persistence.mapper.ResultsetAccessor#getValueFromResultSet(java.sql.ResultSet, java.lang.String)
	 */
	public Object getValueFromResultSet(ResultSet resultSet,
			String columnName) throws PersistenceException {
		try {
			return getValue(resultSet, columnName);
		} catch (SQLException e) {
			throw new com.ibm.next.mam.errorframework.exceptions.persistence.SQLException("P006", Severities.ERROR, e);
		}
	}
	
}