/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.chap7;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class FitUnitTest {

	private static Log log = LogFactory.getLog(FitUnitTest.class);

	/**
	 * Simple test, just to make sure we catch any failures of the FitUnitTest
	 * @throws Exception
	 */
	@Test
	public void testFitExample() throws Exception {

		FitRulesExample example= new FitRulesExample();
		example.runFitTest();
		

	}

}
