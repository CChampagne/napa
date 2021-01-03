package org.cch.napa.entity.impl.sqlite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cch.napa.entity.SQLTypeMapper;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.entity.impl.AbstractQueryValueAccessor;
import org.cch.napa.entity.impl.DefaultSQLTypeMapper;
import org.cch.napa.mapper.QueryValueAccessor;

/**
 * @author Christophe Champagne
 *
 */
public class SQLiteTypeMapper extends DefaultSQLTypeMapper {
	/**
	 * @see SQLTypeMapper#getResultSetGetterFromClass(java.lang.Class, int)
	 */
	public QueryValueAccessor getResultSetGetterFromClass(Class<?>cls, int sqlType){
		QueryValueAccessor resultSetGetter = null;
		if (cls.equals(BigDecimal.class)){
			resultSetGetter =  new AbstractQueryValueAccessor(sqlType){
				public BigDecimal getValue(ResultSet resultSet, String columnName) throws SQLException {
					double val = resultSet.getDouble(columnName);
					BigDecimal value = null;
					if(!resultSet.wasNull()){
						value = new BigDecimal(val);
					}
					return value;
				}				
			};			
		} else {
			resultSetGetter = super.getResultSetGetterFromClass(cls, sqlType);
		} 
		return resultSetGetter;
	}
	
	public void setParameter(PreparedStatement statement, int index, Object value) throws SQLException, PersistenceException{
		if (value !=null && value instanceof BigDecimal){
			statement.setDouble(index, ((BigDecimal)value).doubleValue());
		}else{
			super.setParameter(statement, index, value);
		}

	}
	@SuppressWarnings("resource")
	protected void setBlob(PreparedStatement statement, Object o, int index) throws java.sql.SQLException, PersistenceException{
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			byte[] content = bos.toByteArray();
			statement.setBytes(index, content);
		} catch (IOException e) {
			throw new PersistenceException(e);
		} catch (Exception e) {
			throw new PersistenceException(e);
		} finally {
	        try {
	        	//bos closed when oos is closed
				oos.close();
			} catch (IOException e) {
				throw new PersistenceException( "Could not close the queue inputStream", e);
			}			
		}
	}
	protected QueryValueAccessor getResultSetBlobAccessor(){
		return new AbstractQueryValueAccessor(){
			public Serializable getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException {
				Serializable serializable = null;
				try {
					serializable = deserializeBytes(resultSet.getBytes(columnName));
				} catch (IOException e) {
					throw new PersistenceException("Could not deserialize column " + columnName, e);
				}
				return serializable;
			}				
			protected Serializable deserializeBytes(byte[] blob) throws IOException, SQLException, PersistenceException{
				Serializable value = null;
				if(blob != null && blob.length > 0){
					ObjectInputStream oi = null;
					try {
						oi = new ObjectInputStream(new ByteArrayInputStream(blob));
						value = (Serializable)oi.readObject();
					} catch (ClassNotFoundException e) {
						throw new PersistenceException(e);
					} finally {
						if(oi != null){
							oi.close();
						}
					}
				}
				return value;
			}		
		};
	}
}
