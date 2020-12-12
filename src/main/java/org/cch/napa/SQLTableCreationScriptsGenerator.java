package org.cch.napa;

import java.sql.Types;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.annotations.DBField;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.annotations.atk.EntityHandler;
import org.cch.napa.annotations.atk.EntityIndex;

/**
 * @author Christophe Champagne
 *
 */
public class SQLTableCreationScriptsGenerator {
	private EntityDaoFactory factory;
	
	public SQLTableCreationScriptsGenerator(EntityDaoFactory factory) {
		this.factory = factory;
	}
	public <E> String generateCreateTable(Class<E> entityClass) throws AnnotationException{
		StringBuilder query = new StringBuilder("create table ");
		EntityHandler<E> handler = factory.getEntityHandler(entityClass);
		query.append(handler.getTableName());
		query.append(" (");
		boolean first = true;
		for(EntityField field:handler.getEntityFields()){
			if(first){
				first = false;
			} else {
				query.append(',');
			}
			query.append("\n    ");
			appendField(query, field);
		}
		if(handler.getPrimaryKey().size()>0){
			first=true;
			query.append(",\n PRIMARY KEY (");
			for(EntityField field : handler.getPrimaryKey()){
				if(first){
					first = false;
				} else {
					query.append(',');
				}
				query.append(field.getDBFieldName());
			}
			query.append(')');
		}
		query.append("\n)");
		return query.toString();
	}
	public <E> String generateDropTable(Class<E> entityClass) throws AnnotationException{
		return "drop table " + 	 factory.getEntityHandler(entityClass).getTableName();
	}
	protected void appendField(StringBuilder query, EntityField field) throws AnnotationException{
		query.append(field.getDBFieldName());
		query.append(' ');
		appendTypeName(query, field);
		if(!field.isNullable()){
			query.append(" NOT NULL");
		}
		if(!Conditions.isEmptyOrNull(field.getDefaultValue())){
			query.append(" DEFAULT ");
			query.append(field.getDefaultValue());
		}
	}
	protected void appendTypeName(StringBuilder query, EntityField field) throws AnnotationException{
		switch (field.getSqlType()){
			case Types.BIGINT:
				query.append("BIGINT");
				break;
			case Types.VARCHAR:
				query.append("VARCHAR");
				appendSize(query, field, 255);
				break;
			case Types.CHAR:
				query.append("CHAR");
				appendSize(query, field, 30);
				break;
			case Types.NUMERIC:
			case Types.DECIMAL:
				query.append("DECIMAL");
				query.append('(');
				if(field.getSize()>DBField.DEFAULT){
					query.append(field.getSize());
				} else {
					query.append(10);
				}
				query.append(',');
				if(field.getPrecision()>DBField.DEFAULT){
					query.append(field.getPrecision());
				} else {
					query.append(2);
				}
				query.append(')');
				break;
			case Types.SMALLINT:
			case Types.BOOLEAN:
				query.append("SMALLINT");
				break;
			case Types.INTEGER:
				query.append("INTEGER");
				break;
			case Types.FLOAT:
				query.append("FLOAT");
				break;
			case Types.DOUBLE:
				query.append("DOUBLE");
				break;
			case Types.BLOB:
				query.append("BLOB");
				appendSize(query, field, 10240);
				break;
			case Types.DATE:
				query.append("DATE");
				break;
			case Types.TIME:
				query.append("TIME");
				break;
			case Types.TIMESTAMP:
				query.append("TIMESTAMP");
				break;
			default:
				//Unknown type
				throw new AnnotationException("Unknown field type = " + field.getSqlType()
						+ " (field name = " + field.getFieldName() +")");
		}
	}
	private void appendSize(StringBuilder query, EntityField field,int defaultSize){
		query.append('(');
		if(field.getSize()>DBField.DEFAULT){
			query.append(field.getSize());
		} else {
			query.append(defaultSize);
		}
		query.append(')');
	}

	public  <E> String generateCanRead(Class<E> entityClass) throws AnnotationException{
		return generateCanRead(factory.getEntityHandler(entityClass).getTableName());
	}
	public String generateCanRead(String tableName) throws AnnotationException{
		String query = "SELECT 1 from " 
					+ tableName 
					+ " where 1=0";
		return query;
	}
	/**
	 * Generate statement to add a field on a table
	 * @param entityClass class of the org.cch.napa.entity representing the table
	 * @param fieldName 
	 * @return null if the field cannot be extracted from the class
	 * @throws AnnotationException
	 */
	public <E> String generateAddField(Class<E> entityClass, String fieldName) throws AnnotationException{
		EntityHandler<E> handler = factory.getEntityHandler(entityClass);
		EntityField field = handler.getEntityField(fieldName);
		StringBuilder query = null;
		if(field != null){
			query = new StringBuilder("alter table ");
			query.append(handler.getTableName());
			query.append(" add column ");
			appendField(query, field);
		}
		return (query!=null)?query.toString():null;
	}
	/**
	 * generate index
	 * @param index
	 * @return
	 * @throws AnnotationException
	 */
	public  String generateIndex(EntityIndex index) throws AnnotationException{
		StringBuilder query = new StringBuilder("create");
		if(index.isUnique()){
			query.append(" unique");
		}
		query.append(" index ");
		query.append(index.getName());
		query.append(" on ");
		query.append(index.getTableName());
		query.append('(');
		boolean first= true;
		for(EntityField field : index.getFields()){
			if(!first){
				query.append(", ");
			} else {
				first = false;
			}
			query.append(field.getDBFieldName());
		}
		query.append(')');
		
		return query.toString();
	}
}
