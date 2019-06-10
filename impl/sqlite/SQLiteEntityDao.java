/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl.sqlite;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.ibm.next.mam.persistence.impl.EntityDaoImpl;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class SQLiteEntityDao<E extends Persistable> extends EntityDaoImpl<E> {

	/**
	 * @param entityClass
	 * @param factory
	 * @param connectionProvider
	 * @throws AnnotationException
	 */
	public SQLiteEntityDao(Class<E> entityClass, EntityDaoFactory factory, ConnectionProvider connectionProvider)
			throws AnnotationException {
		super(entityClass, factory, connectionProvider);
	}

	/**
	 * @param entityClass
	 * @param factory
	 * @throws AnnotationException
	 */
	public SQLiteEntityDao(Class<E> entityClass, EntityDaoFactory factory) throws AnnotationException {
		super(entityClass, factory);
	}

	/**
	 * @see com.ibm.next.mam.persistence.impl.EntityDaoImpl#checkValue(java.lang.Object, com.ibm.next.mam.persistence.annotations.atk.EntityField)
	 */
	@Override
	protected void checkValue(Object val, EntityField field) throws PersistenceException {
		//Do nothing because SQLite doesn't care about size limitations
	}
}
