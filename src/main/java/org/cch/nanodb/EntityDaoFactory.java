package org.cch.nanodb;

import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.annotations.atk.EntityHandler;
import org.cch.nanodb.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne
 *
 */
public interface EntityDaoFactory {
	JdbcDao getJdbcDao();
	JdbcDao getJdbcDao(ConnectionProvider connectionProvider);
	<E> EntityHandler<E> getEntityHandler(Class<E> entityClass) throws AnnotationException;
	<E> EntityDao<E> getEntityDao(Class<E> entityClass) throws PersistenceException;
	<E> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider) throws PersistenceException;
	SQLTypeMapper getSqlTypeMapper();
	<E> SQLGenerator<E> getSQLGenerator(Class<E> entityClass) throws AnnotationException;
	<E> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass) throws AnnotationException;
	ConnectionProvider getDefaultConnectionProvider();
}
