/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.ibm.next.mam.config.Configuration;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.util.ConstantsProperties;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class OresSQLiteConnectionProvider implements ConnectionProvider {
	private Connection connection;
	private String connectionString = null;
	
	public Connection resetConnection() throws SQLException {
		close();
		return getConnection();
	}

	public Connection getConnection() throws SQLException {
		if(connection == null){
			Configuration config = Configuration.getInstance();
			if(connectionString == null) {
				connectionString =  config.getProperty(ConstantsProperties.DB_CONNECTION_STRING);
			}			
			// register the driver 
			String sDriverName = "org.sqlite.JDBC";
			try {
				Class.forName(sDriverName);
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException("Could not find Driver " + sDriverName, ex);
			}
			connection = DriverManager.getConnection(connectionString);
		}
		return connection;
	}

	public void close() throws SQLException {
		if(connection!=null){
			try {
				
				connection.close();
				connection = null;
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * @see com.ibm.next.mam.persistence.ConnectionProvider#resetConnection(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("hiding")
	public Connection resetConnection(String connectionString, String user, String password,
			Map<?, ?> additionalParameters) throws SQLException {
		this.connectionString = connectionString;
		return resetConnection();
	}
}