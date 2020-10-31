/**
 * 
 */
package org.cch.nanodb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter<br> 
 * It specifies the values is generated on insert.<br> 
 * 
 * @author Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {
	/**
	 * This is the name of the generator<br>
	 * It corresponds to one instance of the generator. 
	 * For example the name of the generator can be the name of a DB sequence.
	 * A same Generator can thus be related to several fields.
	 * If omitted, the name is determined following the type of the generator.
	 * @return the name of the Generator instance
	 */
	String name() default "";
	/**
	 * Type of the Generator.<br>
	 * The type can be used to retrieve the exact implementation of the generator.
	 * If omitted, the type is determined dynamically following the type of the field.
	 * @return the type of the Generator instance
	 */
	String type() default "";	
	/**
	 * Specifies if the value is generated only when the annotated field is null
	 */
	boolean onlyGenerateWhenNull() default true;
	/**
	 * Specifies if the value is generated only during an insert 
	 * or if it also works on 
	 */
	boolean generateAlsoOnUpdate() default false;
	/**
	 * Optional parameters that could be useful following the kind of Generator
	 */
	Parameter[] parameters() default {};
}
