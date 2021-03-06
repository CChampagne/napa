package org.cch.napa.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is be used as a parameter index of the  
 * to signify it is a member of the index<br>
 * 
 * @author Christophe Champagne Christophe Champagne
 *
 */
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
	

	/**
	 * Name of the index
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * Name of the index
	 * 
	 * @return
	 */
	String[] fields();
	
	/**
	 * 
	 * @return
	 */
	boolean unique() default false;
	
}
