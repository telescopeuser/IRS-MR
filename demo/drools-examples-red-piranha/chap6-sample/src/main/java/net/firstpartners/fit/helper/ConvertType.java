/*
 * Created on 28/02/2005
 */
package net.firstpartners.fit.helper;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Sets up the commons converter util with some extra converters (will need to
 * add to this over time). 
 * 
 * TODO: rename and use pico container to "start" this (using startable).
 * Also use pico to manage dependencies (nice side effect).
 */
public class ConvertType {
	
	//setup the custom converters
	public static void setupConverters() {
		ConvertUtils.register(new DateConverter(), java.util.Date.class); 		
	}
	

}

/** 
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * This is for the bean utils converter to do its thang.
 * 
 */
class DateConverter implements Converter {

	/* (non-Javadoc)
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	public Object convert(Class clazz, Object value) {
		String s = (String) value;
		return new Date(s);
	}
	
}