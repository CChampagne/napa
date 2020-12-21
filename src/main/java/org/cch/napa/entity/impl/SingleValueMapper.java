package org.cch.napa.entity.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.mapper.RecordMapper;

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