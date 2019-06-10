/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.ibm.next.mam.persistence.ConnectionProvider;

/**
 * @author Christophe Champagne (GII561)
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
	 * @see com.ibm.next.mam.persistence.ConnectionProvider#resetConnection(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	public Connection resetConnection(String connectionString, String user, String password,
			Map<?, ?> additionalParameters) throws SQLException {
		return resetConnection();
	}
}