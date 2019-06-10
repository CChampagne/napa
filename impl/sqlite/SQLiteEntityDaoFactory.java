/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl.sqlite;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.persistence.EntityDao;
import com.ibm.next.mam.persistence.SQLTypeMapper;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class SQLiteEntityDaoFactory extends EntityDaoFactoryImpl {
	

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new SQLiteTypeMapper();
	}

	/**
	 * @see com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl#getEntityDao(java.lang.Class)
	 */
	@Override
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new SQLiteEntityDao<E>(entityClass, this);
	}

	/**
	 * @see com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl#getEntityDao(java.lang.Class, com.ibm.next.mam.persistence.ConnectionProvider)
	 */
	@Override
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new SQLiteEntityDao<E>(entityClass, this, connectionProvider);
	}

}
