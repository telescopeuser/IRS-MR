/*
 * Created on 28/02/2005
 */
package net.firstpartners.fit.helper;

import java.lang.reflect.Method;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Utility class for mna-handling the method/attribute field from the fit tests.
 */
public class MethodTextParser {

	private MethodTextParser() {
		
	}
	
	
	/**
	 * 
	 * @param methodName
	 * @return the method with the index removed.
	 */
	public static String removeIndexText(String methodName) {
		if (methodName.indexOf("(") != -1) {
			return methodName.substring(0, methodName.indexOf('('));
		} else {
			return methodName;
		}
		
	}


	/** checks if a particulat method name is indexed */
	public static boolean isIndexed(String methodName) {
		if (!(methodName.indexOf("(") != -1)) {
			return false;
		} else {
			String val = getIndexValue(methodName)[0];
			return NumberUtils.isDigits(val);
		}		
	}

	
	/**
	 * @param methodName Name of the method.
	 * @return An array of all the arguments between "(" and ")", comma seperated.
	 */
	public static String[] getIndexValue(String methodName) {
		int idxStart = methodName.indexOf('(');
		int idxEnd = methodName.indexOf(')');
		return methodName.substring(idxStart + 1, idxEnd).split(",");
	}    	
	
	/**
	 * Note this will search for the FIRST match by name (no type data is considered to find the match).
	 * Watch out for method overloading !
	 * Case is ignored.
	 * It will also check for number of parameters.
	 * 
	 * @param methodName name of the method to match. 
	 * @param domainObject the object to search over.
	 * @return a method if possible.
	 */
	public static Method getMethod(String methodName, int parameterCount, Object domainObject) {		
		//TODO: really need to have a weak cache of this stuff for performance (possibly).		
		Method[] methods = domainObject.getClass().getMethods();
		for (int i = 0 ; i < methods.length; i++) {
			Method meth = methods[i];
			if (meth.getParameterTypes().length == parameterCount
				&& meth.getName().equalsIgnoreCase(methodName)) {
				return methods[i];
			}
		}
		throw new IllegalArgumentException("Unable to find method called: " + methodName +
				" with " + parameterCount + " parameter(s).");
	}	
	
}
