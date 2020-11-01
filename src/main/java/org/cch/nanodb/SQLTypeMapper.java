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

	int getSqlTypeFromClass(Class<?> cls);

	int getSizeFromType(int type);

	int getPrecisionFromType(int type);

	ResultSetAccessor getResultSetGetterFromClass(Class<?> cls,
			int sqlType);
	
	void setParameter(PreparedStatement statement, int index, Object value)throws SQLException, PersistenceException;

}