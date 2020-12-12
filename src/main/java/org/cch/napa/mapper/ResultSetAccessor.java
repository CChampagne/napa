package org.cch.napa.mapper;

import java.sql.ResultSet;

import org.cch.napa.exceptions.PersistenceException;

/**
 * @author Christophe Champagne
 *
 */

public interface ResultSetAccessor{
	Object getValueFromResultSet(ResultSet resultSet, String columnName) throws PersistenceException;
}