/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence;

import java.util.HashMap;
import java.util.Map;

import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;

/**
 * This class allows to associate a default ConnectionProvider with an EntityDaoFactory
 * @author Christophe Champagne (GII561)
 *
 */
public class ConnectionProviderHelper {
	private static Trace TRACE = Trace.getInstance(ConnectionProviderHelper.class.getName());
	private static final Map<Class<? extends EntityDaoFactory>, ConnectionProvider> connectionProviders 
			= new HashMap<Class<? extends EntityDaoFactory>, ConnectionProvider>();

	/**
	 * @return the connectionProvider corresponding to the factory (as default)
	 */
	public static ConnectionProvider getConnectionProvider(Class<? extends EntityDaoFactory> factoryClass) {
		ConnectionProvider connectionProvider = connectionProviders.get(factoryClass);
		if(connectionProvider == null){
			TRACE.log(Severities.WARNING, "The connection provider hasn't been defined yet");
		}
		return connectionProvider;
	}
	/**
	 * @return the connectionProvider corresponding to the factory (as default)
	 */
	public static ConnectionProvider createNewConnectionProvider(Class<? extends EntityDaoFactory> factoryClass) {
		ConnectionProvider connectionProvider = getConnectionProvider(factoryClass);
		if(connectionProvider != null){
			try {
				connectionProvider = connectionProvider.getClass().newInstance();
			} catch (Exception ex) {
				TRACE.logException(ex);
			}
		}
		return connectionProvider;
	}
	/**
	 * @param connectionProvider the connectionProvider to set
	 */
	public static void setConnectionProvider(ConnectionProvider connectionProvider, Class<? extends EntityDaoFactory> factoryClass) {
		connectionProviders.put(factoryClass, connectionProvider);
	}

}
