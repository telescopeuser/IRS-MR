package net.firstpartners.drools.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * An Adaptor that allows us to log to An Excel File
 * @author paulbrowne
 *
 */
public class ExcelLogger implements ILogger {

	//For holding logging data until we can flush it
	List<String> loggedItems = new ArrayList<String>();  
	
	//We also allow this to be configured to log to console
	private Log log = LogFactory.getLog(getClass());
	

	
	public ExcelLogger(){
		
	}
	
	public void debug(String output){
		log.debug(output);
		loggedItems.add("debug:"+output);
	}
	
	public void info(String output){
		log.info(output);
		loggedItems.add("info:"+output);
	}
	
	public void exception(String output, Throwable t){
		log.error(output,t);
		loggedItems.add("exception:"+output+" "+t.getMessage());
	}

	/**
	 * Flush all the items that we have noted in the log into the workbook
	 * @param wb
	 */
	public void flush(HSSFWorkbook wb,String logWorkSheetName) {
		
		
		
		//Delete and recreate
		deleteCreateWorksheet(wb,logWorkSheetName);
		
		//Get our sheet
		HSSFSheet sheet= wb.getSheet(logWorkSheetName);
		
		//Start at A1, now work our way down , adding the logging output
		
		short currentRow =0;
		
		
		 for (String s : loggedItems){
		        
		        // Create a row and put some cells in it. Rows are 0 based.
		        HSSFRow row = sheet.createRow(currentRow);
		        
		        // Create a cell and put a value in it.
		        HSSFCell cell = row.createCell((short)0);
		        cell.setCellValue(s);

		        currentRow++;
		 }

		
		 
		 
	}
	
	private void deleteCreateWorksheet(HSSFWorkbook wb,String worksheetName){
		
		
		try{
			wb.createSheet(worksheetName);
		} catch (IllegalArgumentException iae){
			
			
			//worksheet already exists , so remove it
			HSSFSheet sheet = wb.getSheet(worksheetName);
			
			int sheetIndex= wb.getSheetIndex(sheet); 
			
			wb.removeSheetAt(sheetIndex);
			
			//now try creating it again
			wb.createSheet(worksheetName);
			
		}
		
		
		
		
	
		
		
		
	}
}
