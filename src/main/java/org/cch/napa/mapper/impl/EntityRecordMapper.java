package org.cch.napa.mapper.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.SQLTypeMapper;
import org.cch.napa.entity.annotations.atk.EntityField;
import org.cch.napa.entity.annotations.atk.EntityHandler;
import org.cch.napa.mapper.RecordMapper;
import org.cch.napa.mapper.ResultSetAccessor;

/**
 * @author Christophe Champagne
 *
 */
public class EntityRecordMapper<E> implements RecordMapper<E> {
	private static Map<Class<? extends EntityDaoFactory>,
	Map<Class<?>, Map<String, ResultSetAccessor>>>resultSetGettersPerFactory
	= new Hashtable<Class<? extends EntityDaoFactory>, Map<Class<?>,Map<String,ResultSetAccessor>>>();

	
	private Class<E> entityClass;
	private EntityHandler<E> entityHandler;
	private Map<String, ResultSetAccessor> resultSetGetters;
	
	public EntityRecordMapper(Class<E> entityClass, EntityDaoFactory factory) throws AnnotationException {
		this.entityClass = entityClass;
		this.entityHandler = factory.getEntityHandler(entityClass);
		Map<Class<?>, Map<String,ResultSetAccessor>> resultSetPerPersistable = resultSetGettersPerFactory.get(factory.getClass());
		if(resultSetPerPersistable == null){
			resultSetPerPersistable = new HashMap<Class<?>, Map<String,ResultSetAccessor>>();
			resultSetGettersPerFactory.put(factory.getClass(), resultSetPerPersistable);
		} else {
			this.resultSetGetters = resultSetPerPersistable.get(entityClass);
		}
		if(this.resultSetGetters == null){
			this.resultSetGetters = getResultSetGetters(entityHandler, factory);
			resultSetPerPersistable.put(entityClass, resultSetGetters);
		}

	}
	/**
	 * @see RecordMapper#map(java.sql.ResultSet)
	 */
	public E map(ResultSet resultSet) throws PersistenceException {
		E entity = null;
		String name = null;
		try {
			entity = entityClass.newInstance();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for(int index = 1; index <=metaData.getColumnCount(); index ++){
				name = metaData.getColumnName(index);
				EntityField entityField = entityHandler.getEntityField(name);
				if(entityField != null){
					ResultSetAccessor resultSetGetter = resultSetGetters.get(entityField.getDBFieldName());
					if(resultSetGetter!=null){
						Object value = resultSetGetter.getValueFromResultSet(resultSet, entityField.getDBFieldName());
						entityField.set(entity, value);
					} else {
						System.err.println("No resultSet getter for " + name + " field");
					}
				}
				//TODO remove the else or at least make the warnings only appear once per field
				else {
					System.err.println("Cannot find entityField for DBField " + name);
				}
			}
		} catch (Exception e) {
			throw new PersistenceException("Problem while processing field " + name, e);
		}

		return entity;
	}
	
	private synchronized static <E> Map<String, ResultSetAccessor> getResultSetGetters(EntityHandler<E> entityHandler, EntityDaoFactory factory){
		Map<String, ResultSetAccessor>resultSetGetters = new HashMap<String, ResultSetAccessor>();
		SQLTypeMapper typeMapper = factory.getSqlTypeMapper();
		for(EntityField entityField:entityHandler.getEntityFields()){
			resultSetGetters.put(entityField.getDBFieldName(),
					typeMapper.getResultSetGetterFromClass(entityField.getJavaType(), entityField.getSqlType()));
		}
		return resultSetGetters;
	}

}
