/**
 * by Christophe Champagne
 */
package nanodb.annotations.generator;

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
	 * Generator creation a date or derivative corresponding to the current time
	 */
	public static final String DATE = "date";
	/**
	 * Generator creation a Calendar corresponding to the current time
	 */
	public static final String CALENDAR = "cal";
}
