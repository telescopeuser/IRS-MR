/*
 * Created on 26/02/2005
 */
package net.firstpartners.fit.helper;


import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Utilities for using reflection on a domain object.
 * Makes heavy use of apache commons, especially the bean utils and converter utils.
 */
public abstract class ReflectionHelper {
	
	
	/**
	 * @param argsAsString
	 * @param holder
	 * @param argTypes
	 * @return
	 * @throws IllegalAccessException
	 */
	Object[] convertArgs(List argsAsString, DomainObjectHolder holder, Class[] argTypes, Class domainObjectClass) throws IllegalAccessException {
		Object[] convertedArgs = new Object[argTypes.length];
		for (int i = 0; i < argTypes.length; i++) {
			Class targetClass = argTypes[i];
			String argText = (String) argsAsString.get(i);
			Object staticField = findStaticField(new Class[] {targetClass, domainObjectClass}, argText);
			if (staticField != null) {
				convertedArgs[i] = staticField;
			} else if (holder.containsKey(argText)) {
				convertedArgs[i] = holder.getObject(argText);
			} else {
				Object arg = ConvertUtils.convert((String) argsAsString.get(i), argTypes[i]);
				convertedArgs[i] = arg;
			}
		}
		return convertedArgs;
	}	
	
	/**
	 * @param english with spaces.
	 * @return the camel case version.
	 */
	public static String convertEnglishToCamel(String english) {
		if (!(english.indexOf(" ") != -1)) {
			//assume already camel.
			return english;
		}
		
		String properCase = WordUtils.capitalize(english);
		String titleCase = ReflectionHelper.decapitaliseFirst(properCase);		
		return removeSpaces(titleCase);
	}
	
	private static String removeSpaces(String titleCase) {
		return StringUtils.replace(titleCase, " ", "");
	}

	/**
	 * 
	 * @param s The string to work on.  
	 * @return a String with the first character made lower case.
	 */
	public static String decapitaliseFirst(String s) {
		char head = s.charAt(0);
		String tail = s.substring(1);
		return Character.toLowerCase(head) + tail;
	}

	
	/**
	 * Take an array of classes and try and find a matching field. 
	 */
	static Object findStaticField(Class[] classes, String fieldName) throws IllegalArgumentException, IllegalAccessException {
		for (int i = 0; i < classes.length; i++) {
			Object obj = findStaticField(classes[i], fieldName);
			if (obj != null) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * Get a static field, to help with "fake" enums.
	 * If it contains spaces, then it will convert them to underscores, as is the norm.
	 * Type is ignored.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object findStaticField(Class clazz, String fieldName) throws IllegalArgumentException, IllegalAccessException {
		String name = convertFieldNameSpaces(fieldName);
		Field field;
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {			
			if (fields[i].getName().equalsIgnoreCase(name)) {
				return fields[i].get(null);
			}
		}
		return null;
		
	}


	static String convertFieldNameSpaces(String fieldName) {
		String name = fieldName;
		if (fieldName.indexOf(" ") != -1) {
			name = fieldName.replace(' ', '_');
		}
		return name;
	}		


	
	
	

}


