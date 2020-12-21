package org.cch.napa.jdbc;

import org.cch.napa.ConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Christophe Champagne
 *
 */
public class BasicConnectionProvider implements ConnectionProvider {
	private Connection connection;
	private String connectionString;

	public BasicConnectionProvider(String connectionString) {
		this.connectionString = connectionString;
	}
	public Connection resetConnection() throws SQLException {
		close();
		return getConnection();
	}

	public Connection getConnection() throws SQLException {
		if(connection == null){
			connection = DriverManager.getConnection(connectionString);
		}
		return connection;
	}

	public void close() throws SQLException {
		if(connection!=null){
			connection.close();
			connection = null;
		}
	}
}