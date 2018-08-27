/*
 * Created on 8/04/2005
 *
 */
package net.firstpartners.fit.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 */
public class ConstructorReflectionHelper extends ReflectionHelper {
	
	private Class _clazz;
	
	public ConstructorReflectionHelper(Class clazz) {
		_clazz = clazz;
	}
	
	public Object createNewInstance(List argsAsString, DomainObjectHolder holder) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		Constructor[] constructors = _clazz.getConstructors();
		Class[] argTypes = null;
		for (int i = 0; i < constructors.length; i++) {
			argTypes = _clazz.getConstructors()[i].getParameterTypes();
			if (argTypes.length == argsAsString.size()) {
				break;
			}
		}
		if (argTypes == null) {
			throw new IllegalArgumentException("Argument mismatch for the constructor");
		}
		Object[] convertedArgs = convertArgs(argsAsString, holder, argTypes, _clazz);
		return _clazz.getConstructor(argTypes).newInstance(convertedArgs);
	}		

}
