/**
 * by Christophe Champagne
 */
package nanodb.impl.sqlite;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.ConnectionProvider;
import nanodb.EntityDao;
import nanodb.SQLTypeMapper;
import nanodb.entity.Persistable;
import nanodb.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteEntityDaoFactory extends EntityDaoFactoryImpl {
	

	/**
	 * @see nanodb.EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new SQLiteTypeMapper();
	}

	/**
	 * @see nanodb.impl.EntityDaoFactoryImpl#getEntityDao(java.lang.Class)
	 */
	@Override
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new SQLiteEntityDao<E>(entityClass, this);
	}

	/**
	 * @see nanodb.impl.EntityDaoFactoryImpl#getEntityDao(java.lang.Class, nanodb.ConnectionProvider)
	 */
	@Override
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new SQLiteEntityDao<E>(entityClass, this, connectionProvider);
	}

}
