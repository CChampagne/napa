package org.cch.napa.mapper;

import org.cch.napa.exceptions.PersistenceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Christophe Champagne
 *
 */

public interface QueryValueAccessor {
	Object getValueFromResultSet(ResultSet resultSet, String columnName) throws PersistenceException;
	void assignToStatement(PreparedStatement statement, int index, Object value) throws PersistenceException;
}