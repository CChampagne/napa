package org.cch.napa;

import org.cch.napa.entity.LazyResultSetIterable;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.exceptions.SQLException;
import org.cch.napa.mapper.RecordMapper;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * This DAO is the most basic and the closest to JDBC layer.
 * It provides basic JDBC functionalities with (a bit) less boilerplate.
 *
 * @author Christophe Champagne
 *
 */
public interface JdbcDao {

	/**
	 * Executes a select and returns an {@link LazyResultSetIterable} that will populate the data object on the fly.
	 * It thus preferable if the query returns an important number of records.
	 * Note that the iterable wraps a PreparedStatement that needs to be closed so it also implements Closeable.
	 * It will be automatically closed if all the the items have been iterated.
	 * @param query The sql query to execute (optionally with indexed parameters)
	 * @param mapper The object that will map the recordset into the expected entities
	 * @param parameters The parameters of the query
	 * @return The LazyResultSetIterable
	 * @throws PersistenceException a general persistence exception of a {@link SQLException} itself cause by a native {@link java.sql.SQLException}
	 */
	<T> LazyResultSetIterable<T> lazilySelect(String query, RecordMapper<T> mapper,
											  Object... parameters) throws PersistenceException;
	/**
	 * Executes a select and returns a list of data object of the expected type
	 * @param query The sql query to execute (optionally with indexed parameters)
	 * @param mapper The object that will map the recordset into the expected entities
	 * @param parameters The parameters of the query
	 * @return a list of data object corresponding to the given generic type T
	 * @throws PersistenceException a general persistence exception of a {@link SQLException} itself cause by a native {@link java.sql.SQLException}
	 */
	<T> List<T> select(String query, RecordMapper<T> mapper,
					   Object... parameters) throws PersistenceException;

	/**
	 * Executes a select and returns a list of data object of the expected type.
	 * @param statement The already prepared statement
	 * @param mapper The object that will map the recordset into the expected entities
	 * @return a list of data object corresponding to the given generic type T
	 * @throws PersistenceException a general persistence exception of a {@link SQLException} itself cause by a native {@link java.sql.SQLException}
	 */
	<T> List<T> select(PreparedStatement statement, RecordMapper<T> mapper) throws PersistenceException;

	/**
	 * Prepares a statement following the query and the optional parameters.
	 * The sql type of each parameter is deducted from the class of the parameter.
	 * Note that if the type is serializable but isn't assimilable to a primitive type
	 * (a Number, a Boolean, a String) then it is considered as a blob.
	 * @param query The (optionally parametrized) sql query to execute
	 * @param parameters the parameter(s) of the query
	 * @return the prepared statement to execute
	 * @throws SQLException Exception wrapping the exception thrown by the underlying JDBC layer.
	 */
	PreparedStatement prepareStatement(String query,
			Object... parameters) throws PersistenceException;

	/**
	 * Executes an SQL statement
	 * @param query The sql query to execute (optionally with indexed parameters)
	 * @param parameters The parameters of the query
	 * @throws PersistenceException
	 */
	void executeUpdate(String query, Object... parameters)
			throws PersistenceException;

	/**
	 * Executes an SQL statement
	 * @param statement The already prepared statement
	 * @throws PersistenceException
	 */
	void executeUpdate(PreparedStatement statement)
			throws PersistenceException;

}
