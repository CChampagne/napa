/**
 * by Christophe Champagne
 */
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
	public JdbcDao getJdbcDao();
	public JdbcDao getJdbcDao(ConnectionProvider connectionProvider);
	public <E> EntityHandler<E> getEntityHandler(Class<E> entityClass) throws AnnotationException;
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass) throws PersistenceException;
	public <E> EntityDao<E> getEntityDao(Class<E> entityClass, ConnectionProvider connectionProvider) throws PersistenceException;
	public SQLTypeMapper getSqlTypeMapper();
	public <E> SQLGenerator<E> getSQLGenerator(Class<E> entityClass) throws AnnotationException;
	public <E> EntityRecordMapper<E> getEntityRecordMapper(Class<E> entityClass) throws AnnotationException;
}
