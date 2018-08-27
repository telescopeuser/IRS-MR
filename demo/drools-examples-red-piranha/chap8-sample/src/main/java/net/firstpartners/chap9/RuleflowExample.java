package net.firstpartners.chap9;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.firstpartners.drools.RuleRunner;
import net.firstpartners.drools.log.ExcelLogger;
import net.firstpartners.excel.Range;
import net.firstpartners.excel.RangeConvertor;
import net.firstpartners.excel.RangeHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.drools.StatefulSession;

/**
 * Sample showing how we can read and manipulate data from excel
 * Read Ranges from Excel, Convert to a format that rules can use
 * 
 * Based on Sample from Apache POI
 * 
 * @author paulbrowne
 * 
 */
public class RuleflowExample {

	
	private static Log log = LogFactory.getLog(RuleflowExample.class);

	private static final String EXCEL_DATA_FILE = "chocolate-data.xls";

	private static final String EXCEL_OUTPUT_FILE = "chocolate-output.xls";

	// the name of the sheet the we log files to
	private static final String EXCEL_LOG_WORKSHEET_NAME = "log";

	private static final String[] RULES_FILES = new String[] {
			"ruleflow-rules.drl"};

	private static final String RULEFLOW_FILE="trading.rf";
	
	private static final String RULEFLOW_ID = "ruleflow-sample";

	
	/**
	 * Read an excel file and spit out what we find.
	 * 
	 * @param args
	 *            Expect one argument that is the file to read.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Open our Excel file using Apache Poi
		// This method searches for our file in a number of places on disk
		InputStream inputFromExcel = RuleflowExample.class
				.getClassLoader().getResourceAsStream(EXCEL_DATA_FILE);

		if (null == inputFromExcel) {
			throw new FileNotFoundException("Cannot find file:"
					+ EXCEL_DATA_FILE);
		} else {
			log.info("found file:" + EXCEL_DATA_FILE);
		}

		// Convert this into a (POI) Workbook
		HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inputFromExcel));

		// Convert the cell
		RangeHolder ranges = RangeConvertor.convertExcelToCells(wb);
		HashMap<String, Object> globals = new HashMap<String, Object>();

		// Create a new Excel Logging object
		ExcelLogger excelLogger = new ExcelLogger();

		// Load and fire our rules files against the data
		StatefulSession statefulSession = new RuleRunner().getStatefulSession(RULES_FILES,null, RULEFLOW_FILE, globals,
				excelLogger);

		
		
		//Unlike the stateless session, we have control over when we fire our rules
		log.debug("============ Firing Rules =========");


		//Put all our objects in
		Collection<Object> allRangeValues = ranges.getAllRangesAndCells();
		for (Object fact: allRangeValues){
			statefulSession.insert(fact);
	    }
		
		//start the session
		statefulSession.startProcess(RULEFLOW_ID);
		
		
		statefulSession.fireAllRules();

		log.debug("============ End Firing Rules =========");
		
	

		// update the excel spreadsheet with the result of our rules
		RangeConvertor.convertCellsToExcel(wb, ranges);

		// update the excel spreadsheet with our log file
		excelLogger.flush(wb, EXCEL_LOG_WORKSHEET_NAME);

		// Write out modified Excel sheet
		FileOutputStream outputFromExcel = new FileOutputStream(
				EXCEL_OUTPUT_FILE);
		wb.write(outputFromExcel);
		outputFromExcel.close();

		// Close our input work book
		inputFromExcel.close();

		//Close the session
		statefulSession.dispose();
		
		// complete
		log.info("Finished");
	}

}
