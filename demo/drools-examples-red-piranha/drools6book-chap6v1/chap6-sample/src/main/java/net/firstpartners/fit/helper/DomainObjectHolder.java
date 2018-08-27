/*
 * Created on 25/02/2005
 */
package net.firstpartners.fit.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * This is a "singleton" that will hold the list of domain objects.
 * All objects are keyed in UPPER CASE so as to ignore case.
 * 
 */
public class DomainObjectHolder {
	
	private Map domainObjects;
	private static ThreadLocal _thread = new ThreadLocal();
		
	private DomainObjectHolder() {
		domainObjects = new HashMap();
	}
	
	/**
	 * @return The object holder.
	 */
	public static DomainObjectHolder getInstance() {
		DomainObjectHolder instance = (DomainObjectHolder) _thread.get();
		if (instance == null) {
			instance = new DomainObjectHolder();
			_thread.set(instance);
		}
		return instance;
	}
	
	/**
	 * 
	 * @param obj the object to add.
	 * @param key the key to look up.
	 * will throw an Illegal Argument exception if it already exists.
	 */
	public void addObject(Object obj, String key) {
		String keyUpper = key.toUpperCase(); 
		if (domainObjects.containsKey(keyUpper)) {
			throw new IllegalArgumentException("The domain object key '" + key 
										+ "' already exists, chose another ");
		}
		domainObjects.put(keyUpper, obj);
	}
	
	/**
	 * 
	 * @param key - the object to look up.
	 * @return the object.
	 */
	public Object getObject(String key) {
		String keyUpper = key.toUpperCase();
		if (!domainObjects.containsKey(keyUpper)) {
			throw new IllegalArgumentException("No domain object for key '" + key 
												+"' exists");
		}
		return domainObjects.get(keyUpper);
	}
	
	/**
	 * 
	 * @param key The object key to look up.
	 * @return true if it exists.
	 */
	public boolean containsKey(String key) {
		String keyUpper = key.toUpperCase();
		return domainObjects.containsKey(keyUpper);
	}

	/**
	 * Reset the map of objects.
	 */
	public void reset() {
		domainObjects.clear();
	}
	
	/**
	 * @return All the domain objects.
	 */
	public Collection all() {
		return this.domainObjects.values();
	}

}
