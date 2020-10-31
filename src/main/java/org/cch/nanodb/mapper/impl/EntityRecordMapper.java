package org.cch.nanodb.mapper.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.annotations.atk.EntityField;
import org.cch.nanodb.annotations.atk.EntityHandler;
import org.cch.nanodb.mapper.RecordMapper;
import org.cch.nanodb.mapper.ResultsetAccessor;

/**
 * @author Christophe Champagne
 *
 */
public class EntityRecordMapper<E> implements RecordMapper<E> {
	private static Map<Class<? extends EntityDaoFactory>,
	Map<Class<?>, Map<String, ResultsetAccessor>>>resultsetGettersPerFactory
	= new Hashtable<Class<? extends EntityDaoFactory>, Map<Class<?>,Map<String,ResultsetAccessor>>>();

	
	private Class<E> entityClass;
	private EntityHandler<E> entityHandler;
	private Map<String, ResultsetAccessor>resultsetGetters;
	
	public EntityRecordMapper(Class<E> entityClass, EntityDaoFactory factory) throws AnnotationException {
		this.entityClass = entityClass;
		this.entityHandler = factory.getEntityHandler(entityClass);
		Map<Class<?>, Map<String,ResultsetAccessor>> resultSetPerPersitable = resultsetGettersPerFactory.get(factory.getClass());
		if(resultSetPerPersitable == null){
			resultSetPerPersitable = new HashMap<Class<?>, Map<String,ResultsetAccessor>>();
			resultsetGettersPerFactory.put(factory.getClass(), resultSetPerPersitable);
		} else {
			this.resultsetGetters = resultSetPerPersitable.get(entityClass);
		}
		if(this.resultsetGetters == null){
			this.resultsetGetters = getResulsetGetters(entityHandler, factory);
			resultSetPerPersitable.put(entityClass, resultsetGetters);
		}

	}
	/**
	 * @see RecordMapper#map(java.sql.ResultSet)
	 */
	public E map(ResultSet resultSet) throws SQLException, PersistenceException {
		E entity = null;
		String name = null;
		try {
			entity = entityClass.newInstance();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for(int index = 1; index <=metaData.getColumnCount(); index ++){
				name = metaData.getColumnName(index);
				EntityField entityField = entityHandler.getEntityField(name);
				if(entityField != null){
					ResultsetAccessor resultsetGetter = resultsetGetters.get(entityField.getDBFieldName());
					if(resultsetGetter!=null){
						Object value = resultsetGetter.getValueFromResultSet(resultSet, entityField.getDBFieldName());
						entityField.set(entity, value);
					} else {
						TRACE.log(Severities.ERROR, "No resultset getter for " + name + " field");
					}
				}
				//TODO remove the else or at least make the warnings only appear once per field
				else {
					TRACE.log(Severities.WARNING,"Cannot find entityField for DBField " + name);
				}
			}
		} catch (Exception e) {
			TRACE.log(Severities.ERROR, "Problem while processing field " + name);
			throw new PersistenceException(e);
		}

		return entity;
	}
	
	private synchronized static <E> Map<String, ResultsetAccessor> getResulsetGetters(EntityHandler<E> entityHandler, EntityDaoFactory factory){
		Map<String, ResultsetAccessor>resultsetGetters = new HashMap<String, ResultsetAccessor>();
		SQLTypeMapper typeMapper = factory.getSqlTypeMapper();
		for(EntityField entityField:entityHandler.getEntityFields()){
			resultsetGetters.put(entityField.getDBFieldName(),
					typeMapper.getResulsetGetterFromClass(entityField.getJavaType(), entityField.getSqlType()));
		}
		return resultsetGetters;
	}

}
