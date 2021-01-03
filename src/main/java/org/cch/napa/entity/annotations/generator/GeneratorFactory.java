package org.cch.napa.entity.annotations.generator;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import org.cch.napa.entity.annotations.generator.impl.*;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.entity.annotations.GeneratedValue;
import org.cch.napa.entity.annotations.atk.EntityField;
import org.cch.napa.entity.annotations.atk.EntityHandler;

/**
 * @author Christophe Champagne
 * Class in charge of retrieving the instance of a generator corresponding to the given parameters
 * 
 */
public class GeneratorFactory {
	private static final Map<String, Generator> generatorsPerName = new Hashtable<String, Generator>();
	private static final Map<String, Class<? extends Generator>> generatorsPerType = new Hashtable<String, Class<? extends Generator>>();
	private static final Map<Class<?>, String> defaultGeneratorTypesPerFieldType = new Hashtable<Class<?>, String>();
	
	static {
		generatorsPerType.put(GeneratorTypes.SEQUENCE, PseudoSequenceGenerator.class);
		generatorsPerType.put(GeneratorTypes.DATE, DateGenerator.class);
		generatorsPerType.put(GeneratorTypes.CALENDAR, CalendarGenerator.class);
		generatorsPerType.put(GeneratorTypes.UUID, UuidGenerator.class);
		generatorsPerType.put(GeneratorTypes.UUID_STRING, UuidStringGenerator.class);

		defaultGeneratorTypesPerFieldType.put(Integer.class, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Long.class, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Integer.TYPE, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Long.TYPE, GeneratorTypes.SEQUENCE);
		defaultGeneratorTypesPerFieldType.put(Date.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(java.sql.Date.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Time.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Timestamp.class, GeneratorTypes.DATE);
		defaultGeneratorTypesPerFieldType.put(Calendar.class, GeneratorTypes.CALENDAR);
		defaultGeneratorTypesPerFieldType.put(UUID.class, GeneratorTypes.UUID);
		defaultGeneratorTypesPerFieldType.put(String.class, GeneratorTypes.UUID_STRING);
	}
	/**
	 * Get generator following given parameter
	 * @param entityField
	 * @param entityHandler
	 * @return
	 * @throws PersistenceException
	 */
	public static synchronized Generator getGenerator(EntityField entityField, EntityHandler<?> entityHandler) throws PersistenceException{
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
					throw new PersistenceException("No Type could be found corresponding to the Java type of the generated value");
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
