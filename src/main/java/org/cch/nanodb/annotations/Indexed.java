/**
 * by Christophe Champagne
 */
package org.cch.nanodb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to signify it is a member of one or several indexes<br>
 * 
 * @author Christophe Champagne Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Indexed {

	/**
	 * Name(s) of the index(es) the field takes part.<br/>
	 * If nothing specified, then the index concerns this field only
	 * and its name is generated from the name of the table and the name of the field 
	 * @return returns an empty array by default.
	 */
	String[] names() default {};
}
