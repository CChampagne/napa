/**
 * 
 */
package com.ibm.next.mam.persistence.annotations.atk;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.SQLTypeMapper;
import com.ibm.next.mam.persistence.annotations.DBField;
import com.ibm.next.mam.persistence.annotations.Entity;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.Id;
import com.ibm.next.mam.persistence.annotations.Index;
import com.ibm.next.mam.persistence.annotations.Indexed;
import com.ibm.next.mam.persistence.annotations.Indexes;
import com.ibm.next.mam.persistence.annotations.NotNull;
import com.ibm.next.mam.persistence.annotations.Transient;
import com.ibm.next.mam.persistence.annotations.UniqueIndexed;
import com.ibm.next.mam.persistence.annotations.generator.Generator;
import com.ibm.next.mam.persistence.annotations.generator.GeneratorFactory;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.ibm.next.mam.util.Conditions;
import com.ibm.next.mam.util.StringUtil;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;

/**
 * @author GII561
 * Class intended to facilitate the mapping between the persistent POJOs and the corresponding database table.
 * It retrieves the metadata necessary for persistence 
 */
public class EntityHandler<T extends Persistable>{
	private static Trace TRACE = Trace.getInstance(EntityHandler.class.getName());
	
	//attributes
	//|-inner
	private Class<T> entityClass;
	private Map<String, EntityField> fieldsByName = new HashMap<String, EntityField>();
	private Map<String, EntityIndex> indexes = new HashMap<String, EntityIndex>();
	private EntityDaoFactory factory;
	//Used to detect indexes that imply transient field(s)
	//once the field is determined as transient, we can use this to check if it is implied in an inddex
	//if so ->ERROR
	private Set<String> fieldsWithIndex = new HashSet<String>();
	//|-public
	private List<EntityField> entityFields = new ArrayList<EntityField>();
	private List<EntityField> primaryKey = new ArrayList<EntityField>();
	private String tableName; 
	
	//constructor
	public EntityHandler(Class<T> entityClass,EntityDaoFactory factory) throws AnnotationException{
		this.entityClass = entityClass;
		this.factory = factory;

		analyseEntity();
	}


	public List<EntityField> getEntityFields() {
		return entityFields;
	}
	public List<EntityField> getPrimaryKey() {
		return primaryKey;
	}
	public String getTableName() {
		return tableName;
	}
	public Collection<EntityIndex> getIndexes(){
		return indexes.values();
	}
	public EntityField getEntityField(String dbFieldName){
		return fieldsByName.get(dbFieldName.trim().toUpperCase());
	}	
	//Inner	
	private void putEntityField(String dbFieldName, EntityField entityField){
		fieldsByName.put(dbFieldName.trim().toUpperCase(), entityField);
	}
	
