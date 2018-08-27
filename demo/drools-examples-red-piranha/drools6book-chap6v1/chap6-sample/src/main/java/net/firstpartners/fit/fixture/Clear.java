
package net.firstpartners.fit.fixture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.firstpartners.fit.helper.DomainObjectHolder;
import fit.Fixture;
import fit.Parse;

/**
 * This fixture will reset the domain objects.
 * 
 * @author Michael Neale
 */
public class Clear extends Fixture {
	
	private Log log = LogFactory.getLog(getClass());
	
	public void doTable(Parse p) {
		log.info("Clearing domain objects..");
		DomainObjectHolder.getInstance().reset();
		super.doTable(p);
	}
	

}
