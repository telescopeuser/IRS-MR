/*
 * Created on 8/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.firstpartners.fit.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;


/**
 * @author U982043
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MethodReflectionHelper extends ReflectionHelper {
	
	private Object _domainObject;
	
	public MethodReflectionHelper(Object obj) {
	
		ConvertType.setupConverters();
		_domainObject = obj;
	}
	

	/**
	 * Used for getting simple values back.  
	 */
	public ReturnedObject invokeMethod(String methodName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		Method method = getMethod(methodName, 0);
		return new ReturnedObject(method.invoke(_domainObject, new Object[] {}));
	}
	
	
	/**
	 * This invokes a method taking a list of arguments as Strings.
	 * 
	 * The commons converter utility is used to convert the incoming string args to appropriate types.
	 * 
	 * The method is found by finding the first matching method by name and number of arguments. Avoid overloaded methods.
	 * Argument type conversion:
	 * A static in either the target class, OR the domain object is looked up by name
	 * A domain object key is looked up by name.
	 * Failing that then type conversion is used (via commons utils).
	 */
	public ReturnedObject invokeMethod(String methodName, List argsAsString, DomainObjectHolder holder) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		Method method = getMethod(methodName, argsAsString.size());
		Class[] argTypes = method.getParameterTypes();
		Object[] convertedArgs = convertArgs(argsAsString, holder, argTypes, _domainObject.getClass());
		return new ReturnedObject(method.invoke(_domainObject, convertedArgs));
	}	
	
	

	/** used for setting simple values, or getting back and indexed/mapped value
	 */
	public ReturnedObject invokeMethod(String methodName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = getMethod(methodName, 1);
		Class targetClass = method.getParameterTypes()[0];		
		Object convertedVal = ConvertUtils.convert(value, targetClass);
		Object returnObj = method.invoke(_domainObject, new Object[] {convertedVal});
		return new ReturnedObject(returnObj);
	}
	
	/** just invoke a method an pass the object directly. No conversion. */
	public ReturnedObject invokeMethodNoConversion(String methodName, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = getMethod(methodName, 1);
		Object returnObj = method.invoke(_domainObject, new Object[] {value});
		return new ReturnedObject(returnObj);
	}
	
	/** used for setting indexed values (simple to add same for mapped when needed). No conversion. */
	public ReturnedObject invokeMethodNoConversion(String methodName, Object value, int index) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = getMethod(methodName, 2);
		Class[] targets = method.getParameterTypes();
		Object returnObj;
		if (targets[0] == Integer.class) {	
			returnObj = method.invoke(_domainObject, new Object[] {new Integer(index), value});
		} else {			
			returnObj = method.invoke(_domainObject, new Object[] {value, new Integer(index)});		
		}
		return new ReturnedObject(returnObj);		
	}
	
	
	/** used for setting indexed values (simple to add same for mapped when needed) */
	public ReturnedObject invokeMethod(String methodName, String value, int index) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = getMethod(methodName, 2);
		Class[] targets = method.getParameterTypes();
		Object returnObj;
		if (targets[0] == Integer.class) {
			Object convertedVal = ConvertUtils.convert(value, targets[1]);
			returnObj = method.invoke(_domainObject, new Object[] {new Integer(index), convertedVal});
		} else {
			Object convertedVal = ConvertUtils.convert(value, targets[0]);
			returnObj = method.invoke(_domainObject, new Object[] {convertedVal, new Integer(index)});		
		}
		return new ReturnedObject(returnObj);		
	}
	
	
	
	private Method getMethod(String methodName, int parameterCount) {
		return MethodTextParser.getMethod(methodName, parameterCount,  _domainObject);
	}
	

	
	
	
}