	/**
	 * Analyses the entity class in order to get the meta data (FieldName, type...)
	 * necessary for persistence
	 * @throws AnnotationException 
	 */
	//TODO consider to analyze ancestors and implemented interfaces
	private void analyseEntity() throws AnnotationException{
		SQLTypeMapper sqlTypeMapper = factory.getSqlTypeMapper();
		this.tableName = retrieveTableName();
		//will we map all fields?
		boolean mapAllFieldsToDB = true;
		if( entityClass.isAnnotationPresent(Entity.class)){
			Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
			mapAllFieldsToDB = entityAnnotation.considerAllAttributesAsDBFields();
		}
		//Check the fields first
		//	check public fields
		Field[] fields = entityClass.getFields();
		analyseFields(fields, mapAllFieldsToDB);

		//check all other fields
		fields = entityClass.getDeclaredFields();
		analyseFields(fields, mapAllFieldsToDB);
		
		//Check the Methods (getters and setters)
		Method[] methods = entityClass.getMethods();
		analyseMethods(methods, mapAllFieldsToDB);
		
		//remove the transient elements and create the primary keys list
		//And the fields that could not work correctly
		this.fieldsByName.clear();
		for(Iterator<EntityField>iter = this.entityFields.iterator(); iter.hasNext();){
			EntityField entityField = iter.next();
			if(entityField.isTransientField()){
				if(entityField.isPrimaryKey() || !entityField.isNullable()){
					TRACE.log(Severities.ERROR, "The element " + entityField.getDBFieldName() +  " is marked as transient but has @Id or @DbField annotation");
					throw new AnnotationException();
				}
				if(fieldsWithIndex.contains(entityField.getDBFieldName())){
					TRACE.log(Severities.ERROR, "The element " + entityField.getDBFieldName() +  " is marked as transient but is implied in an index!");
					throw new AnnotationException();
				}
				iter.remove();
//				this.fieldsByName.remove(entityField.getDBFieldName());
			} else {
				if(entityField.getField() == null && (entityField.getSetter() == null || entityField.getGetter() == null)){
					Field field = lookupFieldInAncestors(entityField.getFieldName(), entityField.getDBFieldName());
					if(field != null){
						entityField.setField(field);//TODO analyse annotations by try to not override existing...
					}
				}
				if(entityField.getJavaType().isPrimitive()){
					entityField.setNullable(false);
					//Set a default value to allow adding new not null field
					//TODO What about other non-nullable fields?
					//TODO Might not really be a good solution because one might not want to have default value
					//+ quite specific to SQLite
					//-> should consider more general mechanism with special annotation or annotation field
					if(entityField.getDefaultValue()==null){
						entityField.setDefaultValue("0");
					}
				}
				if(entityField.isPrimaryKey()){
					this.primaryKey.add(entityField);
				}
				if(entityField.getSqlType() == Types.NULL){
					entityField.setSqlType(sqlTypeMapper.getSqlTypeFromClass(entityField.getJavaType()));
				}
				if(entityField.getSize()==DBField.DEFAULT){
					entityField.setSize(sqlTypeMapper.getSizeFromType(entityField.getSqlType()));
				}
				if(entityField.getPrecision()==DBField.DEFAULT){
					entityField.setPrecision(sqlTypeMapper.getPrecisionFromType(entityField.getSqlType()));					
				}
				if(entityField.getGeneratedValueAnnotation() != null){
					if(entityField.getJavaType().isPrimitive() && entityField.getGeneratedValueAnnotation().onlyGenerateWhenNull()){
						TRACE.log(Severities.ERROR,"It seems that the field -" + entityField.getDBFieldName() + 
								"- is primitive while the generator should only work for null values. This is inconsistent!");
						throw new AnnotationException(AnnotationException.ERR_INVALID_ANNOTATION);
					}
					try {
						Generator generator = GeneratorFactory.getGenerator(entityField, this);
						entityField.setGenerator(generator);
					} catch (PersistenceException ex) {
						throw new AnnotationFormatError(ex);
					}
				}
				putEntityField(entityField.getDBFieldName(), entityField);
			}
		}
		//Create indexes from class annotation @Indexes
		//Done in the end in order to have all entityFields defined
		if(entityClass.isAnnotationPresent(Indexes.class)){
			Indexes indexesAnnotation = entityClass.getAnnotation(Indexes.class);
			for(Index indexAnnotation : indexesAnnotation.indexes()){
				createOrUpdateIndex(indexAnnotation.name(), indexAnnotation.unique(), indexAnnotation.fields());
			}
		}
	}
	private void analyseFields(Field[] fields, boolean mapAllFieldsToDB) throws AnnotationException{
		for(Field field: fields){
			if(mapAllFieldsToDB || isDbField(field)||isId(field)){
				String name = field.getName();
				//TODO should we check if there is a corresponding getter or setter or if the attribute is public?
				EntityField entityField = analyseElement(field, name);
				if(entityField.getField()==null){
					entityField.setField(field);
				} else if(!entityField.getField().equals(field)){
					throw new AnnotationException(AnnotationException.ERR_DUPLICATED_FIELD_DEFINITION, Severities.FATAL);
				}
				if(Modifier.isFinal(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers())	
						|| Modifier.isTransient(field.getModifiers())	
						){
					entityField.setTransientField(true);
				}
			}
		}
	}
	private void analyseMethods(Method[] methods, boolean mapAllFieldsToDB) throws AnnotationException{
		for(Method method: methods){
//			if(!Modifier.isStatic(method.getModifiers()) && method.isAccessible() && (mapAllFieldsToDB || isDbField(method)||isId(method))){
			if(!Modifier.isStatic(method.getModifiers()) && !Modifier.isNative(method.getModifiers()) && !method.isSynthetic() 
					&& (mapAllFieldsToDB || isDbField(method)||isId(method))){
				String name = toFieldName(method.getName());
				if(isGetter(method) || isSetter(method)){
					EntityField entityField = analyseElement(method, name);
					//if it is a getter or a setter
					if(isGetter(method)){
						if(entityField.getGetter()==null){
							entityField.setGetter(method);
						} else {
							TRACE.log(Severities.ERROR, "The getter " + method.getName() +  " corresponds to a field having already a getter defined");
							throw new AnnotationException(AnnotationException.ERR_DUPLICATED_FIELD_DEFINITION, Severities.FATAL);
						}
					} else {
						if(entityField.getSetter()==null){
							entityField.setSetter(method);
						} else {
							TRACE.log(Severities.ERROR, "The setter " + method.getName()  +  " corresponds to a field having already a setter defined");
							throw new AnnotationException(AnnotationException.ERR_DUPLICATED_FIELD_DEFINITION, Severities.FATAL);
						}
					}
				} else if(isDbField(method) || isId(method) || isNotNull(method)){
					TRACE.log(Severities.ERROR, "The method " + method.getName() +  " is marked is not a getter or a setter but has @Id or @DbField annotation");
					throw new AnnotationException();
				}
			}
		}

	}
	private Field lookupFieldInAncestors(String fieldName, String dbFieldName){
		Field ancestorField = null;
		Class<? super T> currentClass = entityClass.getSuperclass();
		while(ancestorField == null && currentClass != null && !currentClass.equals(Object.class)){
			try {
				ancestorField=currentClass.getDeclaredField(fieldName);
			} catch (SecurityException e) {
				//
			} catch (NoSuchFieldException e) {
				//
				if(dbFieldName != null){
					for(Field field : currentClass.getDeclaredFields()){
						if(isDbField(field)){
							DBField dbField = field.getAnnotation(DBField.class);
							if(dbFieldName.equalsIgnoreCase(dbField.name())){
								return field;
							}
						}
					}
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return ancestorField;
	}
	private boolean isSetter(Method method){
		return method.getName().startsWith("set") 
				&& !method.isVarArgs()
				&& method.getParameterTypes().length == 1;
	}
	private boolean isGetter(Method method){
		return (method.getName().startsWith("get")  || method.getName().startsWith("is"))
				&& !method.isVarArgs()
				&& method.getParameterTypes().length == 0
				&& !method.getReturnType().equals(Void.TYPE);
	}

	private EntityField analyseElement(AnnotatedElement element,String name) throws AnnotationException{
		EntityField entityField = getEntityField(name);
		DBField dbField = null;
		boolean isDBField = isDbField(element);
		boolean found = true;
		
		if(isDBField){
			dbField = element.getAnnotation(DBField.class);
			EntityField entityFieldByDbName = getEntityField(dbField.name());
			if(entityField == null){
				entityField = entityFieldByDbName; 
			} else if(entityFieldByDbName != null && entityFieldByDbName != entityField){
				//We have got a problem because it seem it was defined twice at least on different fields
				TRACE.log(Severities.ERROR,"It seems that the same db column -" + dbField.name() + "- corresponds to two java fields.");
				throw new AnnotationException(AnnotationException.ERR_DUPLICATED_FIELD_DEFINITION);
			} else if(entityFieldByDbName == null && !name.equalsIgnoreCase(entityField.getDBFieldName())){
				//Now we have two DB column for the same java name.
				TRACE.log(Severities.ERROR,"It seems that the same java field -" + name + "- corresponds to two db columns : "
						+ entityField.getDBFieldName() + " and " + dbField.name());
				throw new AnnotationException(AnnotationException.ERR_DUPLICATED_FIELD_DEFINITION);
			}
		}
		if(entityField == null){
			entityField = new EntityField();
			found = false;
		}
		if(isDBField){
			entityField.setSqlType(dbField.sqlType());
			entityField.setDBFieldName(dbField.name());
			if(dbField.isNullable()){
				entityField.setNullable(true);
			}
			if(dbField.isPrimaryKey()){
				entityField.setPrimaryKey(true);
				entityField.setNullable(false);
			}
			if(dbField.size()>DBField.DEFAULT){
				entityField.setSize(dbField.size());
			}
			if(dbField.precision()>DBField.DEFAULT){
				entityField.setPrecision(dbField.precision());
			}
			if(!Conditions.isEmptyOrNull(dbField.defaultValue())){
				entityField.setDefaultValue(dbField.defaultValue());
			}
			if(dbField.isReadOnly()){
				entityField.setReadOnly(true);
			}
		}
		if(isId(element)){
			entityField.setPrimaryKey(true);
			entityField.setNullable(false);
		}
		if(isNotNull(element)){
			entityField.setNullable(false);
		}
		if(Conditions.isEmptyOrNull(entityField.getDBFieldName())){
			entityField.setDBFieldName(name);
		}
		//Checks if transient
		// currently the modifier transient makes the field transient for the persistence
		///TODO reconsider if taking the modifier in account is a good thing
		Member member = (Member)element;
		if(isTransient(element) || Modifier.isTransient(member.getModifiers())){
			entityField.setTransientField(true);
		}
		if(isGenerated(element)){
			entityField.setGeneratedValueAnnotation(element.getAnnotation(GeneratedValue.class));
		}
		if(!found){
			this.entityFields.add(entityField);
		}
		putEntityField(name, entityField);
		if(isDBField){
			putEntityField(entityField.getDBFieldName(), entityField);
		}
		if(element.isAnnotationPresent(Indexed.class)){
			Indexed indexedAnnotation = element.getAnnotation(Indexed.class);
			if(indexedAnnotation.names().length==0){
				createOrUpdateIndex("", false, entityField.getDBFieldName());
			} else {
				for(String indexName : indexedAnnotation.names()){
					createOrUpdateIndex(indexName, false, entityField.getDBFieldName());
				}
			}
		}
		if(element.isAnnotationPresent(UniqueIndexed.class)){
			UniqueIndexed indexedAnnotation = element.getAnnotation(UniqueIndexed.class);
			if(indexedAnnotation.names().length==0){
				createOrUpdateIndex("", true, entityField.getDBFieldName());
			} else {
				for(String indexName : indexedAnnotation.names()){
					createOrUpdateIndex(indexName, true, entityField.getDBFieldName());
				}
			}
		}
		return entityField;
	}
	private boolean isId(AnnotatedElement element){
		return element.isAnnotationPresent(Id.class);
	}
	private boolean isDbField(AnnotatedElement element){
		return element.isAnnotationPresent(DBField.class);
	}
	private boolean isTransient(AnnotatedElement element){
		return element.isAnnotationPresent(Transient.class);
	}
	private boolean isNotNull(AnnotatedElement element){
		return element.isAnnotationPresent(NotNull.class);
	}
	private boolean isGenerated(AnnotatedElement element){
		return element.isAnnotationPresent(GeneratedValue.class);
	}
	protected String toFieldName(String getterOrSetterName){
		StringBuilder name = new StringBuilder(getterOrSetterName);
		if(getterOrSetterName.startsWith("get") ||
				getterOrSetterName.startsWith("set")){
			name = name.delete(0,3);
			name.setCharAt(0, Character.toLowerCase(name.charAt(0)));
		}else if(getterOrSetterName.startsWith("is")){
			name = name.delete(0,2);
			name.setCharAt(0, Character.toLowerCase(name.charAt(0)));			
		} else {
			return null;
		}
		return name.toString();
	}
	private String retrieveTableName(){
		String table = entityClass.getSimpleName();
		if( entityClass.isAnnotationPresent(Entity.class)){
			Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
			if(entityAnnotation.table().length()>0){
				table = entityAnnotation.table();
			}
		}
		return table;
	}
	private void createOrUpdateIndex(String nameFromAnnotation, boolean unique, String...fieldNames) throws AnnotationException{
		String name = getFinalIndexName(nameFromAnnotation, unique, fieldNames);
		EntityIndex index = indexes.get(name);
		if(index == null){
			index = new EntityIndex(name);
			index.setTableName(this.tableName);
			indexes.put(name, index);
		} else if(index.isUnique() != unique){
			TRACE.log(Severities.ERROR,"It seems that the index " + name + " is inconsistently declared as both unique and non unique");
			throw new AnnotationException(AnnotationException.ERR_INVALID_ANNOTATION);
		}
		for(String fieldName: fieldNames){
			EntityField field = getEntityField(fieldName);
			if(field == null){
				TRACE.log(Severities.ERROR,"It seems that the field -" + fieldName + 
						" declared in index " + name + " is not a field for table " + this.tableName);
				throw new AnnotationException(AnnotationException.ERR_INVALID_ANNOTATION);
			}
			index.addField(field);
			fieldsWithIndex.add(fieldName);
		}
	}
	private String getFinalIndexName(String nameFromAnnotation, boolean unique, String...fieldNames){
		String name = nameFromAnnotation;
		if(nameFromAnnotation.length() == 0){
			name = (unique?"uidx_":"idx_")
					+ "_" + this.tableName
					+ StringUtil.join(fieldNames, "_");
		}
		return name;
	}
}
