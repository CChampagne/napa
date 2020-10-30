/**
 * by Christophe Champagne
 */
package nanodb.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.exceptions.SQLException;
import nanodb.ConnectionProvider;
import nanodb.ConnectionProviderHelper;
import nanodb.EntityDao;
import nanodb.EntityDaoFactory;
import nanodb.JdbcDao;
import nanodb.SQLGenerator;
import nanodb.SQLNull;
import nanodb.annotations.DBField;
import nanodb.annotations.GeneratedValue;
import nanodb.annotations.atk.EntityField;
import nanodb.annotations.atk.EntityHandler;
import nanodb.annotations.generator.impl.SingleValueMapper;
import nanodb.entity.Persistable;
import nanodb.mapper.RecordMapper;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;

/**
 * @author Christophe Champagne
 *
 */
public class EntityDaoImpl<E extends Persistable> implements EntityDao<E>{
	private static Trace TRACE = Trace.getInstance(EntityDaoImpl.class.getName());
	
	private JdbcDao jdbcDao;
	private Class<E> entityClass;
	private SQLGenerator<E> sqlGenerator;
	private EntityHandler<E> entityHandler;
	private RecordMapper<E> recordMapper;
	private EntityDaoFactory factory;
	private enum Operation{INSERT, UPDATE, SELECT, DELETE};
	private String isPresentQuery;
	private String countQuery;
	
	
	public EntityDaoImpl(Class<E> entityClass, EntityDaoFactory factory) throws AnnotationException{
		this(entityClass, factory, ConnectionProviderHelper.getConnectionProvider(factory.getClass()));
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
	 * @see nanodb.EntityDao#selectAll(nanodb.entity.Persistable)
	 */
	public List<E> selectAll() throws PersistenceException,	SQLException {
		return jdbcDao.select(sqlGenerator.createSelectAll(), recordMapper);
	}
	/***
	 * @see nanodb.EntityDao#select(nanodb.entity.Persistable)
	 */
	public E select(E entityParameter) throws PersistenceException,
			SQLException {
		E retrievedEntity = null;
		String query = sqlGenerator.createSelect();
		List<E> retrievedEntities =  jdbcDao.select(query, recordMapper, getPrimaryKeyValues(entityParameter, Operation.SELECT));
		if(retrievedEntities != null && retrievedEntities.size() == 1){
			retrievedEntity = retrievedEntities.get(0);
		} else if(retrievedEntities != null && retrievedEntities.size() > 1){
			int nbr = 0;
			if(retrievedEntities != null){
				nbr = retrievedEntities.size();
			}
			TRACE.log(Severities.ERROR,"Retrieved an incorrect number of items (expected = 1, obtained = " + nbr + ")");
			throw new PersistenceException("P003", Severities.ERROR);
		} 
		return retrievedEntity;
	}

	/**
	 * @see nanodb.EntityDao#select(java.lang.String, java.lang.Object[])
	 */
	public List<E> select(String query, Object... parameters)
			throws PersistenceException, SQLException {
		return jdbcDao.select(query, recordMapper, parameters);
	}
	/**)
	 * @see nanodb.EntityDao#insert(nanodb.entity.Persistable)
	 */
	public void insert(E entity) throws PersistenceException, SQLException {
		String query = sqlGenerator.createInsert();
		jdbcDao.executeUpdate(query, getValues(entity, Operation.INSERT));
	}

	/**
	 * @see nanodb.EntityDao#update(nanodb.entity.Persistable)
	 */
	public void update(E entity) throws PersistenceException, SQLException {
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
	 * @see nanodb.EntityDao#persist(nanodb.entity.Persistable)
	 */
	public void persist(E entity) throws PersistenceException, SQLException {
		if(recordExists(entity)){
			update(entity);
		} else {
			insert(entity);
		}
	}

	/**
	 * @see nanodb.EntityDao#delete(nanodb.entity.Persistable)
	 */
	public void delete(E entity) throws PersistenceException, SQLException {
		String query = sqlGenerator.createDelete();
		jdbcDao.executeUpdate(query, getPrimaryKeyValues(entity, Operation.DELETE));
	}
	/**
	 * @see nanodb.EntityDao#recordExists(nanodb.entity.Persistable)
	 */
	public boolean recordExists(E entity) throws PersistenceException, SQLException {		
		List<Long> values= jdbcDao.select(isPresentQuery, new SingleValueMapper(), getPrimaryKeyValues(entity, Operation.SELECT));
		long count = values.get(0);
		return count > 0;
	}
	/**
	 * @see nanodb.EntityDao#count()
	 */
	public long count() throws PersistenceException, SQLException {
		return count(countQuery);
	}
	/**
	 * @see nanodb.EntityDao#count(java.lang.String, java.lang.Object[])
	 */
	public long count(String query, Object... parameters) throws PersistenceException, SQLException {
		List<Long> values = jdbcDao.select(query, new SingleValueMapper(), parameters);
		if(values != null && !values.isEmpty() && values.get(0)!=null){
			return values.get(0).longValue();
		}
		return 0;
	}
	/**
	 * @see nanodb.EntityDao#getEntityClass()
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
							//Commit on the entity
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
			//TODO add correct error
			throw new PersistenceException(e);
		}
		return fields;
	}
	protected void checkValue(Object val, EntityField field) throws PersistenceException{
		if(val==null){
			if(!field.isNullable()){
				TRACE.log(Severities.ERROR, "The value of the non nullable field "	+ field.getDBFieldName() + " is NULL");
				throw new PersistenceException();
			}
			val = new SQLNull(field.getSqlType());
		} else if(field.getSize() != DBField.DEFAULT ){
			if(field.getSqlType() == Types.DECIMAL || field.getSqlType() == Types.NUMERIC){
				Number num = (Number)val;
				String intVal = String.valueOf(num.longValue());
				if(intVal.length()>field.getSize()){
					TRACE.log(Severities.ERROR, "It seems that the value of " 
							+ field.getDBFieldName() + " exceeds the size of the field in DB : " + val);
					throw new PersistenceException();
				}
				MathContext mc = new MathContext(field.getPrecision()+intVal.length(), RoundingMode.CEILING);
				val = new BigDecimal(num.doubleValue(), mc);
			} else if (val instanceof String){
				if(val.toString().length()>field.getSize()){
					TRACE.log(Severities.ERROR, "It seems that the value of " 
							+ field.getDBFieldName() + " exceeds the size of the field in DB : " + val);
					throw new PersistenceException();
				}
			}
		}

	}
}
