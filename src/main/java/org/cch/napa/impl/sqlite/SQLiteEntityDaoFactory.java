package org.cch.napa.impl.sqlite;

import org.cch.napa.ConnectionProvider;
import org.cch.napa.EntityDaoFactory;
import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.EntityDao;
import org.cch.napa.SQLTypeMapper;
import org.cch.napa.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteEntityDaoFactory extends EntityDaoFactoryImpl {

	public SQLiteEntityDaoFactory(ConnectionProvider provider) {
		super(provider);
	}
	/**
	 * @see EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new SQLiteTypeMapper();
	}

	/**
	 * @see EntityDaoFactoryImpl#getEntityDao(java.lang.Class)
	 */
	@Override
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new SQLiteEntityDao<E>(entityClass, this);
	}

	/**
	 * @see EntityDaoFactoryImpl#getEntityDao(java.lang.Class, ConnectionProvider)
	 */
	@Override
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new SQLiteEntityDao<E>(entityClass, this, connectionProvider);
	}

}
