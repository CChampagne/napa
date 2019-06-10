/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl;

import java.util.HashMap;
import java.util.Map;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.ConnectionProvider;
import com.ibm.next.mam.persistence.EntityDao;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.JdbcDao;
import com.ibm.next.mam.persistence.SQLGenerator;
import com.ibm.next.mam.persistence.SQLTypeMapper;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.ibm.next.mam.persistence.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class EntityDaoFactoryImpl implements EntityDaoFactory {
	private final Map<Class<?  extends Persistable>, EntityHandler<?  extends Persistable>> handlers = new HashMap<Class<?  extends Persistable>, EntityHandler<?  extends Persistable>>();
	private final Map<Class<?  extends Persistable>, SQLGenerator<?  extends Persistable>> generators = new HashMap<Class<?  extends Persistable>, SQLGenerator<?  extends Persistable>>();


	/**
	 * @throws AnnotationException 
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getEntityDao(java.lang.Class)
	 */
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass) throws AnnotationException {
		return new EntityDaoImpl<E>(entityClass, this);
	}

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getJdbcDao()
	 */
	public JdbcDao getJdbcDao(){
		return new JdbcDaoImpl(this);
	}

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getJdbcDao(com.ibm.next.mam.persistence.ConnectionProvider)
	 */
	public JdbcDao getJdbcDao(ConnectionProvider connectionProvider){
		return new JdbcDaoImpl(connectionProvider, this);
	}

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getEntityDao(java.lang.Class, com.ibm.next.mam.persistence.ConnectionProvider)
	 */
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider)
			throws PersistenceException {
		return new EntityDaoImpl<E>(entityClass, this, connectionProvider);
	}

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getSqlTypeMapper()
	 */
	public SQLTypeMapper getSqlTypeMapper() {
		return new DefaultSQLTypeMapper();
	}

	/**
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getEntityHandler(java.lang.Class)
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
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getSQLGenerator(java.lang.Class)
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
	 * @see com.ibm.next.mam.persistence.EntityDaoFactory#getEntityRecordMapper(java.lang.Class)
	 */
	public <E extends Persistable> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass)
			throws AnnotationException {
		return new EntityRecordMapper<E>(entityClass, this);
	}

}
