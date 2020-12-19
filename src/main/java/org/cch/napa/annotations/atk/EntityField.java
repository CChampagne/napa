/**
 * 
 */
package org.cch.napa.annotations.atk;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Types;

import org.cch.napa.entity.FieldMetaData;
import org.cch.napa.annotations.DBField;
import org.cch.napa.annotations.GeneratedValue;
import org.cch.napa.annotations.generator.Generator;

/**
 * @author Christophe Champagne
 * This class represents a field. 
 * It allows to set and get value 
 * of that field to the Java 
 */
public class EntityField implements FieldMetaData {
	//Attributes
	private String DBFieldName;
	private boolean transientField;
	private boolean primaryKey;
	private boolean nullable = true;
	private boolean readOnly;
	private int sqlType = Types.NULL;
	private int size = DBField.DEFAULT;
	private int precision = DBField.DEFAULT;
	private String defaultValue;
	private Field field;
	private Method getter;
	private Method setter;
	private GeneratedValue generatedValueAnnotation;
	private Generator generator;
	
	//Constructors
	EntityField(){}
	
	public String getDBFieldName() {
		return DBFieldName;
	}
	void setDBFieldName(String dBFieldName) {
		DBFieldName = dBFieldName;
	}
	Field getField() {
		return field;
	}
	void setField(Field field) {
		this.field = field;
		if(field != null) field.setAccessible(true);
	}
	Method getGetter() {
		return getter;
	}
	void setGetter(Method getter) {
		this.getter = getter;
	}
	Method getSetter() {
		return setter;
	}
	void setSetter(Method setter) {
		this.setter = setter;
	}
	void setGenerator(Generator generator){
		this.generator = generator;
	}
	//Public attributes (read only) & methods
	/**
	 * @return the transientField
	 */
	boolean isTransientField() {
		return transientField;
	}

	/**
	 * @param transientField the transientField to set
	 */
	void setTransientField(boolean transientField) {
		this.transientField = transientField;
	}
	/**
	 * @return the sqlType
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * @param sqlType the sqlType to set
	 */
	void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	void setSize(int size) {
		this.size = size;
	}	

	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @return true if the value can be NULL
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param nullable the nullable to set
	 */
	void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return the class of the field or the getter or setter if not defined
	 */
	public Class<?> getJavaType() {
		Class<?> cls = null;
		if(field!=null){
			cls = field.getType();
		} else if(getter!=null){
			cls = getter.getReturnType();
		} else if(setter!=null){
			cls = setter.getParameterTypes()[0];
		}
		return cls;
	}



	/**
	 * @param primaryKey the primaryKey to set
	 */
	void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	/**
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the generator
	 */
	public Generator getGenerator() {
		return generator;
	}


	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the generatedValueAnnotation
	 */
	public GeneratedValue getGeneratedValueAnnotation() {
		return generatedValueAnnotation;
	}

	/**
	 * @param generatedValueAnnotation the generatedValueAnnotation to set
	 */
	void setGeneratedValueAnnotation(GeneratedValue generatedValueAnnotation) {
		this.generatedValueAnnotation = generatedValueAnnotation;
	}	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object get(Object entity) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(getter != null){
			return getter.invoke(entity);
		} else if (field != null ){
			return field.get(entity);
		}
		return null;
	}
	
	/**
	 * 
	 * @param entity
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void set(Object entity, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(setter != null){
			setter.invoke(entity, value);
		} else if (field != null ){
			field.set(entity, value);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntityField [DBFieldName=" + DBFieldName + ", transientField="
				+ transientField + ", primaryKey=" + primaryKey + ", nullable="
				+ nullable + ", sqlType=" + sqlType + ", size=" + size
				+ ", precision=" + precision + ", defaultValue=" + defaultValue
				+ "]";
	}

	/**
	 * @see FieldMetaData#getFieldName()
	 */
	public String getFieldName() {
		return getDBFieldName();
	}

}
