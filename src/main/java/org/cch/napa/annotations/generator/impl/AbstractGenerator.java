package org.cch.napa.annotations.generator.impl;

import org.cch.napa.EntityDaoFactory;
import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.annotations.GeneratedValue;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.annotations.atk.EntityHandler;
import org.cch.napa.annotations.generator.Generator;

/**
 * @author Christophe Champagne
 *
 */
public abstract class AbstractGenerator implements Generator{
	private EntityDaoFactory factory;

	/**
	 * @see Generator#initInstance(GeneratedValue, EntityField, EntityHandler)
	 */
	public <E> Generator initInstance(
			GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws AnnotationException,
			PersistenceException {
		
		performTypeCheck(field);
		performInit(annotation, field, entity);
		return this;
	}
	/**
	 * Checks if the 
	 * @param entityField
	 * @throws PersistenceException when the generated value cannot be 
	 * casted to the expected java type (the type of the field) 
	 */
	protected abstract void performTypeCheck(EntityField entityField)throws PersistenceException;
	
	/**
	 * Do the proper instanciation (if required) and initialization 
	 */
	protected abstract  <E> Generator performInit(
			GeneratedValue annotation, EntityField field, EntityHandler<E> entity) 
			throws AnnotationException,PersistenceException;
	/**
	 * @see Generator#setFactory(EntityDaoFactory)
	 */
	public void setFactory(EntityDaoFactory factory) {
		this.factory = factory;
	}
	/**
	 * @return the factory
	 */
	protected EntityDaoFactory getFactory() {
		return factory;
	}		
}
