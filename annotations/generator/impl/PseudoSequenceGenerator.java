/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator.impl;

import java.util.List;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.Parameter;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.annotations.generator.Generator;
import com.ibm.next.mam.persistence.entity.Persistable;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class PseudoSequenceGenerator extends AbstractSequenceGenerator{
	public final static String PARAM_NAME_CACHED = "cached";
	public final static String PARAM_VALUE_CACHED = "true";
	public final static String PARAM_VALUE_NOT_CACHED = "false";

	private String query;
	private SingleValueMapper mapper = new SingleValueMapper();
	private boolean cached = true;
	private Long lastValue;
	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.Generator#getNextValue()
	 */
	public Number getNextValue() throws PersistenceException {
		if(!cached  || lastValue == null){
			if(getFactory()!=null && getFactory().getJdbcDao() !=null){
				List<Long> values= getFactory().getJdbcDao().select(query, mapper);
				lastValue = values.get(0);
			}
		}
		if(lastValue != null){
			lastValue = new Long(lastValue.longValue() + getStep());
			if (isInt()){			
				return new Integer(lastValue.intValue());
			} 
		}
		return lastValue;
	}


	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.impl.AbstractSequenceGenerator#performInit(com.ibm.next.mam.persistence.annotations.GeneratedValue, com.ibm.next.mam.persistence.annotations.atk.EntityField, com.ibm.next.mam.persistence.annotations.atk.EntityHandler)
	 */
	@Override
	protected <E extends Persistable> Generator performInit(GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws PersistenceException {
		super.performInit(annotation, field, entity);
		query = "select max("+ field.getDBFieldName() + ") from " + entity.getTableName();
		Parameter[] params = annotation.parameters();
		for (Parameter param : params){
			if(PARAM_NAME_CACHED.equals(param)){
				cached = !PARAM_VALUE_NOT_CACHED.equals(param.value());
			}
		}
		return this;
	}

}
