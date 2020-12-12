package org.cch.napa;

/**
 * @author Christophe Champagne
 * This class represents a field's metadata. 
 */
public interface FieldMetaData {
	
	public String getFieldName();
	/**
	 * @return the sqlType
	 */
	public int getSqlType();


	/**
	 * @return the size
	 */
	public int getSize();


	/**
	 * @return the precision
	 */
	public int getPrecision();


	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue();


	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly();
	
	/**
	 * 
	 * @return true if nullable
	 */
	public boolean isNullable();



}
