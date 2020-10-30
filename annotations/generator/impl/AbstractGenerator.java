package nanodb.annotations.generator.impl;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.EntityDaoFactory;
import nanodb.annotations.GeneratedValue;
import nanodb.annotations.atk.EntityField;
import nanodb.annotations.atk.EntityHandler;
import nanodb.annotations.generator.Generator;
import nanodb.entity.Persistable;

/**
 * @author Christophe Champagne
 *
 */
public abstract class AbstractGenerator implements Generator{
	private EntityDaoFactory factory;

	/**
	 * @see nanodb.annotations.generator.Generator#initInstance(nanodb.annotations.GeneratedValue, nanodb.annotations.atk.EntityField, nanodb.annotations.atk.EntityHandler)
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
	 * @see nanodb.annotations.generator.Generator#setFactory(nanodb.EntityDaoFactory)
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
