package org.cch.napa;

import java.util.List;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.annotations.atk.EntityHandler;

/**
 * @author Christophe Champagne
 *
 */
public class SQLGenerator<T> {
	
	//
	private String selectAll;
	private String select;
	private String update;
	private String insert;
	private String delete;
	private String deleteAll;
	private String count;
	private EntityHandler<T> handler;
	
	public SQLGenerator(Class<T> entityClass, EntityDaoFactory factory) throws AnnotationException{
		handler = factory.getEntityHandler(entityClass);
	}
	/**
	 * 
	 * @return
	 */
	public String createSelectAll(){
		if(selectAll == null){
			StringBuffer query = new StringBuffer("Select * from ");
			query.append(handler.getTableName());
			selectAll = query.toString();
		}
		return selectAll;
	}
	
	/**
	 * 
	 * @return
	 */
	public String createSelect(){
		if(select == null){
			StringBuffer query = new StringBuffer(createSelectAll());
			appendWhereClause(query);
			select = query.toString();
		}
		return select;
	}
	/**
	 * 
	 * @return
	 */
	public String createInsert(){
		if(insert == null){
			StringBuffer query = new StringBuffer("insert into ");
			query.append(handler.getTableName());
			query.append(" (");
			appendFieldsList(query, ", ", handler.getEntityFields());
			query.append(") values (");
			appendQuestionMarkList(query, handler.getEntityFields().size());
			query.append(')');
			insert = query.toString();
		}
		return insert;
	}
	/**
	 * 
	 * @return
	 */
	public String createUpdate(){
		if(update == null){
			StringBuffer query = new StringBuffer("update ");
			query.append(handler.getTableName());
			query.append(" set ");
			appendUpdateFields(query, handler.getEntityFields());
			appendWhereClause(query);
			update = query.toString();
		}
		return update;
	}
	/**
	 * 
	 * @return
	 */
	public String createDelete(){
		if(delete == null){
			StringBuffer query = new StringBuffer("delete from ");
			query.append(handler.getTableName());
			appendWhereClause(query);
			delete = query.toString();
		}
		return delete;
	}
	/**
	 * 
	 * @return
	 */
	public String createDeleteAll(){
		if(deleteAll == null){
			StringBuffer query = new StringBuffer("delete from ");
			query.append(handler.getTableName());
			deleteAll = query.toString();
		}
		return deleteAll;
	}
	/**
	 * 
	 * @return
	 */
	public String createCount(){
		if(count == null){
			StringBuffer query = new StringBuffer("select count(*) from ");
			query.append(handler.getTableName());
			count = query.toString();
		}
		return count;
	}
	//----------------------------------------------------------------------------------------------------------------------------
	
	private void appendWhereClause(StringBuffer query){
		if(handler.getPrimaryKey().size()>0){
			query.append(" where ");
			int index = 1;
			for(EntityField field: handler.getPrimaryKey()){
				if(index > 1){
					query.append(" and ");
				}
				query.append(field.getDBFieldName());
				query.append(" = ?");
				index++;
			}
		}
	}
	private void appendUpdateFields(StringBuffer query, List<EntityField> fields){
		if(fields.size()>0){
			int index = 1;
			for(EntityField field: fields){
				if(!field.isPrimaryKey()){
					if(index > 1){
						query.append(", ");
					}
					query.append(field.getDBFieldName());
					query.append(" = ?");
					index++;
				}
			}
		}
	}
	private void appendQuestionMarkList(StringBuffer query, int count){
		if(count>0){
			query.append('?');
			for(int i=1; i<count; i++){
				query.append(", ?");
			}
		}
	}
	private void appendFieldsList(StringBuffer query, String separator, List<EntityField> fields){
		if(fields != null && fields.size()>0){
			query.append(fields.get(0).getDBFieldName());
			for(int i=1; i<fields.size(); i++){
				query.append(separator);
				query.append(fields.get(i).getDBFieldName());
			}
		}	
	}

}
