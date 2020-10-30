/**
 * by Christophe Champagne
 */
package nanodb.impl.sqlite;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.ConnectionProvider;
import nanodb.EntityDaoFactory;
import nanodb.annotations.atk.EntityField;
import nanodb.entity.Persistable;
import nanodb.impl.EntityDaoImpl;

/**
 * @author Christophe Champagne
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
	 * @see nanodb.impl.EntityDaoImpl#checkValue(java.lang.Object, nanodb.annotations.atk.EntityField)
	 */
	@Override
	protected void checkValue(Object val, EntityField field) throws PersistenceException {
		//Do nothing because SQLite doesn't care about size limitations
	}
}
