
package nanodb.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import nanodb.ConnectionProvider;
import com.sap.ip.me.api.logging.Trace;
import com.sap.ip.me.api.persist.core.PersistenceRuntime;
import com.sap.ip.me.persist.jdbc.DBManager;
import com.sap.ip.me.persist.jdbc.DBPersistenceRuntimeImpl;

/**
 * Class in charge of retrieving a valid DB connection
 * @author Christophe Champagne
 *
 */
public class ConnectionHelper {
	private static DBManager dbManager;
	private static ConnectionProvider sapConnectionProvider;
	private static ConnectionProvider oresConnectionProvider;
	private static Trace TRACE = Trace.getInstance(ConnectionHelper.class.getName());
	/**
	 * Retrieves the working DB Connection if the PErsistenceRuntime is specialized for the DB
	 * @return a working connection if the PersistenceRuntime is a DBPersistenceRuntimeImpl, null else
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SQLException
	 */
	public static DBManager getDBManager() throws SQLException{
		if(dbManager == null){
			PersistenceRuntime persistenceRuntime = PersistenceRuntime.getInstance();
			if(persistenceRuntime instanceof DBPersistenceRuntimeImpl){
				Method getManager;
				try {
					getManager = DBPersistenceRuntimeImpl.class.getDeclaredMethod("getDbManager");
					getManager.setAccessible(true);
					dbManager = (DBManager)getManager.invoke(persistenceRuntime);
				} catch (Exception ex) {
					TRACE.logException(ex);
					throw new SQLException(ex.toString());
				}
			} else {
				return null;
			}
		}
		return dbManager;
	}
	/**
	 * Retrieves the working DB Connection if the PErsistenceRuntime is specialized for the DB
	 * @return a working connection if the PersistenceRuntime is a DBPersistenceRuntimeImpl, null else
	 * @throws SQLException
	 */
	public static ConnectionProvider getSAPConnectionProvider(){
		if(sapConnectionProvider == null){
			sapConnectionProvider = new SAPConnectionProvider();
		}
		return sapConnectionProvider;
	}
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static ConnectionProvider getORESConnectionProvider(){
		if (oresConnectionProvider == null) {
			oresConnectionProvider = new OresDB2eConnectionProvider();
//			oresConnectionProvider = new OresSQLiteConnectionProvider();
		}
		return oresConnectionProvider;
	}
}
