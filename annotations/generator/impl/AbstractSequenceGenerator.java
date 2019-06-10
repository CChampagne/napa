/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator.impl;

import com.ibm.next.mam.errorframework.exceptions.persistence.AnnotationException;
import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.Parameter;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.annotations.generator.Generator;
import com.ibm.next.mam.persistence.entity.Persistable;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public abstract class AbstractSequenceGenerator extends AbstractGenerator{

	private Class<?> expectedClass;
	private short step = 1;

	/**
	 * @see com.ibm.next.mam.persistence.annotations.generator.Generator#initInstance(java.lang.String, com.ibm.next.mam.persistence.annotations.atk.EntityField, com.ibm.next.mam.persistence.annotations.atk.EntityHandler)
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
	 * @see com.ibm.next.mam.persistence.annotations.generator.impl.AbstractGenerator#performInit(com.ibm.next.mam.persistence.annotations.GeneratedValue, com.ibm.next.mam.persistence.annotations.atk.EntityField, com.ibm.next.mam.persistence.annotations.atk.EntityHandler)
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