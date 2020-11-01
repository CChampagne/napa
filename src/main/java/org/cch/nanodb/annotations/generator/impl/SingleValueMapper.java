package org.cch.nanodb.annotations.generator.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.mapper.RecordMapper;

/**
 * Mapper used for the select of a numeric max value or a count
 * @author Christophe Champagne
 *
 */
public class SingleValueMapper implements RecordMapper<Long>{

	/**
	 * @see RecordMapper#map(java.sql.ResultSet)
	 */
	public Long map(ResultSet resultSet) throws SQLException,
			PersistenceException {
		long val = resultSet.getLong(1);
		if(resultSet.wasNull()){
			val = 0;
		}
		return new Long(val);
	}
}