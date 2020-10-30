/**
 * by Christophe Champagne
 */
package nanodb;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.annotations.atk.EntityHandler;
import nanodb.entity.Persistable;
import nanodb.mapper.impl.EntityRecordMapper;

/**
 * @author Christophe Champagne
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
