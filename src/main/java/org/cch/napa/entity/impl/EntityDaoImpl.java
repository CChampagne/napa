package org.cch.napa.entity.impl;

import org.cch.napa.*;
import org.cch.napa.annotations.DBField;
import org.cch.napa.annotations.GeneratedValue;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.annotations.atk.EntityHandler;
import org.cch.napa.annotations.generator.impl.SingleValueMapper;
import org.cch.napa.entity.EntityDao;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.LazyResultSetIterable;
import org.cch.napa.entity.SQLGenerator;
import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.exceptions.SQLException;
import org.cch.napa.mapper.RecordMapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christophe Champagne
 *
 */
public class EntityDaoImpl<E> implements EntityDao<E> {
	
	private JdbcDao jdbcDao;
	private Class<E> entityClass;
	private SQLGenerator<E> sqlGenerator;
	private EntityHandler<E> entityHandler;
	private RecordMapper<E> recordMapper;
	private EntityDaoFactory factory;
	private enum Operation{INSERT, UPDATE, SELECT, DELETE}
	private String isPresentQuery;
	private String countQuery;
	
	
	public EntityDaoImpl(Class<E> entityClass, EntityDaoFactory factory) throws AnnotationException{
		this(entityClass, factory, factory.getDefaultConnectionProvider());
	}
	public EntityDaoImpl(Class<E> entityClass, EntityDaoFactory factory, ConnectionProvider connectionProvider) throws AnnotationException{
		this.factory = factory;
		this.entityClass = entityClass;
		this.sqlGenerator = factory.getSQLGenerator(entityClass);
		this.entityHandler = factory.getEntityHandler(entityClass);
		this.recordMapper = factory.getEntityRecordMapper(entityClass);
		this.jdbcDao = factory.getJdbcDao(connectionProvider);
		this.isPresentQuery = this.sqlGenerator.createSelect().replace("*", "count(*)");
		this.countQuery = this.sqlGenerator.createCount();
	}

	/**
	 * @see EntityDao#selectAll()
	 */
	public List<E> selectAll() throws PersistenceException {
		return jdbcDao.select(sqlGenerator.createSelectAll(), recordMapper);
	}
	/***
	 * @see EntityDao#select(E)
	 */
	public E select(E entityParameter) throws PersistenceException,
			SQLException {
		E retrievedEntity = null;
		String query = sqlGenerator.createSelect();
		List<E> retrievedEntities =  jdbcDao.select(query, recordMapper, getPrimaryKeyValues(entityParameter, Operation.SELECT));
		if(retrievedEntities != null && retrievedEntities.size() == 1){
			retrievedEntity = retrievedEntities.get(0);
		} else if(retrievedEntities != null && retrievedEntities.size() > 1){
			int nbr = retrievedEntities.size();
			throw new PersistenceException("Retrieved an incorrect number of items (expected = 1, obtained = " + nbr + ")");
		} 
		return retrievedEntity;
	}

	/**
	 * @see EntityDao#lazilySelectAll()
	 */
	public LazyResultSetIterable<E> lazilySelectAll() throws PersistenceException {
		return  jdbcDao.lazilySelect(sqlGenerator.createSelectAll(), recordMapper);
	}

	/**
	 * @see EntityDao#lazilySelect(String, Object...)
	 */
	public LazyResultSetIterable<E> lazilySelect(String query, Object... parameters) throws PersistenceException {
		return jdbcDao.lazilySelect(query, recordMapper, parameters);
	}

	/**
	 * @see EntityDao#select(java.lang.String, java.lang.Object[])
	 */
	public List<E> select(String query, Object... parameters)
			throws PersistenceException, SQLException {
		return jdbcDao.select(query, recordMapper, parameters);
	}
	/**)
	 * @see EntityDao#insert(E)
	 */
	public void insert(E entity) throws PersistenceException {
		String query = sqlGenerator.createInsert();
		jdbcDao.executeUpdate(query, getValues(entity, Operation.INSERT));
	}

	/**
	 * @see EntityDao#update(E)
	 */
	public void update(E entity) throws PersistenceException {
		String query = sqlGenerator.createUpdate();
		List<EntityField> fields = new ArrayList<EntityField>(entityHandler.getEntityFields().size());
		for(EntityField entityField : entityHandler.getEntityFields()){
			if(!entityField.isPrimaryKey()){
				fields.add(entityField);
			}
		}
		fields.addAll(entityHandler.getPrimaryKey());
		jdbcDao.executeUpdate(query, getValues(entity, fields, Operation.UPDATE));
	}

