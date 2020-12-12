package org.cch.napa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to signify it is a member of one or several unique indexes<br>
 * 
 * Note: this annotation was created in addition of {@link Indexed} in order to let a field be a part of both unique and non-unique indexes.
 * 
 * @author Christophe Champagne Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIndexed {

	/**
	 * Name of the index
	 * If nothing specified, then the index concerns this field only
	 * and its name is generated from the name of the table and the name of the field 
	 * @return
	 */
	String[] names() default {};
}
