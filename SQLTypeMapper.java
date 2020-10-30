/**
 * by Christophe Champagne
 */
package nanodb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import nanodb.exceptions.PersistenceException;
import nanodb.mapper.ResultsetAccessor;

/**
 * @author Christophe Champagne
 *
 */
public interface SQLTypeMapper {

	public abstract int getSqlTypeFromClass(Class<?> cls);

	public abstract int getSizeFromType(int type);

	public abstract int getPrecisionFromType(int type);

	public abstract ResultsetAccessor getResulsetGetterFromClass(Class<?> cls,
			int sqlType);
	
	public abstract void setParameter(PreparedStatement statement, int index, Object value)throws SQLException, PersistenceException;

}