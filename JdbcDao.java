/**
 * 
 */
package com.ibm.next.mam.persistence;

import java.sql.PreparedStatement;
import java.util.List;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.errorframework.exceptions.persistence.SQLException;
import com.ibm.next.mam.persistence.mapper.RecordMapper;

/**
 * @author GII561
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
			Object... parameters) throws SQLException, PersistenceException;

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
			Object... parameters) throws SQLException, PersistenceException;

	public abstract void executeUpdate(String query, Object... parameters)
			throws SQLException, PersistenceException;

}
