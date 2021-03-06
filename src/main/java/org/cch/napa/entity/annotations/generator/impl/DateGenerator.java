package org.cch.napa.entity.annotations.generator.impl;

import java.util.Date;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.entity.annotations.GeneratedValue;
import org.cch.napa.entity.annotations.atk.EntityField;
import org.cch.napa.entity.annotations.atk.EntityHandler;
import org.cch.napa.entity.annotations.generator.Generator;

/**
 * @author Christophe Champagne
 *
 */
public class DateGenerator extends AbstractGenerator {
	private Class<? extends Date> expectedType;
	/**
	 * @see Generator#getNextValue()
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
	 * @see AbstractGenerator#performTypeCheck(EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!Date.class.isAssignableFrom(entityField.getJavaType())){
			//Can only generate  Date and derivatives
			throw new PersistenceException();
		}
	}

	/**
	 * @see AbstractGenerator#performInit(GeneratedValue, EntityField, EntityHandler)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <E> Generator performInit(GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws AnnotationException, PersistenceException {
		// TODO Auto-generated method stub
		expectedType = (Class<? extends Date>) field.getJavaType();
		return this;
	}

}
