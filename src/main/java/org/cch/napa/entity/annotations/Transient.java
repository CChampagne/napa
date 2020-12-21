/**
 * 
 */
package org.cch.napa.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to signify it cannot be mapped to any field in the Database.<br><br>
 * 
 * NOTE: using the transient modifier on a variable should have the same effect
 * 
 * @author Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {
}
