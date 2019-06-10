/**
 * 
 */
package com.ibm.next.mam.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to signify it cannot have a NULL value<br>
 * 
 * @author GII561
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
}
