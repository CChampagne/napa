/**
 * by Christophe Champagne
 */
package nanodb.impl.sqlite;

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

import nanodb.exceptions.PersistenceException;
import nanodb.impl.AbstractResultsetGetter;
import nanodb.impl.DefaultSQLTypeMapper;
import nanodb.mapper.ResultsetAccessor;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;
/**
 * @author Christophe Champagne
 *
 */
public class SQLiteTypeMapper extends DefaultSQLTypeMapper {
	private static Trace TRACE = Trace.getInstance(SQLiteTypeMapper.class.getName());
	
	/**
	 * @see nanodb.SQLTypeMapper#getResulsetGetterFromClass(java.lang.Class, int)
	 */
	public  ResultsetAccessor getResulsetGetterFromClass(Class<?>cls, int sqlType){
		ResultsetAccessor resulsetGetter = null;
		if (cls.equals(BigDecimal.class)){
			resulsetGetter =  new AbstractResultsetGetter(){
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
			resulsetGetter = super.getResulsetGetterFromClass(cls, sqlType);
		} 
		return resulsetGetter;
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
			TRACE.logException(Severities.ERROR, e);
			throw new PersistenceException();
		} catch (Exception e) {
			TRACE.logException(Severities.FATAL, e);
			throw new PersistenceException();
		} finally {
	        try {
	        	//bos closed when oos is closed
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
					serializable = deserializeBytes(resultSet.getBytes(columnName));
				} catch (IOException e) {
					TRACE.logException(Severities.ERROR,"Could not deserialize column " + columnName, e, false);
					throw new PersistenceException(e);
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
