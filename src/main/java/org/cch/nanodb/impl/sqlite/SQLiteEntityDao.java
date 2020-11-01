package org.cch.nanodb.impl.sqlite;

import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.ConnectionProvider;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.annotations.atk.EntityField;
import org.cch.nanodb.impl.EntityDaoImpl;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteEntityDao<E> extends EntityDaoImpl<E> {

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
	 * @see EntityDaoImpl#checkValue(java.lang.Object, EntityField)
	 */
	@Override
	protected void checkValue(Object val, EntityField field) throws PersistenceException {
		//Do nothing because SQLite doesn't care about size limitations
	}
}
