package org.cch.nanodb.annotations.generator.impl;

import org.cch.nanodb.EntityDaoFactory;
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
