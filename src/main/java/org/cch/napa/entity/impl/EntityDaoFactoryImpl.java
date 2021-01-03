package org.cch.napa.entity.impl;

import java.util.HashMap;
import java.util.Map;

import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.ConnectionProvider;
import org.cch.napa.entity.EntityDao;
import org.cch.napa.JdbcDao;
import org.cch.napa.entity.SQLGenerator;
import org.cch.napa.entity.SQLTypeMapper;
import org.cch.napa.entity.annotations.atk.EntityHandler;

/**
 * @author Christophe Champagne
 *
 */
public class EntityDaoFactoryImpl implements EntityDaoFactory {
	private final Map<Class<? >, EntityHandler<? >> handlers = new HashMap<Class<? >, EntityHandler<? >>();
	private final Map<Class<? >, SQLGenerator<? >> generators = new HashMap<Class<? >, SQLGenerator<? >>();
	private final ConnectionProvider connectionProvider;

	public EntityDaoFactoryImpl(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	/**
	 * @throws AnnotationException Exception thrown if the entity is not valid. E.g. it contains inconsistent usage of annotation, name conflict....
	 * @see EntityDaoFactory#getEntityDao(java.lang.Class)
	 */
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new EntityDaoImpl<E>(entityClass, this);
	}

	/**
	 * @see EntityDaoFactory#getJdbcDao()
	 */
	public JdbcDao getJdbcDao(){
		return new JdbcDaoImpl(this);
	}

	/**
	 * @see EntityDaoFactory#getJdbcDao(ConnectionProvider)
	 */
	public JdbcDao getJdbcDao(ConnectionProvider connectionProvider){
		return new JdbcDaoImpl(connectionProvider, this);
	}

	/**
	 * @see EntityDaoFactory#getEntityDao(java.lang.Class, ConnectionProvider)
	 */
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new EntityDaoImpl<E>(entityClass, this, connectionProvider);
	}

	/**
	 * @see EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new DefaultSQLTypeMapper();
	}

	/**
	 * @see EntityDaoFactory#getEntityHandler(java.lang.Class)
	 */
	public <E> EntityHandler<E> getEntityHandler(Class<E> entityClass) throws AnnotationException {
		@SuppressWarnings("unchecked")
		EntityHandler<E> instance = (EntityHandler<E>) handlers.get(entityClass);
		if(instance == null){
			instance = new EntityHandler<E>(entityClass, this);
			handlers.put(entityClass, instance);
		}
		return instance;
	}

	/**
	 * @see EntityDaoFactory#getSQLGenerator(java.lang.Class)
	 */
	public <E> SQLGenerator<E> getSQLGenerator(Class<E> entityClass) throws AnnotationException {
		@SuppressWarnings("unchecked")
		SQLGenerator<E> sqlGenerator = (SQLGenerator<E>) generators.get(entityClass);
		if(sqlGenerator == null){
			sqlGenerator = new SQLGenerator<E>(entityClass, this);
			generators.put(entityClass, sqlGenerator);
		}
		return sqlGenerator;
	}

	/**
	 * @see EntityDaoFactory#getEntityRecordMapper(java.lang.Class)
	 */
	public <E> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass)
			throws AnnotationException {
		return new EntityRecordMapper<E>(entityClass, this);
	}

	public ConnectionProvider getDefaultConnectionProvider() {
		return connectionProvider;
	}

}
