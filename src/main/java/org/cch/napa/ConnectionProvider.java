package org.cch.napa;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Connection provider is intended to create a connection or reset it
 * (close the current connection and then recreate a new one)
 * @author Christophe Champagne
 *
 */
public interface ConnectionProvider {
	/**
	 * This method returns a connectionObject
	 * @return an instance of Connection
	 */
	Connection getConnection() throws SQLException;
	/**
	 * Closes the current connection(s) and creates an new one;
	 * @return the new Connection
	 */
	Connection resetConnection() throws SQLException;
	/**
	 * Closes any current connection;
	 */
	void close() throws SQLException;

}
