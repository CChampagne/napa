package nanodb.annotations.generator.impl;

import nanodb.exceptions.AnnotationException;
import nanodb.exceptions.PersistenceException;
import nanodb.annotations.GeneratedValue;
import nanodb.annotations.Parameter;
import nanodb.annotations.atk.EntityField;
import nanodb.annotations.atk.EntityHandler;
import nanodb.annotations.generator.Generator;
import nanodb.entity.Persistable;

/**
 * @author Christophe Champagne
 *
 */
public abstract class AbstractSequenceGenerator extends AbstractGenerator{

	private Class<?> expectedClass;
	private short step = 1;

	/**
	 * @see nanodb.annotations.generator.Generator#initInstance(java.lang.String, nanodb.annotations.atk.EntityField, nanodb.annotations.atk.EntityHandler)
	 */
	protected void performTypeCheck(EntityField entityField) throws PersistenceException{
		expectedClass = entityField.getJavaType();
		if(!isInt() && !isLong()){
			//This Generator should only work for long and integer values
			throw new PersistenceException();
		}
	}
	/**
	 * @return the expectedClass
	 */
	public Class<?> getExpectedClass() {
		return expectedClass;
	}
	/**
	 * @see nanodb.annotations.generator.impl.AbstractGenerator#performInit(nanodb.annotations.GeneratedValue, nanodb.annotations.atk.EntityField, nanodb.annotations.atk.EntityHandler)
	 */
	@Override
	protected <E extends Persistable> Generator performInit(
			GeneratedValue annotation, EntityField field,
			EntityHandler<E> entity) throws PersistenceException{
		if(annotation.parameters().length>0){
			for(Parameter parameter: annotation.parameters()){
				if(parameter.name().equalsIgnoreCase("step")){
					try {
						step = new Short(parameter.value());
					} catch (NumberFormatException e) {
						//Invalid parameter
						throw new AnnotationException();
					}
				}
			}
		}
		return this;
	}
	/**
	 * @return the step
	 */
	public short getStep() {
		return step;
	}
	protected boolean isInt(){
		return getExpectedClass().equals(Integer.class) || getExpectedClass().equals(Integer.TYPE);
	}
	protected boolean isLong(){
		return getExpectedClass().equals(Long.class) || getExpectedClass().equals(Long.TYPE);
	}
}