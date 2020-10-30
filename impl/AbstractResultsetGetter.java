/**
 * by Christophe Champagne
 */
package nanodb.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import nanodb.exceptions.PersistenceException;
import nanodb.mapper.ResultsetAccessor;
import com.sap.ip.me.api.logging.Severities;

/**
 * 
 */
public abstract class AbstractResultsetGetter implements ResultsetAccessor{
	protected abstract Object getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException;

	/**
	 * @see nanodb.mapper.ResultsetAccessor#getValueFromResultSet(java.sql.ResultSet, java.lang.String)
	 */
	public Object getValueFromResultSet(ResultSet resultSet,
			String columnName) throws PersistenceException {
		try {
			return getValue(resultSet, columnName);
		} catch (SQLException e) {
			throw new nanodb.exceptions.SQLException("P006", Severities.ERROR, e);
		}
	}
	
}