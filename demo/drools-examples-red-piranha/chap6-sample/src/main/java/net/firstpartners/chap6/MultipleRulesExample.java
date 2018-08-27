/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.chap6;

import java.util.HashMap;

import net.firstpartners.chap6.domain.CustomerOrder;
import net.firstpartners.chap6.domain.OoompaLoompaDate;
import net.firstpartners.drools.SimpleRuleRunner;

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
public class MultipleRulesExample {
	
	private static Log log = LogFactory.getLog(MultipleRulesExample.class);
	
    private static final String NEXT_AVAILABLE_SHIPMENT_DATE = "nextAvailableShipmentDate";
    
    private static final String[] RULES_FILES= new String[]{"src/main/java/net/firstpartners/chap6/shipping-rules.drl"};

	public static void main(String[] args) throws Exception {
		
    	//Initial order
		CustomerOrder candyBarOrder = new CustomerOrder(2000);
    	
    	HashMap<String,Object> startDate = new HashMap<String,Object>();
    	startDate.put(NEXT_AVAILABLE_SHIPMENT_DATE, new OoompaLoompaDate(2009,02,03));
    	
    	//Holidays
    	OoompaLoompaDate holiday2= new OoompaLoompaDate(2009,2,10);
    	OoompaLoompaDate holiday1= new OoompaLoompaDate(2009,3,17);
    	
    	log.info("===== Setup =====");
      	log.info(candyBarOrder);
    	
    	//Call the rule engine
      	log.info("==================== Calling Rule Runner ====================");
    	
    	Object[] facts = new Object[3];
    	facts[0]=candyBarOrder;
    	facts[1]=holiday1;
    	facts[2]=holiday2;
    	
    	
    	// A lot of the running rules uses the same code. The RuleRunner (code in this project)
    	// keeps this code in one place. It needs to know
    	// - the name(s) of the files containing our rules
    	// - the fact object(s) containing the information to be passed in and out of our rules
    	// - a list of global values 
    	
        new SimpleRuleRunner().runRules( RULES_FILES, facts,startDate );
        
        //Look at the results
        log.info("======= Results - shipping schedule =======");
    	log.info(candyBarOrder);
    }
}
