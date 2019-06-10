/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.mapper.ResultsetAccessor;

/**
 * @author Christophe Champagne (GII561)
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