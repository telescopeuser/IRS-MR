/*
 * Created on 25/02/2005
 */
package net.firstpartners.fit.fixture;

import java.lang.reflect.InvocationTargetException;

import net.firstpartners.fit.helper.DomainObjectHandler;
import net.firstpartners.fit.helper.MethodReflectionHelper;
import net.firstpartners.fit.helper.ReflectionHelper;
import net.firstpartners.fit.helper.ReturnedObject;
import fit.Parse;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * This fixture sets up the domain objects into the map. TODO: Make this
 * automagically find classes without requiring a full classpath. Also, could
 * make the populate fixture automagically do the same in that case (so this
 * fixture, ultimately, would be optional when class names need to be mapped).
 * Alternatives to this fixture may be a python or ruby based on for more
 * sophisticated model setups (but still basic).
 */
public class Setup extends AbstractRulesTestingFixture {

	/**
	 * @param cell
	 */
	void processRow(Parse cell) {
		String text = cell.text();

		String constructorParams = null;
		if (cell.more.more != null) {
			constructorParams = cell.more.more.text();
		}
		String className = ReflectionHelper.decapitaliseFirst(text);
        
		try {
			Object domainObj = null;
            //Load object from a reference on another object
            if (className.indexOf(' ') > 0) {
                ReturnedObject ro = (ReturnedObject) loadDomainObjectFromMethod(className);
                domainObj = ro.getReturnedObject();
            //Load object usings its constructor with parameters
            } else if (constructorParams != null
					&& !constructorParams.trim().equals("")) {
				domainObj = loadDomainObjectWithConstructor(className,
						constructorParams);
			} else {
				domainObj = loadDomainObject(className);
			}
			addDomainObjectInstance(cell.more.text(), domainObj);
			super.right(cell);
			super.right(cell.more);
		} catch (InstantiationException e) {
			super.wrong(cell, "Unable to create new domain object.");
		} catch (IllegalAccessException e) {
			super.wrong(cell, "Not allowed to create new domain object.");
		} catch (ClassNotFoundException e) {
			super.wrong(cell, "Unable to find domain class.");
		} catch (IllegalArgumentException e) {
			super.wrong(cell, "Illegal argument");
		} catch (SecurityException e) {
			super.wrong(cell, "Security exception");
		} catch (InvocationTargetException e) {
			super.wrong(cell, "Invocation exception");
		} catch (NoSuchMethodException e) {
			super.wrong(cell, "Unable to find the method");
		}
	}

    Object loadDomainObjectFromMethod(String className) throws IllegalAccessException, 
                                                               InstantiationException,
                                                               ClassNotFoundException,
                                                               InvocationTargetException {
        
        String clazz = className.substring(0, className.indexOf(' '));        
        
        int startPos = className.indexOf(' ');
        int endPos = className.length();
        String methodText = className.substring(startPos,endPos);

        Object domainObject = getDomainObjects().getObject(clazz);
        
        MethodReflectionHelper helper = new MethodReflectionHelper(domainObject);
        String method = MethodReflectionHelper.convertEnglishToCamel(methodText);
        return helper.invokeMethod(method);        
    }

    private void addDomainObjectInstance(String text, Object domainObj) {
		getDomainObjects().addObject(domainObj, text);
	}

	private Object loadDomainObject(String className) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return Class.forName(className).newInstance();
	}

	private Object loadDomainObjectWithConstructor(String className, String params)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		DomainObjectHandler handler = new DomainObjectHandler(
				getDomainObjects());
		return handler.constructObjectFromFit(className, params);		
	}

	Object loadDomainObject(String className, String constructorParams)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (constructorParams != null && !constructorParams.equals("")) {
			return loadDomainObjectWithConstructor(className, constructorParams);
		} else {
			return loadDomainObject(className);
		}
	}
    

}