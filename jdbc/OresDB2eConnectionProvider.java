/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.ibm.db2e.jdbc.DB2eDriver;
import com.ibm.next.mam.config.Configuration;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.util.ConstantsProperties;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class OresDB2eConnectionProvider implements ConnectionProvider {
	private Connection connection;
	private String user;
	private String password;
	private String connectionString;
	private Map<?, ?> additionalParameters;
	public Connection resetConnection() throws SQLException {
		close();
		return getConnection();
	}

	public synchronized Connection getConnection() throws SQLException {
		if(connection == null){
			Configuration config = Configuration.getInstance();
			if(connectionString == null) {
				connectionString =  config.getProperty(ConstantsProperties.DB_CONNECTION_STRING);
			}			
			//
			if(user == null) {
				user =  config.getProperty(ConstantsProperties.DB_USER);
			}
			if(password == null){
				password =   config.getProperty(ConstantsProperties.DB_PASSWORD);
			}
			
			Properties p = new Properties();
			p.put("user", user);
			p.put("password", password);
			if(additionalParameters == null){
				p.put("DB2e_ENCODING", "UTF8");
				p.put("ENABLE_DELETE_PHYSICAL_REMOVE", "true");
				p.put("ENABLE_TABLE_CHECKSUM", "false");
			} else {
				p.putAll(additionalParameters);
			}
			Driver driver = new DB2eDriver();
			connection = driver.connect(connectionString, p);;						
		}
		return connection;
	}

	public synchronized void close() throws SQLException {
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
	public Connection resetConnection(String connectionString, String user, String password,
			Map<?, ?> additionalParameters) throws SQLException {
		this.connectionString = connectionString;
		this.user = user;
		this.password = password;
		this.additionalParameters = additionalParameters;
		return resetConnection();
	}
}