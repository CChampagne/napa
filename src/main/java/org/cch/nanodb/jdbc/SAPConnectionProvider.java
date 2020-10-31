/**
 * by Christophe Champagne
 */
package org.cch.nanodb.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.cch.nanodb.ConnectionProvider;

/**
 * @author Christophe Champagne
 *
 */
public class SAPConnectionProvider implements ConnectionProvider {
	public Connection resetConnection() throws SQLException {
		return ConnectionHelper.getDBManager().restoreConnection();
	}

	public Connection getConnection() throws SQLException {
		return ConnectionHelper.getDBManager().getConnection();
	}

	public void close() throws SQLException {
		ConnectionHelper.getDBManager().closeConnection();
		
	}

	/**
	 * @see ConnectionProvider#resetConnection(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	public Connection resetConnection(String connectionString, String user, String password,
			Map<?, ?> additionalParameters) throws SQLException {
		return resetConnection();
	}
}