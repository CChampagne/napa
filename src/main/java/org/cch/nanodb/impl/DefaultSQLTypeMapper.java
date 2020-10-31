/**
 * by Christophe Champagne
 */
package org.cch.nanodb.impl;

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

import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.SQLNull;
import org.cch.nanodb.annotations.DBField;
import org.cch.nanodb.mapper.ResultsetAccessor;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;
/**
 * @author Christophe Champagne
 *
 */
public class DefaultSQLTypeMapper implements SQLTypeMapper {
	private static Trace TRACE = Trace.getInstance(DefaultSQLTypeMapper.class.getName());
	
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
	 * @see SQLTypeMapper#getResulsetGetterFromClass(java.lang.Class, int)
	 */
	public  ResultsetAccessor getResulsetGetterFromClass(final Class<?>cls, int sqlType){
		ResultsetAccessor resulsetGetter = null;
		if((cls.equals(Object.class) || cls.equals(Serializable.class)) && sqlType!=Types.BLOB){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Object getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getObject(columnName);
				}				
			};
		}  else if (cls.equals(String.class) || CharSequence.class.isAssignableFrom(cls)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public String getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getString(columnName);
				}				
			};			
		} else if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Integer getValue(ResultSet resultSet, String columnName) throws SQLException {
					int val = resultSet.getInt(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Integer(val);
				}				
			};			
		} else if (cls.equals(Long.class) || cls.equals(Long.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Long getValue(ResultSet resultSet, String columnName) throws SQLException {
					long val = resultSet.getLong(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Long(val);
				}				
			};			
		} else if (cls.equals(Short.class)  || cls.equals(Short.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Short getValue(ResultSet resultSet, String columnName) throws SQLException {
					short val = resultSet.getShort(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Short(val);
				}				
			};			
		} else if (cls.equals(Double.class) || cls.equals(Double.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Double getValue(ResultSet resultSet, String columnName) throws SQLException {
					double val = resultSet.getDouble(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Double(val);
				}				
			};			
		}  else if (cls.equals(Float.class) || cls.equals(Float.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Float getValue(ResultSet resultSet, String columnName) throws SQLException {
					double val = resultSet.getFloat(columnName);
					if(resultSet.wasNull()){
						return null;
					}
					return new Float(val);
				}				
			};			
		}  else if (cls.equals(BigDecimal.class)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public BigDecimal getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getBigDecimal(columnName);
				}				
			};			
		} else if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)){
			resulsetGetter =  new AbstractResultsetGetter(){
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
			resulsetGetter =  new AbstractResultsetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getTimestamp(columnName);
				}				
			};			
		} else if ((cls.equals(java.sql.Date.class)) || (cls.equals(Date.class) && sqlType == Types.DATE)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getDate(columnName);
				}				
			};			
		} else if ((cls.equals(Time.class)) || (cls.equals(Date.class) && sqlType == Types.TIME)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Date getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getTime(columnName);
				}				
			};			
		} else if (Calendar.class.isAssignableFrom(cls)){
			resulsetGetter =  new AbstractResultsetGetter(){
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
			resulsetGetter =  new AbstractResultsetGetter(){
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Enum<?> getValue(ResultSet resultSet, String columnName) throws SQLException {
					String val = resultSet.getString(columnName);
					Enum<?>valAsEnum = null;
					try {
						valAsEnum = Enum.valueOf((Class<? extends Enum>)cls, val);
					} catch (Exception e) {
						TRACE.logException("Cannot convert '" + val +"' string to " + cls.getName()+ " enumeration",e, false);;
					}
					return valAsEnum;
				}				
			};			
		} else if (cls.equals(Blob.class)){
			resulsetGetter =  new AbstractResultsetGetter(){
				public Blob getValue(ResultSet resultSet, String columnName) throws SQLException {
					return resultSet.getBlob(columnName);
				}				
			};			
		} else if (Serializable.class.isAssignableFrom(cls)){
			resulsetGetter =  getResultsetBlobAccessor();
		}
		return resulsetGetter;
	}
	
	public void setParameter(PreparedStatement statement, int index, Object value) throws SQLException, PersistenceException{
		if(value == null){
			//Null parameter given to statement! This may cause dysfunctionments (depends on DB
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
			TRACE.log(Severities.WARNING, "Could not find corresponding type of parameter #"+ index + " : "+ value);
			//TODO raise exception?
			//statement.setTimestamp(index, new Timestamp(((Calendar)value).getTime().getTime()));					
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
			TRACE.logException(Severities.ERROR, e);
			throw new PersistenceException();
		} catch (Exception e) {
			TRACE.logException(Severities.FATAL, e);
			throw new PersistenceException();
		} finally {
	        try {
				oos.close();
			} catch (IOException e) {
				TRACE.logException(Severities.WARNING, "Could not close the queue inputStream", e, true);
				throw new PersistenceException();
			}			
		}
	}

	protected ResultsetAccessor getResultsetBlobAccessor(){
		return new AbstractResultsetGetter(){
			public Serializable getValue(ResultSet resultSet, String columnName) throws SQLException, PersistenceException {
				Serializable serializable = null;
				try {
					serializable = deserializeBlob(resultSet.getBlob(columnName));
				} catch (IOException e) {
					TRACE.logException(Severities.ERROR,"Could not deserialize column " + columnName, e, false);
					throw new PersistenceException(e);
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
