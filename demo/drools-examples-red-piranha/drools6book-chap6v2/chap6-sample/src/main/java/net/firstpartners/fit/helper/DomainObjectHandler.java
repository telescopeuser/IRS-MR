/*
 * Created on 10/03/2005
 */
package net.firstpartners.fit.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * This takes care of pushing data into the domain objects.
 */
public class DomainObjectHandler {
	
	private DomainObjectHolder _holder;
	
	public DomainObjectHandler(DomainObjectHolder holder) {
		_holder = holder;
	}

	/**
	 * Note that the arguments can be in in the method text, or a single value text (or a combination of the two). 
	 * There is nothing special about the value text. 
	 * 
	 * @param objectKey The key of the object that is being operated on.
	 * @param methodText The method text (including all arguments in brackets).
	 * @param valueText The value: which may require conversion, be another mapped object, or a constant.
	 * @return A return object (holder) in case one is returned (null of course if nothing).
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public ReturnedObject callMethodFromFit(String objectKey, String methodText, String valueText) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object obj = _holder.getObject(objectKey);		
		MethodReflectionHelper helper = new MethodReflectionHelper(obj);
		List args = getArgumentList(methodText, valueText);
		String method = MethodReflectionHelper.convertEnglishToCamel(MethodTextParser.removeIndexText(methodText));
		return helper.invokeMethod(method, args, _holder);
	}
	
	
	public Object constructObjectFromFit(String className, String argText) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class clazz = Class.forName(className);
		List args = getArgumentList(argText);
		ConstructorReflectionHelper helper = new ConstructorReflectionHelper(clazz);
		return helper.createNewInstance(args, _holder);
	}	

	List getArgumentList(String valueText) {
		List list = new ArrayList();
		String[] args = valueText.split(",");			
		for (int i = 0; i < args.length; i ++) {
			addValueTextToArgumentList(args[i].trim(), list);
		}
		return list;
	}

	List getArgumentList(String methodText, String valueText) {
		List list = new ArrayList();
		if (!(methodText.indexOf("(") != -1)) {			
			addValueTextToArgumentList(valueText, list);
		} else { 
			String[] args = MethodTextParser.getIndexValue(methodText);			
			for (int i = 0; i < args.length; i ++) {
				list.add(args[i].trim());				
			}
			addValueTextToArgumentList(valueText, list);
					
		}
		return list;
	}

	/**
	 * @param valueText
	 * @param list
	 * @return
	 */
	private void addValueTextToArgumentList(String valueText, List list) {
		if (valueText != null && !valueText.equals("")) {
			list.add(valueText.trim());
		}
	}

	private String[] appendToArray(String[] args, String valueText) {
			List list = new ArrayList();
			list.add(valueText);
			list.add(Arrays.asList(args));
			return (String[]) list.toArray();
			
	}

}
