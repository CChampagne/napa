/**
 * by Christophe Champagne
 */
package nanodb.impl;

import java.util.HashMap;
import java.util.Map;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.ConnectionProvider;
import nanodb.EntityDao;
import nanodb.EntityDaoFactory;
import nanodb.JdbcDao;
import nanodb.SQLGenerator;
import nanodb.SQLTypeMapper;
import nanodb.annotations.atk.EntityHandler;
import nanodb.entity.Persistable;
import nanodb.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne
 *
 */
public class EntityDaoFactoryImpl implements EntityDaoFactory {
	private final Map<Class<?  extends Persistable>, EntityHandler<?  extends Persistable>> handlers = new HashMap<Class<?  extends Persistable>, EntityHandler<?  extends Persistable>>();
	private final Map<Class<?  extends Persistable>, SQLGenerator<?  extends Persistable>> generators = new HashMap<Class<?  extends Persistable>, SQLGenerator<?  extends Persistable>>();


	/**
	 * @throws AnnotationException 
	 * @see nanodb.EntityDaoFactory#getEntityDao(java.lang.Class)
	 */
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new EntityDaoImpl<E>(entityClass, this);
	}

	/**
	 * @see nanodb.EntityDaoFactory#getJdbcDao()
	 */
	public JdbcDao getJdbcDao(){
		return new JdbcDaoImpl(this);
	}

	/**
	 * @see nanodb.EntityDaoFactory#getJdbcDao(nanodb.ConnectionProvider)
	 */
	public JdbcDao getJdbcDao(ConnectionProvider connectionProvider){
		return new JdbcDaoImpl(connectionProvider, this);
	}

	/**
	 * @see nanodb.EntityDaoFactory#getEntityDao(java.lang.Class, nanodb.ConnectionProvider)
	 */
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new EntityDaoImpl<E>(entityClass, this, connectionProvider);
	}

	/**
	 * @see nanodb.EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new DefaultSQLTypeMapper();
	}

	/**
	 * @see nanodb.EntityDaoFactory#getEntityHandler(java.lang.Class)
	 */
	public <E extends Persistable> EntityHandler<E> getEntityHandler(Class<E> entityClass) throws AnnotationException {
		@SuppressWarnings("unchecked")
		EntityHandler<E> instance = (EntityHandler<E>) handlers.get(entityClass);
		if(instance == null){
			instance = new EntityHandler<E>(entityClass, this);
			handlers.put(entityClass, instance);
		}
		return instance;
	}

	/**
	 * @see nanodb.EntityDaoFactory#getSQLGenerator(java.lang.Class)
	 */
	public <E extends Persistable> SQLGenerator<E> getSQLGenerator(Class<E> entityClass) throws AnnotationException {
		@SuppressWarnings("unchecked")
		SQLGenerator<E> sqlGenerator = (SQLGenerator<E>) generators.get(entityClass);
		if(sqlGenerator == null){
			sqlGenerator = new SQLGenerator<E>(entityClass, this);
			generators.put(entityClass, sqlGenerator);
		}
		return sqlGenerator;
	}

	/**
	 * @see nanodb.EntityDaoFactory#getEntityRecordMapper(java.lang.Class)
	 */
	public <E extends Persistable> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass)
			throws AnnotationException {
		return new EntityRecordMapper<E>(entityClass, this);
	}

}
