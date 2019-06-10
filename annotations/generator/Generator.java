/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.EntityDaoFactory;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.entity.Persistable;

/**
 * @author Christophe Champagne (GII561)
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
	public <E extends Persistable> Generator initInstance(GeneratedValue annotation, EntityField field, EntityHandler<E> entity) throws AnnotationException, PersistenceException;
	/**
	 * The dao factory must be set if Generator need DB access
	 * @param factory
	 */
	public void setFactory(EntityDaoFactory factory);
}
