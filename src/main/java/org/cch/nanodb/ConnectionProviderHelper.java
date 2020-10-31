/**
 * by Christophe Champagne
 */
package org.cch.nanodb;

import java.util.HashMap;
import java.util.Map;


/**
 * This class allows to associate a default ConnectionProvider with an EntityDaoFactory
 * @author Christophe Champagne
 *
 */
public class ConnectionProviderHelper {
	private static final Map<Class<? extends EntityDaoFactory>, ConnectionProvider> connectionProviders 
			= new HashMap<Class<? extends EntityDaoFactory>, ConnectionProvider>();

	/**
	 * @return the connectionProvider corresponding to the factory (as default)
	 */
	public static ConnectionProvider getConnectionProvider(Class<? extends EntityDaoFactory> factoryClass) {
		ConnectionProvider connectionProvider = connectionProviders.get(factoryClass);
		if(connectionProvider == null){
			System.err.println("The connection provider hasn't been defined yet");
		}
		return connectionProvider;
	}
	/**
	 * @return the connectionProvider corresponding to the factory (as default)
	 */
	//TODO is this pertinent method???
	public static ConnectionProvider createNewConnectionProvider(Class<? extends EntityDaoFactory> factoryClass) {
		ConnectionProvider connectionProvider = getConnectionProvider(factoryClass);
		if(connectionProvider != null){
			try {
				connectionProvider = connectionProvider.getClass().newInstance();
			} catch (Exception ex) {
				//TODO? catch and log the exception, throw it...?
				ex.printStackTrace();
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
