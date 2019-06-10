package com.ibm.next.mam.persistence.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.errorframework.exceptions.persistence.SQLException;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.persistence.ConnectionProviderHelper;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.JdbcDao;
import com.ibm.next.mam.persistence.SQLTypeMapper;
import com.ibm.next.mam.persistence.mapper.RecordMapper;
import com.ibm.next.mam.util.ObjectDisplayer;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;

public class JdbcDaoImpl implements JdbcDao {
	private static final Trace TRACE = Trace.getInstance(JdbcDaoImpl.class.getName());
	private ConnectionProvider connectionProvider;
	private SQLTypeMapper sqlTypeMapper;
	private ObjectDisplayer objectDisplayer = new ObjectDisplayer();
	
	public JdbcDaoImpl(ConnectionProvider connectionProvider, EntityDaoFactory factory) {
		this.connectionProvider = connectionProvider;
		sqlTypeMapper = factory.getSqlTypeMapper();
	}	
	public JdbcDaoImpl(EntityDaoFactory factory) {
		this(ConnectionProviderHelper.getConnectionProvider(factory.getClass()), factory);
	}
	/* (non-Javadoc)
	 * @see com.ibm.next.mam.persistence.JdbcDao#select(java.lang.String, com.ibm.next.mam.persistence.mapper.RecordMapper, java.lang.Object)
	 */
	public <T> List<T> select(String query, RecordMapper<T> mapper,Object...parameters) throws SQLException, PersistenceException{
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
			traceQuery(e, query, parameters);
			throw new SQLException(e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					TRACE.logException(Severities.WARNING, "Could not close statement",e,true);
				}
			}
		}
		return list;
	}
	/* (non-Javadoc)
	 * @see com.ibm.next.mam.persistence.JdbcDao#prepareStatement(java.lang.String, java.lang.Object)
	 */
	public PreparedStatement prepareStatement(String query, Object...parameters) throws PersistenceException{
		if(TRACE.isLogging(Severities.DEBUG)){
			TRACE.log(Severities.DEBUG, queryAndParametersToText(query, parameters));
		}
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
			throw new SQLException(e);
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
	 * @see com.ibm.next.mam.persistence.JdbcDao#executeUpdate(java.lang.String, java.lang.Object)
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
			traceQuery(e, query, parameters);
			if(statement != null){
				try {
					if(!statement.getConnection().getAutoCommit()){
						statement.getConnection().rollback();
					}
				} catch (java.sql.SQLException e1) {
					throw new SQLException(e1);
				}
			}
			throw new SQLException(e);
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
					TRACE.logException(Severities.WARNING, "Could not close statement",e,true);
				}
			}
		}
	}

	private void traceQuery(Exception e ,String query, Object...parameters){
		TRACE.logException(Severities.ERROR,"SQL error while executing The following query (" + e + ")", e, false);
		TRACE.log(Severities.ERROR,"Parameters : " + queryAndParametersToText(query, parameters));

	}
	private String queryAndParametersToText(String query, Object...parameters){
		StringBuffer params = new StringBuffer(query);
		params.append('\n');
		int index = 1;
		for(Object parameter: parameters){
			params.append("Parameter #");
			params.append(index++);
			params.append(" = ");
			try {
				objectDisplayer.append(parameter, params);
			} catch (IOException ex) {
				params.append(parameter);
			}
			params.append('\n');
		}
		return params.toString();
	}
}
