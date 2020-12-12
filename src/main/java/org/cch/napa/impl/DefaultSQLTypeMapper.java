package org.cch.napa.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import org.cch.napa.SQLTypeMapper;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.SQLNull;
import org.cch.napa.annotations.DBField;
import org.cch.napa.mapper.ResultSetAccessor;
/**
 * @author Christophe Champagne
 *
 */
public class DefaultSQLTypeMapper implements SQLTypeMapper {
	
	/**
	 * @see SQLTypeMapper#getSqlTypeFromClass(java.lang.Class)
	 */
	public int getSqlTypeFromClass(Class<?> cls){
		if(cls.equals(Integer.class) || cls.equals(Integer.TYPE)){
			return Types.INTEGER;
		} else if(cls.equals(Long.class) || cls.equals(Long.TYPE)){
			return Types.BIGINT;
		} else if(cls.equals(Short.class) || cls.equals(Short.TYPE)){
			return Types.SMALLINT;
		} else if(Number.class.isAssignableFrom(cls)
				||cls.equals(Double.TYPE) ||cls.equals(Float.TYPE)){
			return Types.DECIMAL;
		} else if(cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)){
			return Types.BOOLEAN;
		} else if(cls.equals(String.class) || cls.isEnum()){
			return Types.VARCHAR;
		} else if(cls.equals(java.sql.Date.class)){
			return Types.DATE;
		} else if(cls.equals(java.sql.Time.class)){
			return Types.TIME;
		} else if(cls.equals(java.util.Date.class)){
			return Types.TIMESTAMP;
		} else if(cls.equals(java.util.Calendar.class)){
			return Types.TIMESTAMP;
		} else if(cls.equals(Blob.class)){
			return Types.BLOB;
		} else if(Serializable.class.isAssignableFrom(cls)){
			return Types.BLOB;
		}
		return Types.NULL;
	}
	/**
	 * @see SQLTypeMapper#getSizeFromType(int)
	 */
	public int getSizeFromType(int type){
		switch(type){
			case Types.VARCHAR:
				return 255;
			case Types.BLOB:
				return 10240;
			case Types.DECIMAL:
			case Types.NUMERIC:
				return 10;
		}
		return DBField.DEFAULT;
	}
	/**
	 * @see SQLTypeMapper#getPrecisionFromType(int)
	 */
	public int getPrecisionFromType(int type){
		switch(type){
			case Types.DECIMAL:
			case Types.NUMERIC:
				return 2;
		}
		return DBField.DEFAULT;
	}
	/**
	 * @see SQLTypeMapper#getResultSetGetterFromClass(java.lang.Class, int)
	 */
	public  ResultSetAccessor getResultSetGetterFromClass(final Class<?>cls, int sqlType){
		ResultSetAccessor resultSetGetter = null;
		if((cls.equals(Object.class) || cls.equals(Serializable.class)) && sqlType!=Types.BLOB){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Object getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getObject(columnName);
				}				
			};
		}  else if (cls.equals(String.class) || CharSequence.class.isAssignableFrom(cls)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public String getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getString(columnName);
				}				
			};			
		} else if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Integer getValue(ResultSet resultSet, String columnName) throws SQLException {
					int val = resultSet.getInt(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Integer(val);
				}				
			};			
		} else if (cls.equals(Long.class) || cls.equals(Long.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Long getValue(ResultSet resultSet, String columnName) throws SQLException {
					long val = resultSet.getLong(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Long(val);
				}				
			};			
		} else if (cls.equals(Short.class)  || cls.equals(Short.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Short getValue(ResultSet resultSet, String columnName) throws SQLException {
					short val = resultSet.getShort(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Short(val);
				}				
			};			
		} else if (cls.equals(Double.class) || cls.equals(Double.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Double getValue(ResultSet resultSet, String columnName) throws SQLException {
					double val = resultSet.getDouble(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Double(val);
				}				
			};			
		}  else if (cls.equals(Float.class) || cls.equals(Float.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Float getValue(ResultSet resultSet, String columnName) throws SQLException {
					double val = resultSet.getFloat(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Float(val);
				}				
			};			
		}  else if (cls.equals(BigDecimal.class)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public BigDecimal getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getBigDecimal(columnName);
				}				
			};			
		} else if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Boolean getValue(ResultSet resultSet, String columnName) throws SQLException {
					int val = resultSet.getInt(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Boolean(val==1);
				}				
			};			
		} else if (cls.equals(Timestamp.class) 
				||(cls.equals(Date.class) && (sqlType == Types.TIMESTAMP || sqlType == Types.NULL))){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getTimestamp(columnName);
				}				
			};			
		} else if ((cls.equals(java.sql.Date.class)) || (cls.equals(Date.class) && sqlType == Types.DATE)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getDate(columnName);
				}				
			};			
		} else if ((cls.equals(Time.class)) || (cls.equals(Date.class) && sqlType == Types.TIME)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getTime(columnName);
				}				
			};			
		} else if (Calendar.class.isAssignableFrom(cls)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Calendar getValue(ResultSet resultSet, String columnName) throws SQLException {
					Date date = resultSet.getTimestamp(columnName);
					Calendar cal = null;
					if(date != null){
						cal =Calendar.getInstance();
						cal.setTime(date);
					}
					return cal;
				}				
			};			
		} else if (cls.isEnum()){
			resultSetGetter =  new AbstractResultSetGetter(){
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Enum<?> getValue(ResultSet resultSet, String columnName) throws SQLException {
					String val = resultSet.getString(columnName);
					if (val == null) return null;
					Enum<?>valAsEnum = null;
					try {
						valAsEnum = Enum.valueOf((Class<? extends Enum>)cls, val);
					} catch (Exception e) {
						//TODO allow to be stricter by config and throw exception
						System.err.println("Cannot convert '" + val +"' string to " + cls.getName()+ " enumeration");
					}
					return valAsEnum;
				}				
			};			
		} else if (cls.equals(Blob.class)){
			resultSetGetter =  new AbstractResultSetGetter(){
				public Blob getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getBlob(columnName);
				}				
			};			
		} else if (Serializable.class.isAssignableFrom(cls)){
			resultSetGetter =  getResultSetBlobAccessor();
		}
		return resultSetGetter;
	}
	
	public void setParameter(PreparedStatement statement, int index, Object value) throws SQLException, PersistenceException{
		if(value == null){
			//Null parameter given to statement! This may cause dysfunction's (depends on DB)
			statement.setObject(index, null);
		}else if (value.getClass().isArray()){
			statement.setNull(index, ((SQLNull)value).getSqlType());
		}else if (value instanceof SQLNull){
			int type = ((SQLNull)value).getSqlType();
			if(type == Types.BOOLEAN || type == Types.BIT){
				type = Types.SMALLINT;
			}
			statement.setNull(index, type);
		}else if (value instanceof String){
			statement.setString(index, (String)value);
		}else if (value instanceof Boolean){
			statement.setInt(index, Boolean.TRUE.equals(value)?1:0);
		}else if (value instanceof Integer){
			statement.setInt(index, (Integer)value);
		}else if (value instanceof Long){
			statement.setLong(index, (Long)value);
		}else if (value instanceof Short){
			statement.setShort(index, (Short)value);
		}else if (value instanceof Double){
			statement.setDouble(index, (Double)value);
		}else if (value instanceof Float){
			statement.setFloat(index, (Float)value);
		}else if (value instanceof BigDecimal){
			statement.setBigDecimal(index, (BigDecimal)value);
		}else if (value instanceof Number){
			statement.setDouble(index, ((Number)value).doubleValue());
		}else if (value instanceof Date){
			statement.setTimestamp(index, new Timestamp(((Date)value).getTime()));
		}else if (value instanceof Calendar){
			statement.setTimestamp(index, new Timestamp(((Calendar)value).getTime().getTime()));
		}else if (value.getClass().isEnum()){
			statement.setString(index, ((Enum<?>)value).name());
		}else if (value instanceof Serializable){//lets make it a blob
			setBlob(statement, value, index);
		}else{
			throw new PersistenceException("Could not find corresponding type of parameter #"+ index + " : "+ value);
		}

	}
	protected void setBlob(PreparedStatement statement, Object o, int index) throws java.sql.SQLException, PersistenceException{
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bas = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			byte[] content = bos.toByteArray();
			bas = new ByteArrayInputStream(content);
			statement.setBinaryStream(index, bas, content.length);
		} catch (IOException e) {
			throw new PersistenceException(e);
		} catch (Exception e) {
			throw new PersistenceException(e);
		} finally {
	        try {
				oos.close();
			} catch (IOException e) {
				throw new PersistenceException("Could not close the queue inputStream", e);
			}			
		}
	}

	protected ResultSetAccessor getResultSetBlobAccessor(){
		return new AbstractResultSetGetter(){
			public Serializable getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException {
				Serializable serializable = null;
				try {
					serializable = deserializeBlob(resultSet.getBlob(columnName));
				} catch (IOException e) {
					throw new PersistenceException("Could not deserialize column " + columnName, e);
				}
				return serializable;
			}				
			protected Serializable deserializeBlob(Blob blob) throws IOException, SQLException, PersistenceException{
				Serializable value = null;
				if(blob != null){
					ObjectInputStream oi = null;
					try {
						oi = new ObjectInputStream(blob.getBinaryStream());
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
