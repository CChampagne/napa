/**
 * by Christophe Champagne
 */
package nanodb.annotations.generator;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.EntityDaoFactory;
import nanodb.annotations.GeneratedValue;
import nanodb.annotations.atk.EntityField;
import nanodb.annotations.atk.EntityHandler;
import nanodb.entity.Persistable;

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
	public <E extends Persistable> Generator initInstance(GeneratedValue annotation, EntityField field, EntityHandler<E> entity) throws AnnotationException, PersistenceException;
	/**
	 * The dao factory must be set if Generator need DB access
	 * @param factory
	 */
	public void setFactory(EntityDaoFactory factory);
}
