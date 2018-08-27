/*
 * Created on 28/02/2005
 */
package net.firstpartners.fit.fixture;

import java.lang.reflect.InvocationTargetException;

import net.firstpartners.fit.helper.DomainObjectHandler;
import net.firstpartners.fit.helper.ReturnedObject;

import fit.Parse;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * This fixture makes assertions over the results, to check if the rules are correct.
 */
public class Results extends AbstractRulesTestingFixture {

	/* (non-Javadoc)
	 * @see michael.AbstractRulesTestingFixture#firstColumn(fit.Parse)
	 */
	void processRow(Parse cell) {		
				
		//String methodName = ReflectionHelper.convertEnglishToCamel(cell.more.text());
		String methodName = cell.more.text();
		ReturnedObject result = null;
		
		try {
			result = doWholeRow(cell.text(), methodName);
			if (result.isSame(cell.more.more.text())) {
				resultCorrect(cell);
			} else {
				resultIncorrect(cell, result.toString());
			}
		} catch (IllegalAccessException e) {
			super.wrong(cell, "Unable to access that method.");			
		} catch (InvocationTargetException e) {
			super.exception(cell, e);
		}
		
	}

	private void resultCorrect(Parse cell) {
		super.right(cell);
		super.right(cell.more);
		super.right(cell.more.more);
	}
	
	private void resultIncorrect(Parse cell, String result) {
		super.wrong(cell);
		super.wrong(cell.more);
		super.wrong(cell.more.more, result);
	}

	ReturnedObject doWholeRow(String objectKey, String methodName) throws IllegalAccessException, InvocationTargetException {
		DomainObjectHandler handler = new DomainObjectHandler(getDomainObjects());
		return handler.callMethodFromFit(objectKey, methodName, null);
	}

}
