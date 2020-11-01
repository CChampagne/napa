package org.cch.nanodb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to signify its value is never persisted in database<br>
 * This should be used when the value is supposed to be set by a trigger or by default...<br>
 * This can also be set with the {@link DBField} annotation but it is overridden by this one
 *
 *
 * @author Christophe Champagne Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {
}
