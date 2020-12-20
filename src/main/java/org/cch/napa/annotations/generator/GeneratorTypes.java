package org.cch.napa.annotations.generator;

/**
 * @author Christophe Champagne
 *
 * This interface enumerates the types of generators
 * supported by default by persistence framework.
 */
public class GeneratorTypes {
	/**
	 * Generator fainting a DB sequence
	 */
	public static final String SEQUENCE = "pseudo_sequence";
	/**
	 * Generator creating a date or derivative corresponding to the current time
	 */
	public static final String DATE = "date";
	/**
	 * Generator creating a Calendar corresponding to the current time
	 */
	public static final String CALENDAR = "cal";
	/**
	 *  Generator creating a UUID object
	 */
	public static final String GUID = "GUID";
}
