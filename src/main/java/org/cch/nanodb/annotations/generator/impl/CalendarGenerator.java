/**
 * by Christophe Champagne
 */
package org.cch.nanodb.annotations.generator.impl;

import java.util.Calendar;

import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.annotations.GeneratedValue;
import org.cch.nanodb.annotations.atk.EntityField;
import org.cch.nanodb.annotations.atk.EntityHandler;
import org.cch.nanodb.annotations.generator.Generator;
import org.cch.nanodb.entity.Persistable;

/**
 * @author Christophe Champagne
 *
 */
public class CalendarGenerator extends AbstractGenerator {
	/**
	 * @see Generator#getNextValue()
	 */
	public Object getNextValue() throws PersistenceException {
		return Calendar.getInstance();
	}


	/**
	 * @see AbstractGenerator#performTypeCheck(EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!Calendar.class.equals(entityField.getJavaType())){
			//Can only generate  Date and derivatives
			throw new PersistenceException();
		}
	}

	/**
	 * @see AbstractGenerator#performInit(GeneratedValue, EntityField, EntityHandler)
	 */
	@Override
	protected <E> Generator performInit(GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws AnnotationException, PersistenceException {
		// TODO Auto-generated method stub
		return this;
	}
}
