/**
 * by Christophe Champagne
 */
package org.cch.nanodb.annotations.generator;

import org.cch.nanodb.exceptions.AnnotationException;
import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.annotations.GeneratedValue;
import org.cch.nanodb.annotations.atk.EntityField;
import org.cch.nanodb.annotations.atk.EntityHandler;
import org.cch.nanodb.entity.Persistable;

/**
 * @author Christophe Champagne
 * Interface of a generator use
 */
public interface Generator{
	/**
	 * 
	 * @return gives the next value 
	 */
	public Object getNextValue() throws PersistenceException;
	
	/**
	 * The methods makes the initializations necessary for the generator to work.<br>
	 * It is important to specify that it should also check if the value it generates
	 *  can be casted to the type of the field.
	 * @return an instance or the generator
	 */
	public <E> Generator initInstance(GeneratedValue annotation, EntityField field, EntityHandler<E> entity) throws AnnotationException, PersistenceException;
	/**
	 * The dao factory must be set if Generator need DB access
	 * @param factory
	 */
	public void setFactory(EntityDaoFactory factory);
}
