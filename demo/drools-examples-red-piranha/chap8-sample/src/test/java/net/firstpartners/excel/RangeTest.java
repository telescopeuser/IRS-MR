package net.firstpartners.excel;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import net.firstpartners.excel.Cell;
import net.firstpartners.excel.Range;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class RangeTest {


	private Log log = LogFactory.getLog(Range.class);

	
	@Test
	public void testCellSearch() throws Exception {

		//Create the test object
		Range testRange = new Range();
		
		//Check that we can the name of the cell in the same range
		assertEquals("range_3",testRange.getCellName("range_12", 3));
		
		//Check a name that contains multiple underscores
		assertEquals("range_double_barrell_3",testRange.getCellName("range_double_barrell_12", 3));
		
	}
	
	@Test
	public void testRangeContains(){
		
		//Create the test object
		Range testRange = new Range();
		
		// add some test cells
		Cell testCellOne = new Cell();
		testCellOne.setValue("cellValue1");
		
		Cell testCellTwo = new Cell();
		testCellTwo.setValue("cellValue2");
		
		testRange.put("name1",testCellOne);
		testRange.put("name2",testCellTwo);
		
		assertTrue(testRange.getRangeContainsValue("cellValue1"));
		assertFalse(testRange.getRangeContainsValue("cellValue3")); // we don't add this , should not exist
		
		
	}

}

