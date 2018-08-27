package net.firstpartners.drools.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An Adaptor that allows us to log to An Excel File
 * @author paulbrowne
 *
 */
public class ConsoleLogger implements ILogger {

	
	//We also allow this to be configured to log to console
	private Log log = LogFactory.getLog(getClass());
	

	
	public ConsoleLogger(){
		
	}
	
	public void debug(String output){
		log.debug(output);
	}
	
	public void info(String output){
		log.info(output);
	}
	
	public void exception(String output, Throwable t){
		log.error(output,t);
	}


}
