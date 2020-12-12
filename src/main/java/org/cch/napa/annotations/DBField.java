/**
 * 
 */
package org.cch.napa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

/**
 * This annotation can be used on an attribute of a bean or a getter or a setter 
 * to map it to a db field<br>
 * 
 * @author Christophe Champagne
 *
 */
@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
	int DEFAULT = -1;
	/**
	 * name is the name of the field. If omitted, then the field name corresponds to the attribute name <br>
	 */
	String name() default "";
	/**
	 * sqlType is the SQL type as defined in {@link java.sql.Types}. <br> 
	 * If omitted, then the field name corresponds to the attribute name
	 */
	int sqlType() default Types.NULL;
	/**
	 * Size is the size of the column when applicable (e.g. on a varchar but not on an integer).
	 * This property should be used for validation before persistence and to generate tables creation scripts
	 * The value is equals to -1 by default
	 */
	int size() default DEFAULT;
	/**
	 * Size is the precision of the column when applicable (e.g. on a decimal but not on an integer).
	 * This property should be used for validation before persistence and to generate EPL code (create table...)
	 * The value is equals to -1 by default
	 */
	int precision() default DEFAULT;
	/**
	 * Tells if the field is (a part of) the primary key.
	 * This field is fundamental to generate update queries
	 * (false by default)
	 */
	boolean isPrimaryKey() default false;
	/**
	 * Tells if the value of the field can be null.
	 * (true by default)
	 */
	boolean isNullable() default true;
	
	/**
	 * Gives the default value of a field.<br>
	 * WARNING! Single quotes are not managed in order to be able to call SQL functions (get current date...) and set empty strings<br>
	 * This implies that:<br>
	 *  - varchars must be surrounded by single quotes eg @DBField(default="'Hello world'")<br>
	 *  - single quotes must be doubled<br>
	 * 
	 */
	String defaultValue() default "";
	
	/**
	 * Specifies ,if the field is readOnly (when then value is @code true).
	 * This value may be overridden by the {@link ReadOnly} annotation
	 */
	boolean isReadOnly() default false;
}
