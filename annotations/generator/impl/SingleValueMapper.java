/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.mapper.RecordMapper;

/**
 * Mapper used for the select of a numeric max value or a count
 * @author Christophe Champagne (GII561)
 *
 */
public class SingleValueMapper implements RecordMapper<Long>{

	/**
	 * @see com.ibm.next.mam.persistence.mapper.RecordMapper#map(java.sql.ResultSet)
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