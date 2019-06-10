/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.mapper;

import java.sql.ResultSet;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;

/**
 * @author Christophe Champagne (GII561)
 *
 */

public interface ResultsetAccessor{
	public Object getValueFromResultSet(ResultSet resultSet, String columnName) throws PersistenceException;
}