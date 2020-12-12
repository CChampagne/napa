package org.cch.napa.annotations.generator.impl;

import org.cch.napa.exceptions.AnnotationException;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.annotations.GeneratedValue;
import org.cch.napa.annotations.Parameter;
import org.cch.napa.annotations.atk.EntityField;
import org.cch.napa.annotations.atk.EntityHandler;
import org.cch.napa.annotations.generator.Generator;

/**
 * @author Christophe Champagne
 *
 */
public abstract class AbstractSequenceGenerator extends AbstractGenerator{

	private Class<?> expectedClass;
	private short step = 1;

	/**
	 * @see Generator#initInstance(GeneratedValue, EntityField, EntityHandler)
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
	 * @see AbstractGenerator#performInit(GeneratedValue, EntityField, EntityHandler)
	 */
	@Override
	protected <E> Generator performInit(
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