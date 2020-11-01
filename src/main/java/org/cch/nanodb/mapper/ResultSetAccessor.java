package org.cch.nanodb.mapper;

import java.sql.ResultSet;

import org.cch.nanodb.exceptions.PersistenceException;

/**
 * @author Christophe Champagne
 *
 */

public interface ResultSetAccessor{
	Object getValueFromResultSet(ResultSet resultSet, String columnName) throws PersistenceException;
}