/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator.impl;

import java.util.Date;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.annotations.generator.Generator;
import com.ibm.next.mam.persistence.entity.Persistable;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class DateGenerator extends AbstractGenerator {
	private Class<? extends Date> expectedType;
	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.Generator#getNextValue()
	 */
	public Object getNextValue() throws PersistenceException {
		Date value;
		try {
			value = expectedType.newInstance();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
		value.setTime(System.currentTimeMillis());
		return value;
	}


	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.impl.AbstractGenerator#performTypeCheck(com.ibm.next.mam.persistence.annotations.atk.EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!Date.class.isAssignableFrom(entityField.getJavaType())){
			//Can only generate  Date and derivatives
			throw new PersistenceException();
		}
	}

	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.impl.AbstractGenerator#performInit(com.ibm.next.mam.persistence.annotations.GeneratedValue, com.ibm.next.mam.persistence.annotations.atk.EntityField, com.ibm.next.mam.persistence.annotations.atk.EntityHandler)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <E extends Persistable> Generator performInit(GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws AnnotationException, PersistenceException {
		// TODO Auto-generated method stub
		expectedType = (Class<? extends Date>) field.getJavaType();
		return this;
	}

}
