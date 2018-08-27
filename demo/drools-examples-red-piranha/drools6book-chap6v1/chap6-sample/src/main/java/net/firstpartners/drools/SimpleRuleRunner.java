/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.drools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatelessSession;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;


public class SimpleRuleRunner {

	private Log log = LogFactory.getLog(getClass());

	
    public SimpleRuleRunner() {
    }

    public void runRules(String[] rules,
            Object fact,String globalName, Object globalValue) throws Exception {
    
    	//Convert object to array
    	Object[] facts = new Object[1];
    	facts[0] = fact;
    	
    	//Convert Globals to HashMap
    	HashMap<String,Object> globals = new HashMap<String,Object>();
    	globals.put(globalName, globalValue);
    	
    	//Call the overloaded method
    	runRules(rules,facts,globals);
    	
    	
    }
    
    /**
     * Run the rules
     * note that the rules files are currently only loaded the first time, then cached
     * @param rules
     * @param facts
     * @param globals
     * @throws Exception
     */
    public void runRules(String[] rules,
                         Object[] facts,
                         HashMap<String,Object> globals) throws Exception {

    	
    	//From docs : quote
    	//A Rule Base instance is thread safe, in the sense that you can have the one instance shared across threads in your application, 
    	// which may be a web application, for instance. 
    	//The most common operation on a rulebase is to create a new rule session; either stateful or stateless.
    	RuleBase masterRulebase= loadRules(rules);
		
		//Create a new stateless session
        StatelessSession workingMemory = masterRulebase.newStatelessSession();

        for (String o : globals.keySet()){
       	 log.info( "Inserting global name: " + o+" value:"+globals.get(o) );
       	  workingMemory.setGlobal(o, globals.get(o));
        }
        
        //Add the logger
        log.info("Inserting handle to logger (via global)");
        workingMemory.setGlobal("log", LogFactory.getLog(SimpleRuleRunner.class));
        
      	log.info("==================== Calling Rule Engine ====================");
        
        //Fire using the facts
        workingMemory.execute(facts);
        
    }
    
    private RuleBase loadRules(String[] rules) throws Exception{
    	  RuleBase localRuleBase = RuleBaseFactory.newRuleBase();
          PackageBuilder builder = new PackageBuilder();
          
          
          for ( int i = 0; i < rules.length; i++ ) {
              String ruleFile = rules[i];
              log.info( "Loading file: " + ruleFile ); 
              
              File checkFile = new File(ruleFile);
              if(!checkFile.exists()){
              	throw new FileNotFoundException("Cannot find file:"+ruleFile);
              } else {
              	log.info("found file:"+ruleFile);
              }
              
              
              builder.addPackageFromDrl(new FileReader( checkFile ) );
          }

          Package pkg = builder.getPackage();
          localRuleBase.addPackage( pkg );
          
          return localRuleBase;
    }
}
