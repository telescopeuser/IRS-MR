/**
 * Chap 6 /7 sample, modified 
 * 
 * (c) Paul Browne, FirstPartners.net 
 */
package net.firstpartners.chap11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.firstpartners.chap11.domain.CustomerOrder;
import net.firstpartners.chap11.domain.OoompaLoompaDate;
import net.firstpartners.drools.RuleRunner;
import net.firstpartners.drools.log.ConsoleLogger;
import net.firstpartners.drools.log.ILogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is our Chocolate Shipping example
 * It aims to show
 * a) Running our rules from Java
 * b) Multiple Rules Interacting
 * @author paulbrowne
 *
 */
public class EventRulesExample {
	
	private static Log log = LogFactory.getLog(EventRulesExample.class);
	
    private static final String NEXT_AVAILABLE_SHIPMENT_DATE = "nextAvailableShipmentDate";
    
    private static final String[] RULES_FILES= new String[]{"shipping-rules.drl"};

	public static void main(String[] args) throws Exception {
		
    	//Initial order
		CustomerOrder candyBarOrder = new CustomerOrder(2000);
    	
    	HashMap<String,Object> globals = new HashMap<String,Object>();
    	globals.put(NEXT_AVAILABLE_SHIPMENT_DATE, new OoompaLoompaDate(2009,02,03));
    	
    	//Holidays
    	OoompaLoompaDate holiday2= new OoompaLoompaDate(2009,2,10);
    	OoompaLoompaDate holiday1= new OoompaLoompaDate(2009,3,17);
    	
    	log.info("===== Setup =====");
      	log.info(candyBarOrder);
    	
    	//Call the rule engine
      	log.info("==================== Calling Rule Runner ====================");
    	
    	Collection<Object> facts = new ArrayList<Object>();
    	facts.add(candyBarOrder);
    	facts.add(holiday1);
    	facts.add(holiday2);
    	
    	
    	
  // Create a new Excel Logging object
		ILogger logger = new ConsoleLogger();

        
     // Load and fire our rules files against the data
		new RuleRunner().runStatelessRules(RULES_FILES,null/*nodsl*/, facts, globals,
				null/*noruleflow*/,logger);
        
        //Look at the results
        log.info("======= Results - shipping schedule =======");
    	log.info(candyBarOrder);
    }
}
