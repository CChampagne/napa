/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.ibm.next.mam.persistence.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public interface EntityDaoFactory {
	public JdbcDao getJdbcDao();
	public JdbcDao getJdbcDao(ConnectionProvider connectionProvider);
	public <E extends Persistable> EntityHandler<E> getEntityHandler(Class<E> entityClass) throws AnnotationException;
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass) throws PersistenceException;
	public <E extends Persistable> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider) throws PersistenceException;
	public SQLTypeMapper getSqlTypeMapper();
	public <E extends Persistable> SQLGenerator<E> getSQLGenerator(Class<E> entityClass) throws AnnotationException;
	public <E extends Persistable> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass) throws AnnotationException;
}
