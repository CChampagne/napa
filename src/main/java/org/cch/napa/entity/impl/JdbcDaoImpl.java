package org.cch.napa.entity.impl;

import org.cch.napa.*;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.LazyResultSetIterable;
import org.cch.napa.entity.SQLTypeMapper;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.exceptions.SQLException;
import org.cch.napa.mapper.RecordMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcDaoImpl implements JdbcDao {
	private final static Logger log = Logger.getLogger(JdbcDaoImpl.class.toString());
	private final ConnectionProvider connectionProvider;
	private final SQLTypeMapper sqlTypeMapper;
	
	public JdbcDaoImpl(ConnectionProvider connectionProvider, EntityDaoFactory factory) {
		this.connectionProvider = connectionProvider;
		sqlTypeMapper = factory.getSqlTypeMapper();
	}	
	public JdbcDaoImpl(EntityDaoFactory factory) {
		this(factory.getDefaultConnectionProvider(), factory);
	}

	/**
	 *
	 * @see org.cch.napa.JdbcDao#lazilySelect(String, RecordMapper, Object...)
	 */
	public <T> LazyResultSetIterable<T> lazilySelect(String query, RecordMapper<T> mapper, Object... parameters) throws PersistenceException {
		PreparedStatement statement = prepareStatement(query, parameters);
		return new LazyResultSetIterableImpl<T>(statement,mapper);
	}

	/**
	 * @see org.cch.napa.JdbcDao#select(java.lang.String, org.cch.napa.mapper.RecordMapper, java.lang.Object[])
	 */
	public <T> List<T> select(String query, RecordMapper<T> mapper,Object...parameters) throws PersistenceException {
		try{
			PreparedStatement statement = prepareStatement(query, parameters);
			return select(statement, mapper);
		} catch(SQLException e){
			throw new SQLException("Failed executing query\n"+ queryAndParametersToText(query, parameters), e.getCause());
		}
	}
	public <T> List<T> select(PreparedStatement statement, RecordMapper<T> mapper) throws PersistenceException {
		List<T> list = new ArrayList<T>();
		try{
			ResultSet resultSet =  statement.executeQuery();
			while(resultSet.next()){
				T item = mapper.map(resultSet);
				list.add(item);
			}
		}catch(java.sql.SQLException e){
			throw new SQLException("Failed executing query\n", e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					log.log(Level.WARNING, "Could not close prepared statement", e);
				}
			}
		}
		return list;
	}
	/**
	 * @see org.cch.napa.JdbcDao#prepareStatement(java.lang.String, java.lang.Object[])
	 */
	public PreparedStatement prepareStatement(String query, Object...parameters) throws PersistenceException{
		PreparedStatement statement = null;
		try {
			Connection connection = connectionProvider.getConnection();
			if(connection != null){
				statement = connection.prepareStatement(query);
				int index = 0;
				for(Object o: parameters){
					index++;
					sqlTypeMapper.setParameter(statement, index, o);
				}
			}
			if(log.isLoggable(Level.FINE)) {
				log.fine("Prepared sql statement :\n" + queryAndParametersToText(query,parameters));
			}
		} catch (java.sql.SQLException e) {
			throw new SQLException("Failed to prepare statement of\n"+ queryAndParametersToText(query, parameters),e);
		}
		return statement;
	}

	/**
	 * @see org.cch.napa.JdbcDao#executeUpdate(java.lang.String, java.lang.Object[])
	 */
	public void executeUpdate(String query, Object...parameters) throws PersistenceException {
		try{
			PreparedStatement statement = prepareStatement(query, parameters);
			executeUpdate(statement);
		} catch(SQLException e){
			throw new SQLException(e.getMessage() + "\n"+ queryAndParametersToText(query, parameters), e.getCause());
		}
	}
	public void executeUpdate(PreparedStatement statement) throws PersistenceException {
		try{
			statement.executeUpdate();
			if(!statement.getConnection().getAutoCommit()){
				statement.getConnection().commit();
			}
		} catch(java.sql.SQLException e){
			if(statement != null){
				try {
					if(!statement.getConnection().getAutoCommit()){
						statement.getConnection().rollback();
					}
				} catch (java.sql.SQLException e1) {
					throw new SQLException("Could not rollback", e1);
				}
			}
			throw new SQLException("Exception while executing", e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					log.log(Level.SEVERE,"Could not close statement", e);
				}
			}
		}
	}
	private String queryAndParametersToText(String query, Object...parameters){
		StringBuffer params = new StringBuffer(query);
		params.append('\n');
		int index = 1;
		for(Object parameter: parameters){
			params.append("Parameter #");
			params.append(index++);
			params.append(" = ");
			if (parameter == null)
				params.append("NULL");
			else if (parameter instanceof CharSequence)
				params.append("'" + parameter + "'");
			else if (parameter instanceof Number || parameter instanceof Boolean || parameter.getClass().isPrimitive())
				params.append(parameter);
			else
				params.append("{" + parameter + "}");
			params.append('\n');
		}
		return params.toString();
	}
}
