/**
 * 
 */
package org.cch.napa.annotations.atk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author Christophe Champagne
 *
 */
public class EntityIndex {
	private String name;
	private boolean unique;
	private Collection<EntityField>fields = new LinkedHashSet<EntityField>();
	private String tableName;
	/**
	 * 
	 */
	EntityIndex(String name) {
		this.name = name;
	}
	/**
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * @param unique the unique to set
	 */
	void setUnique(boolean unique) {
		this.unique = unique;
	}
	/**
	 * @return the fields
	 */
	public Collection<EntityField> getFields() {
		return new ArrayList<EntityField>(fields);
	}
	/**
	 * @param field the fields to set
	 */
	void addField(EntityField field) {
		this.fields.add(field);
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
//	/**
//	 * 
//	 * @param name
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}

}
