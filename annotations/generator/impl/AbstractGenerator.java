/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator.impl;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.annotations.generator.Generator;
import com.ibm.next.mam.persistence.entity.Persistable;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public abstract class AbstractGenerator implements Generator{
	private EntityDaoFactory factory;

	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.Generator#initInstance(com.ibm.next.mam.persistence.annotations.GeneratedValue, com.ibm.next.mam.persistence.annotations.atk.EntityField, com.ibm.next.mam.persistence.annotations.atk.EntityHandler)
	 */
	public <E extends Persistable> Generator initInstance(
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
	protected abstract  <E extends Persistable> Generator performInit(
			GeneratedValue annotation, EntityField field, EntityHandler<E> entity) 
			throws AnnotationException,PersistenceException;
	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.Generator#setFactory(com.ibm.next.mam.persistence.EntityDaoFactory)
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
