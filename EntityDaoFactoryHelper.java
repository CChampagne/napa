/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence;

import java.util.HashMap;
import java.util.Map;

import com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl;
import com.ibm.next.mam.persistence.impl.db2e.DB2eEntityDaoFactory;
import com.ibm.next.mam.persistence.impl.sqlite.SQLiteEntityDaoFactory;

/**
 * Class retrieving the default factory implementation for specific databases 
 * 
 * @author Christophe Champagne (GII561)
 *
 */
public class EntityDaoFactoryHelper {
	//Map of supported DB
	private static Map<String, EntityDaoFactory> factories = new HashMap<String, EntityDaoFactory>();
	
	static {
		//
		factories.put("db2e", new DB2eEntityDaoFactory());
		factories.put("sqlite", new SQLiteEntityDaoFactory());
	}
	
	public static String extractDBTypeFromConnectionString(String connectionString){
		String dbType = null;
		if(connectionString != null){
			//a connection String is structured as this:
			//jdbc:db_type:SomespecificStrings...
			//The aim is to retrieve dbtype so it can be used to determine factory implementation
			int posBegin  = connectionString.indexOf(':');
			if(posBegin > -1){
				int posEnd = connectionString.indexOf(':', posBegin + 1);
				if(posEnd > -1 && posEnd > (posBegin + 1)){
					dbType = connectionString.substring(posBegin + 1, posEnd).toLowerCase();
				}
			}
		}
		return dbType;
	}
	/**
	 * 
	 * @param connectionString
	 * @return
	 */
	public static EntityDaoFactory getFactoryFromConnectionString(String connectionString){
		EntityDaoFactory factory = null;
		String dbType = extractDBTypeFromConnectionString(connectionString);
		if(dbType != null){
			factory = factories.get(dbType);
		}
		if(factory == null){
			factory = new EntityDaoFactoryImpl();//The default implementation
		}
		return factory;
	}
	public static void registerFactory(String dbType, EntityDaoFactory factory){
		factories.put(dbType.toLowerCase(), factory);//TODO test parameters aren't null
	}
}
