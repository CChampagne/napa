package org.cch.napa.entity.annotations.generator;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.annotations.GeneratedValue;
import org.cch.napa.entity.annotations.atk.EntityField;
import org.cch.napa.entity.annotations.atk.EntityHandler;

/**
 * @author Christophe Champagne
 * Interface of a generator use
 */
public interface Generator {
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
