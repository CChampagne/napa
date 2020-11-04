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
	 * Executes a select and returns a list of the
	 * @param query The sql query to execute
	 * @param mapper The object that will map the recordset into the expected entities
	 * @param parameters The parameters of the query
	 * @return
	 * @throws SQLException
	 */
	<T> LazyResultSetIterable<T> lazySelect(String query, RecordMapper<T> mapper,
					   Object... parameters) throws PersistenceException;
	/**
	 * Executes a select and returns a list of entities (data object) of the expected type
	 * @param query The sql query to execute
	 * @param mapper The object that will map the recordset into the expected entities
	 * @param parameters The parameters of the query
	 * @return
	 * @throws SQLException
	 */
	<T> List<T> select(String query, RecordMapper<T> mapper,
			Object... parameters) throws PersistenceException;

	/**
	 * Prepares a statement following the query and the optional parameters.
	 * The sql type of each parameter is deducted from the class of the parameter.
	 * Note that if the type is serializable but isn't assimilable to a primitive type
	 * (a Number, a Boolean, a String) then it is considered as a blob.
	 * @param query The (optionally parametrized) sql query to execute
	 * @param parameters
	 * @return the prepared statement to execute
	 * @throws SQLException Exception wrapping the exception thrown by the underlying JDBC layer.
	 */
	PreparedStatement prepareStatement(String query,
			Object... parameters) throws PersistenceException;

	void executeUpdate(String query, Object... parameters)
			throws PersistenceException;

}
