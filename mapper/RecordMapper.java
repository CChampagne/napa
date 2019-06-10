/**
 * 
 */
package com.ibm.next.mam.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;

/**
 * @author GII561
 *
 */
public interface RecordMapper<T> {
	public T map(ResultSet resultSet) throws SQLException, PersistenceException;
}
