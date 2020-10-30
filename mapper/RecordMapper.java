/**
 * 
 */
package nanodb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import nanodb.exceptions.PersistenceException;

/**
 * @author Christophe Champagne
 *
 */
public interface RecordMapper<T> {
	public T map(ResultSet resultSet) throws SQLException, PersistenceException;
}
