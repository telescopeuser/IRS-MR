
package net.firstpartners.fit.fixture;


import java.lang.reflect.InvocationTargetException;

import net.firstpartners.fit.helper.DomainObjectHandler;

import fit.Parse;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * This populates data for the domain objects that were setup with DomainSetupFixture.
 * 
 * Due to fit, I have to use some kind of a singleton pattern to get stuff that it across multiple fixtures
 * (as each fixture is stand alone, with not much in common other then summary data).
 * 
 * This works by taking the right most cell as a value and setting it via the method in the middle cell.
 * The first cell indicates what part of the object graph it will be places in (it is a name that was setup in the
 * <link>DomainSetupFixture</link>).
 * <br>
 * This operates as follows:
 * 	1. Check if the name matches a key that was setup in <link>Setup</link>. If it does, then that should be 
 *     passed into the method, no conversion.
 *  2. It looks at the type of the reveiving method, it then checks it for any static fields that may have the same name as the 
 *     "value" - this allows ENUM type stuff to work (TODO: Not implemented).
 *  3. It looks through all the types from the <link>Setup</link> and sees if any static fields match up by name.
 *     This also allows flexible ENUM type stuff to work. (TODO: Not implemented - may never be).
 * <br>
 * Text conversion:
 * Plain english is used by removing spaces, and then looking for matching methods/keys/fields based on name, ignoring case.
 * Obviously this won't work well if you tend to mix case up badly.
 * Also, overloading the methods will not work too well, but this never a good idea for "beans" - and this is what it is all about.
 * Remember that populating domain objects for a rules engine should be pretty simple (as it is a Business Object model).
 * 
 */
public class Populate extends AbstractRulesTestingFixture {


	/**
	 * Can handle things liks "SET some value (1)" where "1" is an index value.
	 * Makes it a bit more readable to end users.
	 */
    void processRow(Parse cell) {
    	

    	
    	//String methodName = ReflectionHelper.convertEnglishToCamel(cell.more.text());
    	String methodName = cell.more.text();
    	String valueText = cell.more.more.text();

    	try {
	    	parseAndInvoke(cell.text(), methodName, valueText);
	    	super.right(cell);
	    	super.right(cell.more);
	    	super.right(cell.more.more);	    				
		} catch (IllegalAccessException e) {
			super.wrong(cell, "Unable to access that method.");				
		} catch (InvocationTargetException e) {
			super.exception(cell, e);			
		}    		 

    }


	void parseAndInvoke(String domainObjectKey, String methodName, String valueText) throws IllegalAccessException, InvocationTargetException {
		DomainObjectHandler handler = new DomainObjectHandler(getDomainObjects());
		handler.callMethodFromFit(domainObjectKey, methodName, valueText);
	}



}
