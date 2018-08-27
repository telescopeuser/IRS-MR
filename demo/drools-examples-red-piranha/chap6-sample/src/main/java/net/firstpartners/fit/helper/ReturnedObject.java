/*
 * Created on 28/02/2005
 */
package net.firstpartners.fit.helper;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Thin wrapper for a returned object from reflection invokation.
 */
public class ReturnedObject {
	
	private Object _returnedObject;
	
	/**
	 * @param obj The object that is being returned.
	 */
	public ReturnedObject(Object obj) {
		ConvertType.setupConverters();
		_returnedObject = obj;
	}
	
	/**
	 * Convert to a human readable string if needed.
	 */
	public String toString() {
		return ConvertUtils.convert(_returnedObject);
	}
	
	
	/**
	 * This works by converting the valueToCompare to the same type as the returned object, and 
	 * then comparing them - this gives the best chance of match, as opposed to just comparing strings.
	 * 
	 * @param valueToCompare The value you wish to compare the returned object with.
	 * @return True if it is the same.
	 */
	public boolean isSame(String valueToCompare) {
		
		
		Object staticValue = findStaticField(valueToCompare);
		if (staticValue != null) {
			return staticValue.equals(_returnedObject);
		}
		Object value = ConvertUtils.convert(valueToCompare, _returnedObject.getClass());
		return value.equals(_returnedObject);
	}


	private Object findStaticField(String valueToCompare)  {
		try {
			return ReflectionHelper.findStaticField(_returnedObject.getClass(), valueToCompare);		
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Unable to access static field of name: " 
												+ valueToCompare);
		}
	}
	
    /**
     * @return Returns the returnedObject.
     */
    public Object getReturnedObject() {
        return _returnedObject;
    }
    /**
     * @param returnedObject The returnedObject to set.
     */
    public void setReturnedObject(Object returnedObject) {
        _returnedObject = returnedObject;
    }
}
