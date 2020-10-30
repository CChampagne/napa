/**
 * by Christophe Champagne
 */
package nanodb.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.ibm.next.mam.config.Configuration;
import nanodb.ConnectionProvider;
import com.ibm.next.mam.util.ConstantsProperties;

/**
 * @author Christophe Champagne
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
	 * @see nanodb.ConnectionProvider#resetConnection(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("hiding")
	public Connection resetConnection(String connectionString, String user, String password,
			Map<?, ?> additionalParameters) throws SQLException {
		this.connectionString = connectionString;
		return resetConnection();
	}
}