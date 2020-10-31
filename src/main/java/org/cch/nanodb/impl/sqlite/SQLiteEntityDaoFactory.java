/**
 * by Christophe Champagne
 */
package org.cch.nanodb.impl.sqlite;

import org.cch.nanodb.ConnectionProvider;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.EntityDao;
import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.entity.Persistable;
import org.cch.nanodb.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteEntityDaoFactory extends EntityDaoFactoryImpl {
	

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
