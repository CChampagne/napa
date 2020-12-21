package org.cch.napa;

import org.cch.napa.entity.EntityDao;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.EntityDaoFactoryHelper;
import org.cch.napa.entity.TableManager;
import org.cch.napa.exceptions.PersistenceException;

/**
 * This is a facade to the main functionalities of Napa.
 * It can be used as a starting point to everything
 */
public class Client {
    private EntityDaoFactory factory;

    //TODO should we be able to inject specific ConnectionProvider???
    public Client (String connectionString) {
        factory = EntityDaoFactoryHelper.getFactoryFromConnectionString(connectionString);
    }

    public Client (EntityDaoFactory factory) {
        this.factory = factory;
    }

    public JdbcDao getJdbcDao() {
        return factory.getJdbcDao();
    }

    public <E> EntityDao<E> getEntityDao(Class<E> cls) throws PersistenceException {
        return factory.getEntityDao(cls);
    }

    public TableManager getTableManager() throws PersistenceException {
        return new TableManager(factory);
    }

    public EntityDaoFactory getFactory() {
        return factory;
    }
}
