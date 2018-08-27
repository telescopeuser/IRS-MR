/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.chap7;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fit.FileRunner;

/**
 * Run rules with FIT based business case testing
 * 
 * @author paulbrowne
 * 
 */
public class FitRulesExample {

	private static Log log = LogFactory.getLog(FitRulesExample.class);

	/**
	 * Main Method so that we can run this from the command line
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Non static handle to call methods
		FitRulesExample example = new FitRulesExample();

		log.info("default directory:" + new File(".").getAbsolutePath());

		// Default args if we haven't already done so
		if ((args == null) || args.length == 0) {
			log.debug("Setting args to defaults");
			example.runFitTest();
		} else {
			// Call the runFit method
			example.runFitTest(args);
		}

	}

	/**
	 * Run the test using the defaults
	 * 
	 * @throws IOException
	 */
	public void runFitTest() throws IOException {
		String[] args = new String[2];
		args[0] = "fit-testcases/fit-testcase.htm"; // input file
		args[1] = "fit-testcases/fit-test-result.htm"; // output file

		runFitTest(args);
	}

	/**
	 * Separate method to make the FIT example easier to unit test
	 * 
	 * @param fitInputFile
	 * @param fitOutputFile
	 * @throws IOException
	 */
	public void runFitTest(String[] args) throws IOException {

		log.debug("Input from:" + args[0]);
		log.debug("Output to:" + args[1]);

		FileRunner fitFileRunner = new FileRunner();
		fitFileRunner.run(args);

	}
}
