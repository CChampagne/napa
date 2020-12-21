package org.cch.napa.entity.annotations.generator.impl;

import org.cch.napa.entity.annotations.GeneratedValue;
import org.cch.napa.entity.annotations.atk.EntityField;
import org.cch.napa.entity.annotations.atk.EntityHandler;
import org.cch.napa.entity.annotations.generator.Generator;
import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;

import java.util.Date;
import java.util.UUID;

/**
 * @author Christophe Champagne
 *
 */
public class GuidGenerator extends AbstractGenerator {
	/**
	 * @see Generator#getNextValue()
	 */
	public Object getNextValue() throws PersistenceException {
		return UUID.randomUUID();
	}

	/**
	 * @see AbstractGenerator#performTypeCheck(EntityField)
	 */
	@Override
	protected void performTypeCheck(EntityField entityField) throws PersistenceException {
		if(!UUID.class.isAssignableFrom(entityField.getJavaType())){
			//Can only generate  UUId and derivatives
			throw new PersistenceException();
		}
	}
}
