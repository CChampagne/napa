package org.cch.nanodb.impl;

import java.util.HashMap;
import java.util.Map;

import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.ConnectionProvider;
import org.cch.nanodb.EntityDao;
import org.cch.nanodb.JdbcDao;
import org.cch.nanodb.SQLGenerator;
import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.annotations.atk.EntityHandler;
import org.cch.nanodb.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne
 *
 */
public class EntityDaoFactoryImpl implements EntityDaoFactory {
	private final Map<Class<? >, EntityHandler<? >> handlers = new HashMap<Class<? >, EntityHandler<? >>();
	private final Map<Class<? >, SQLGenerator<? >> generators = new HashMap<Class<? >, SQLGenerator<? >>();


	/**
	 * @throws AnnotationException 
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

}
