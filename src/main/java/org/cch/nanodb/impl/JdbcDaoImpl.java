package org.cch.nanodb.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.exceptions.SQLException;
import org.cch.nanodb.ConnectionProvider;
import org.cch.nanodb.ConnectionProviderHelper;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.JdbcDao;
import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.mapper.RecordMapper;

public class JdbcDaoImpl implements JdbcDao {
	private ConnectionProvider connectionProvider;
	private SQLTypeMapper sqlTypeMapper;
	
	public JdbcDaoImpl(ConnectionProvider connectionProvider, EntityDaoFactory factory) {
		this.connectionProvider = connectionProvider;
		sqlTypeMapper = factory.getSqlTypeMapper();
	}	
	public JdbcDaoImpl(EntityDaoFactory factory) {
		this(ConnectionProviderHelper.getConnectionProvider(factory.getClass()), factory);
	}
	/**
	 * @see org.cch.nanodb.JdbcDao#select(java.lang.String, org.cch.nanodb.mapper.RecordMapper, java.lang.Object[])
	 */
	public <T> List<T> select(String query, RecordMapper<T> mapper,Object...parameters) throws PersistenceException{
		List<T> list = new ArrayList<T>();
		PreparedStatement statement = null;
		try{
			statement = prepareStatement(query, parameters);
			ResultSet resultSet =  statement.executeQuery();
			while(resultSet.next()){
				T item = mapper.map(resultSet);
				list.add(item);
			}
		}catch(java.sql.SQLException e){
			throw new SQLException("Failed\n"+ queryAndParametersToText(query, parameters), e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * @see org.cch.nanodb.JdbcDao#prepareStatement(java.lang.String, java.lang.Object[])
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
		} catch (java.sql.SQLException e) {
			throw new SQLException("Failed to prepare statement of\n"+ queryAndParametersToText(query, parameters),e);
		}
		return statement;
	}

//	protected Blob getNewBlob(Connection connection) throws java.sql.SQLException{
//		Blob blob = null;
//		if(connection instanceof DB2eConnection){
//			DB2eConnection db2eConnection = (DB2eConnection)connection;
//			blob = db2eConnection.createBlob();
//		}
//		return blob;
//	}
	/* (non-Javadoc)
	 * @see org.cch.nanodb.JdbcDao#executeUpdate(java.lang.String, java.lang.Object)
	 */
	public void executeUpdate(String query, Object...parameters) throws PersistenceException{
		PreparedStatement statement = null;
		try{
			statement = prepareStatement(query, parameters);
			statement.executeUpdate();
			if(!statement.getConnection().getAutoCommit()){
				statement.getConnection().commit();
			}
		} catch(java.sql.SQLException e){
			String trace = "\n" + queryAndParametersToText(query, parameters);
			if(statement != null){
				try {
					if(!statement.getConnection().getAutoCommit()){
						statement.getConnection().rollback();
					}
				} catch (java.sql.SQLException e1) {
					throw new SQLException("Could not rollback " + trace, e1);
				}
			}
			throw new SQLException("Exception while executing " + trace, e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					System.err.println("Could not close statement");
					e.printStackTrace();
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
