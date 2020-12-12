package org.cch.napa.impl.sqlite;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.ConnectionProvider;
import org.cch.napa.EntityDaoFactory;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.impl.EntityDaoImpl;

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
