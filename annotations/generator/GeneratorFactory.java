/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.annotations.generator;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import com.ibm.next.mam.errorframework.exceptions.persistence.PersistenceException;
import com.ibm.next.mam.persistence.annotations.GeneratedValue;
import com.ibm.next.mam.persistence.annotations.atk.EntityField;
import com.ibm.next.mam.persistence.annotations.atk.EntityHandler;
import com.ibm.next.mam.persistence.annotations.generator.impl.CalendarGenerator;
import com.ibm.next.mam.persistence.annotations.generator.impl.DateGenerator;
import com.ibm.next.mam.persistence.annotations.generator.impl.PseudoSequenceGenerator;
import com.ibm.next.mam.persistence.entity.Persistable;
import com.sap.ip.me.api.logging.Severities;
import com.sap.ip.me.api.logging.Trace;

/**
 * @author Christophe Champagne (GII561)
 * Class in charge of retrieving the instance of a generator corresponding to the given parameters
 * 
 */
public class GeneratorFactory {
	private static final Map<String, Generator> generatorsPerName = new Hashtable<String, Generator>();
	private static final Map<String, Class<? extends Generator>> generatorsPerType = new Hashtable<String, Class<? extends Generator>>();
	private static final Map<Class<?>, String> defaultGeneratorTypesPerFieldType = new Hashtable<Class<?>, String>();
	private static final Trace TRACE = Trace.getInstance(GeneratorFactory.class.getName());
	
	static {
		generatorsPerType.put(GeneratorTypes.SEQUENCE, PseudoSequenceGenerator.class);
		generatorsPerType.put(GeneratorTypes.DATE, DateGenerator.class);
		generatorsPerType.put(GeneratorTypes.CALENDAR, CalendarGenerator.class);
		
		defaultGeneratorTypesPerFieldType.put(Integer.class, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Long.class, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Integer.TYPE, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Long.TYPE, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Date.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(java.sql.Date.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Time.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Timestamp.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Calendar.class, GeneratorTypes.CALENDAR);
	}
	/**
	 * Get generator following given parameter
	 * @param type Type of the generator
	 * @param name the name of the generator. Looks first if 
	 * @param entityField
	 * @param entityHandler
	 * @return
	 * @throws PersistenceException
	 */
	public static synchronized Generator getGenerator(EntityField entityField, EntityHandler<? extends Persistable> entityHandler) throws PersistenceException{
		Generator generator = null; 
		GeneratedValue annotation = entityField.getGeneratedValueAnnotation();
		if(!isDefault(annotation.name())){
			generator = generatorsPerName.get(annotation.name());
		}
		if(generator == null){
			String type = annotation.type();
			if(isDefault(type)){
				type = defaultGeneratorTypesPerFieldType.get(entityField.getJavaType());
				if(type == null){
					TRACE.log(Severities.ERROR, "No Type could be found corresponding to the Java type of the generated vaue");
					throw new PersistenceException();
				}
			}
			Class<? extends Generator> generatorClass = generatorsPerType.get(type);
			try {
				generator = generatorClass.newInstance();
			} catch (InstantiationException e) {
				throw new PersistenceException(e);
			} catch (IllegalAccessException e) {
				throw new PersistenceException(e);
			}
			generator = generator.initInstance(annotation, entityField, entityHandler);
			if(!isDefault(annotation.name())){
				generatorsPerName.put(annotation.name(), generator);
			}
		}
		return generator;
	}
	
	public static synchronized void registerType(String typeName, Class<Generator> generatorImplementationClass){
		generatorsPerType.put(typeName, generatorImplementationClass);
	}
	public static synchronized void setDefaultForJavaType(Class<?> javaTypeOfField, String generatorTypeName){
		defaultGeneratorTypesPerFieldType.put(javaTypeOfField, generatorTypeName);
	}
	
	private static boolean isDefault(String value){
		return value == null || value.trim().length() == 0;
	}
}
