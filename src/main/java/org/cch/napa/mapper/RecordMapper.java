package org.cch.napa.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.napa.exceptions.PersistenceException;

/**
 * @author Christophe Champagne
 *
 */
public interface RecordMapper<T> {
	T map(ResultSet resultSet) throws SQLException, PersistenceException;
}
