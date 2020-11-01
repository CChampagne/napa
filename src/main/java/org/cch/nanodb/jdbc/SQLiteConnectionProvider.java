package org.cch.nanodb.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.cch.nanodb.ConnectionProvider;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteConnectionProvider implements ConnectionProvider {
	private Connection connection;
	private String connectionString = null;

	SQLiteConnectionProvider(String connectionString) {
		this.connectionString = connectionString;
	}
	public Connection resetConnection() throws SQLException {
		close();
		return getConnection();
	}

	public Connection getConnection() throws SQLException {
		if(connection == null){
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
}