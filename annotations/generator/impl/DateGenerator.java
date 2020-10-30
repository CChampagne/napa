/**
 * by Christophe Champagne
 */
package nanodb.annotations.generator.impl;

import java.util.Date;

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
public class DateGenerator extends AbstractGenerator {
	private Class<? extends Date> expectedType;
	/**
	 * @see nanodb.annotations.generator.Generator#getNextValue()
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
	 * @see nanodb.annotations.generator.impl.AbstractGenerator#performTypeCheck(nanodb.annotations.atk.EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!Date.class.isAssignableFrom(entityField.getJavaType())){
			//Can only generate  Date and derivatives
			throw new PersistenceException();
		}
	}

	/**
	 * @see nanodb.annotations.generator.impl.AbstractGenerator#performInit(nanodb.annotations.GeneratedValue, nanodb.annotations.atk.EntityField, nanodb.annotations.atk.EntityHandler)
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
