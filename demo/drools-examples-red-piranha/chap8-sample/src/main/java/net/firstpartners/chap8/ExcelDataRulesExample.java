package net.firstpartners.chap8;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import net.firstpartners.drools.RuleRunner;
import net.firstpartners.drools.log.ExcelLogger;
import net.firstpartners.excel.Range;
import net.firstpartners.excel.RangeConvertor;
import net.firstpartners.excel.RangeHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Sample showing how we can read both data and rules from Excel
 * 
 * Read Ranges from Excel, Convert to a format that rules can use
 * 
 * Based on Sample from Apache POI
 * 
 * @author paulbrowne
 * 
 */
public class ExcelDataRulesExample {

	private static Log log = LogFactory.getLog(ExcelDataRulesExample.class);

	private static final String EXCEL_DATA_FILE = "chocolate-data.xls";

	private static final String EXCEL_OUTPUT_FILE = "chocolate-output.xls";

	// the name of the sheet the we log files to
	private static final String EXCEL_LOG_WORKSHEET_NAME = "log";

	private static final String[] RULES_FILES = new String[] {
			"log-rules.drl", "TradingRules.xls" };

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
		InputStream inputFromExcel = ExcelDataRulesExample.class
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

		// Log the cell contents
		log.debug("============ Excel Cell Contents In =========");
		for (Range r : ranges) {
			log.debug(r);
		}

		// Load and fire our rules files against the data
		new RuleRunner().runStatelessRules(RULES_FILES,null, ranges.getAllRangesAndCells(), globals,
				null,excelLogger);

		// Log the cell contents
		log.debug("============ Excel Cell Contents Out =========");
		for (Range r : ranges) {
			log.debug(r);
		}

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

		// complete
		log.info("Finished");
	}

}
