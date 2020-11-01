/**
 * by Christophe Champagne
 */
package org.cch.nanodb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.mapper.ResultSetAccessor;

/**
 * @author Christophe Champagne
 *
 */
public interface SQLTypeMapper {

	public abstract int getSqlTypeFromClass(Class<?> cls);

	public abstract int getSizeFromType(int type);

	public abstract int getPrecisionFromType(int type);

	public abstract ResultSetAccessor getResultSetGetterFromClass(Class<?> cls,
			int sqlType);
	
	public abstract void setParameter(PreparedStatement statement, int index, Object value)throws SQLException, PersistenceException;

}