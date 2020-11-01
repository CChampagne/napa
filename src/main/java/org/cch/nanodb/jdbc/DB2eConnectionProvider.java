/**
 * by Christophe Champagne
 */
package org.cch.nanodb.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.cch.nanodb.ConnectionProvider;

/**
 * @author Christophe Champagne
 *
 */
public class DB2eConnectionProvider implements ConnectionProvider {
	private Connection connection;
	private String user;
	private String password;
	private String connectionString;
	private Map<?, ?> additionalParameters;

	public DB2eConnectionProvider(String connectionString, String user, String password,
									  Map<?, ?> additionalParameters) {
		init(connectionString, user, password, additionalParameters);
	}
	private void init(String connectionString, String user, String password,
					  Map<?, ?> additionalParameters) {
		this.connectionString = connectionString;
		this.user = user;
		this.password = password;
		this.additionalParameters = additionalParameters;
	}
	public Connection resetConnection() throws SQLException {
		close();
		return getConnection();
	}

	public synchronized Connection getConnection() throws SQLException {
		if(connection == null){

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
			try {
				Class<?> cls = Class.forName("com.ibm.db2e.jdbc.DB2eDriver");
				Driver driver = (Driver)cls.getConstructor().newInstance();
				connection = driver.connect(connectionString, p);;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

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


}