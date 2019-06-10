/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Christophe Champagne (GII561)
 * This annotation can be used to be set as parameter of other annotations
 */
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	/**
	 * The name of the parameter
	 * @return
	 */
	String name();
	/**
	 * 
	 */
	String value();
}
