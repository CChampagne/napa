package org.cch.napa.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.cch.napa.ConnectionProvider;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.impl.EntityDaoFactoryImpl;
import org.cch.napa.entity.impl.db2e.DB2eEntityDaoFactory;
import org.cch.napa.entity.impl.sqlite.SQLiteEntityDaoFactory;
import org.cch.napa.jdbc.BasicConnectionProvider;
import org.cch.napa.jdbc.DB2eConnectionProvider;
import org.cch.napa.jdbc.SQLiteConnectionProvider;

/**
 * Class retrieving the default factory or connection provider implementation for specific databases
 *
 * @author Christophe Champagne
 */
public class EntityDaoFactoryHelper {
    public final static String SQLITE_DB_TYPE = "sqlite";
    public final static String DB2E_DB_TYPE = "db2e";

    //Map of supported DB
    private static Map<String, Class<? extends EntityDaoFactory>> factories = new HashMap<String, Class<? extends EntityDaoFactory>>();
    private static Map<String, Class<? extends ConnectionProvider>> providers = new HashMap<String, Class<? extends ConnectionProvider>>();

    static {
        //
        registerFactory(DB2E_DB_TYPE, DB2eEntityDaoFactory.class, DB2eConnectionProvider.class);
        registerFactory(SQLITE_DB_TYPE, SQLiteEntityDaoFactory.class, SQLiteConnectionProvider.class);
    }

    /**
     * a connection String is structured as this:
     * org.cch.napa.jdbc:db_type:SomespecificStrings...
     * The aim is to retrieve db type so it can be used to determine factory implementation
     *
     * @param connectionString A complete connection string with the DB type
     * @return
     */
    public static String extractDBTypeFromConnectionString(String connectionString) {
        String dbType = null;
        if (connectionString != null) {
            int posBegin = connectionString.indexOf(':');
            if (posBegin > -1) {
                int posEnd = connectionString.indexOf(':', posBegin + 1);
                if (posEnd > -1 && posEnd > (posBegin + 1)) {
                    dbType = connectionString.substring(posBegin + 1, posEnd).toLowerCase();
                }
            }
        }
        return dbType;
    }

    public static ConnectionProvider getProviderFromConnectionString(String connectionString) {
        ConnectionProvider provider = null;
        Class<? extends ConnectionProvider> cls = providers.get(connectionString);
        if (cls == null) {
            String dbType = extractDBTypeFromConnectionString(connectionString);
            if (dbType != null) {
                cls = providers.get(dbType);
            }
        }
        if (cls != null) {
            try {
                provider = cls.getConstructor(String.class).newInstance(connectionString);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (provider == null) {
            provider = new BasicConnectionProvider(connectionString);//The default implementation
            registerConnectionProvider(connectionString, BasicConnectionProvider.class);
        }
        return provider;
    }

    public static EntityDaoFactory getFactoryFromConnectionString(String connectionString) {
        return getFactoryFromConnectionString(connectionString, getProviderFromConnectionString(connectionString));
    }

    public static EntityDaoFactory getFactoryFromConnectionString(String connectionString, ConnectionProvider connectionProvider) {
        EntityDaoFactory factory = null;
        String dbType = extractDBTypeFromConnectionString(connectionString);
        if (dbType != null) {
            Class<? extends EntityDaoFactory> cls = factories.get(dbType);
            try {
                factory = cls.getConstructor(ConnectionProvider.class).newInstance(connectionProvider);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (factory == null) {
            factory = new EntityDaoFactoryImpl(connectionProvider);//The default implementation
            registerFactory(dbType, factory.getClass());
        }
        return factory;
    }

    /**
     * Associate an EntityFactory implementation with a DB type or connection string
     * @param dbType DB type (sqlite, db2e...) or a connection string
     * @param factoryCls The class of the associated EntityDaoFactory implementation
     */
    public static void registerFactory(String dbType, Class<? extends EntityDaoFactory> factoryCls) {
        if (dbType == null) throw new NullPointerException("The DB type is null");
        if (dbType == null) throw new NullPointerException("The factoryClass is null");
        factories.put(dbType.toLowerCase(), factoryCls);
    }

    /**
     *
     * @param dbType DB type (sqlite, db2e...) or a connection string
     * @param factoryCls The class of the associated EntityDaoFactory implementation
     * @param providerCls The class of the associated ConnectionProvider implementation
     */
    public static void registerFactory(String dbType, Class<? extends EntityDaoFactory> factoryCls, Class<? extends ConnectionProvider> providerCls) {
        registerFactory(dbType, factoryCls);
        registerConnectionProvider(dbType, providerCls);
    }

    /**
     *
     * @param dbType DB type (sqlite, db2e...) or a connection string
     * @param providerCls The class of the associated ConnectionProvider implementation
     */
    public static void registerConnectionProvider(String dbType, Class<? extends ConnectionProvider> providerCls) {
        if (dbType == null) throw new NullPointerException("The DB type is null");
        if (dbType == null) throw new NullPointerException("The provider class is null");
        providers.put(dbType.toLowerCase(), providerCls);
    }

}
