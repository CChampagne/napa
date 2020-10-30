/**
 * by Christophe Champagne
 */
package nanodb.annotations.generator.impl;

import java.util.Calendar;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.annotations.GeneratedValue;
import nanodb.annotations.atk.EntityField;
import nanodb.annotations.atk.EntityHandler;
import nanodb.annotations.generator.Generator;
import nanodb.entity.Persistable;

/**
 * @author Christophe Champagne
 *
 */
public class CalendarGenerator extends AbstractGenerator {
	/**
	 * @see nanodb.annotations.generator.Generator#getNextValue()
	 */
	public Object getNextValue() throws PersistenceException {
		return Calendar.getInstance();
	}


	/**
	 * @see nanodb.annotations.generator.impl.AbstractGenerator#performTypeCheck(nanodb.annotations.atk.EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!Calendar.class.equals(entityField.getJavaType())){
			//Can only generate  Date and derivatives
			throw new PersistenceException();
		}
	}

	/**
	 * @see nanodb.annotations.generator.impl.AbstractGenerator#performInit(nanodb.annotations.GeneratedValue, nanodb.annotations.atk.EntityField, nanodb.annotations.atk.EntityHandler)
	 */
	@Override
	protected <E extends Persistable> Generator performInit(GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws AnnotationException, PersistenceException {
		// TODO Auto-generated method stub
		return this;
	}
}