	/**
	 * @see EntityDao#persist(E)
	 */
	public void persist(E entity) throws PersistenceException {
		if(recordExists(entity)){
			update(entity);
		} else {
			insert(entity);
		}
	}

	/**
	 * @see EntityDao#delete(E)
	 */
	public void delete(E entity) throws PersistenceException {
		String query = sqlGenerator.createDelete();
		jdbcDao.executeUpdate(query, getPrimaryKeyValues(entity, Operation.DELETE));
	}
	/**
	 * @see EntityDao#recordExists(E)
	 */
	public boolean recordExists(E entity) throws PersistenceException {
		List<Long> values= jdbcDao.select(isPresentQuery, new SingleValueMapper(), getPrimaryKeyValues(entity, Operation.SELECT));
		long count = values.get(0);
		return count > 0;
	}
	/**
	 * @see EntityDao#count()
	 */
	public long count() throws PersistenceException {
		return count(countQuery);
	}
	/**
	 * @see EntityDao#count(java.lang.String, java.lang.Object[])
	 */
	public long count(String query, Object... parameters) throws PersistenceException {
		List<Long> values = jdbcDao.select(query, new SingleValueMapper(), parameters);
		if(values != null && !values.isEmpty() && values.get(0)!=null){
			return values.get(0).longValue();
		}
		return 0;
	}
	/**
	 * @see EntityDao#getEntityClass()
	 */
	public Class<? extends E> getEntityClass() {
		return entityClass;
	}
	//------------------------------------------------------------------------------------------------------------------------------
	private Object[] getPrimaryKeyValues(E entity, Operation operation) throws PersistenceException{
		return getValues(entity, entityHandler.getPrimaryKey(), operation);
	}

	private Object[] getValues(E entity, Operation operation) throws PersistenceException{
		return getValues(entity, entityHandler.getEntityFields(), operation);
	}
	private Object[] getValues(E entity, List<EntityField> entityFields, Operation operation) throws PersistenceException{
		Object[] fields;
		try {
			fields = new Object[entityFields.size()];
			int i=0;
			for(EntityField field: entityFields){
				Object val = field.get(entity);
				if(field.getGenerator()!=null){
					GeneratedValue annotation = field.getGeneratedValueAnnotation();
					if(val == null || !annotation.onlyGenerateWhenNull() || field.getJavaType().isPrimitive()){
						if(operation == Operation.INSERT 
								|| (operation == Operation.UPDATE && annotation.generateAlsoOnUpdate())){
							field.getGenerator().setFactory(this.factory);
							val = field.getGenerator().getNextValue();
							//Commit on the org.cch.napa.entity
							//TODO do this AFTER DB commit in order to keep data consistent
							field.set(entity, val);
						}
					}
				}
				fields[i++] = val;
			}
		} catch (PersistenceException e) {
			throw e;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
		return fields;
	}
	protected void checkValue(Object val, EntityField field) throws PersistenceException{
		if(val==null){
			if(!field.isNullable()){
				throw new PersistenceException("The value of the non nullable field " + field.getDBFieldName() + " is precisely NULL");
			}
			val = new SQLNull(field.getSqlType());
		} else if(field.getSize() != DBField.DEFAULT ){
			if(field.getSqlType() == Types.DECIMAL || field.getSqlType() == Types.NUMERIC){
				Number num = (Number)val;
				String intVal = String.valueOf(num.longValue());
				if(intVal.length()>field.getSize()){
					throw new PersistenceException("It seems that the value of "
							+ field.getDBFieldName() + " exceeds the size of the field in DB : " + val);
				}
				MathContext mc = new MathContext(field.getPrecision()+intVal.length(), RoundingMode.CEILING);
				val = new BigDecimal(num.doubleValue(), mc);
			} else if (val instanceof String){
				if(val.toString().length()>field.getSize()){
					throw new PersistenceException("It seems that the value of "
							+ field.getDBFieldName() + " exceeds the size of the field in DB : " + val);
				}
			}
		}

	}
}
