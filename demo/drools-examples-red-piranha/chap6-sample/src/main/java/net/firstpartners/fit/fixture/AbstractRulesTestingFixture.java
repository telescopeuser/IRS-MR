
package net.firstpartners.fit.fixture;

import net.firstpartners.fit.helper.DomainObjectHolder;
import fit.Fixture;
import fit.Parse;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Base class for my rules testing fixtures.
 */
public abstract class AbstractRulesTestingFixture extends Fixture {

	/** gets to the first column - this is how rules fit testing works.
	 * Use cell.more etc.. to read the next rows. Only 3 rows in total 
	 * are read at a time.
	 */
	abstract void processRow(Parse cell);
	
	/**
	 * This will handle any comments or irrelevant rows.
	 */
	private void doFirstColumn(Parse cell) {
		String text = cell.text();
		if (text == null || text.equals("")) {
			super.ignore(cell);
			return;
		}
		processRow(cell);
	}

	public void doCell(Parse cell, int columnNumber) {
		
		switch (columnNumber) {
			case 0 : 
				doFirstColumn(cell);
			break;    			
			case 1 :
				ignore(cell);
			break;    		
			case 2 :
				ignore(cell);
			break;
		}
	}

	/** return the instance of Domain Object Holder that is being used 
	 * 
	 */
	public DomainObjectHolder getDomainObjects() {
		return DomainObjectHolder.getInstance();
	}
	
}
