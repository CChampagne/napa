/**
 * by Christophe Champagne
 */
package nanodb.mapper;

import java.sql.ResultSet;

import nanodb.exceptions.PersistenceException;

/**
 * @author Christophe Champagne
 *
 */

public interface ResultsetAccessor{
	public Object getValueFromResultSet(ResultSet resultSet, String columnName) throws PersistenceException;
}