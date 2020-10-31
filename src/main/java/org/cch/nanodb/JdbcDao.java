/**
 * 
 */
package org.cch.nanodb;

import java.sql.PreparedStatement;
import java.util.List;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.exceptions.SQLException;
import org.cch.nanodb.mapper.RecordMapper;

/**
 * @author Christophe Champagne
 *
 */
public interface JdbcDao {

	/**
	 * 
	 * @param query
	 * @param mapper
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public abstract <T> List<T> select(String query, RecordMapper<T> mapper,
			Object... parameters) throws PersistenceException;

	/**
	 * prepares a statement following the query and the optional parameters
	 * The sql type of each parameter is deducted from the class of the parameter
	 * Note that if the type is serializable 
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public abstract PreparedStatement prepareStatement(String query,
			Object... parameters) throws PersistenceException;

	public abstract void executeUpdate(String query, Object... parameters)
			throws SQLException, PersistenceException;

}
