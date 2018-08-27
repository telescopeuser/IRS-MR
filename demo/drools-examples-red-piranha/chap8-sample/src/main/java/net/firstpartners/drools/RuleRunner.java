package net.firstpartners.drools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;

import net.firstpartners.drools.log.ILogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.StatelessSession;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.rule.Package;


public class RuleRunner {

	private static final String XLS_FILE_EXTENSION = ".xls";
	private Log log = LogFactory.getLog(getClass());

	
    public RuleRunner() {
    }


        
    private RuleBase loadRules(String[] rules,String dslFileName,String ruleFlowFileName) throws Exception{
    
    	  RuleBase localRuleBase = RuleBaseFactory.newRuleBase();
          PackageBuilder builder = new PackageBuilder();
          
          
          for ( int i = 0; i < rules.length; i++ ) {
              String ruleFile = rules[i];
              log.info( "Loading file: " + ruleFile ); 
          
              //Check the type of rule file, then load it
              if(ruleFile.endsWith(XLS_FILE_EXTENSION)){
            	  loadExcelRules(ruleFile,builder);  
              } else {
            	  loadRuleFile(ruleFile,dslFileName,ruleFlowFileName,builder);
              }
            
              Package pkg = builder.getPackage();
              localRuleBase.addPackage( pkg );    
              
          }
          
         
          
          return localRuleBase;
    }
    
    
    
    private void loadRuleFile(String ruleFile,String dslFileName,String ruleFlowFileName,PackageBuilder addRulesToThisPackage ) throws DroolsParserException, IOException{
    	
    	//This method is more flexible in finding resources on disk
        InputStream ruleSource = RuleRunner.class.getClassLoader().getResourceAsStream(ruleFile);

        //We must be able to find all rule files
        if(null==ruleSource){
        	throw new FileNotFoundException("Cannot find rule file:"+ruleFile);
        } else {
        	log.info("found rule file:"+ruleFile);
        }
        
        
        //Check if the user has passed in a DSL
        if(dslFileName!=null){
        	
        	//Load the rules , expanding them using the DSL Specified
            InputStream dslSource = RuleRunner.class.getClassLoader().getResourceAsStream(dslFileName);
            
          //We must be able to find all rule files
            if(null==dslSource){
            	throw new FileNotFoundException("Cannot find dsl file:"+dslFileName);
            } else {
            	log.info("found dsl file:"+ dslFileName);
            }
            
          //Load the rules , using DSL
        	addRulesToThisPackage.addPackageFromDrl(
        			new InputStreamReader(ruleSource),
        			new InputStreamReader(dslSource));
        	
        	
        	
        } else {
        	
        	//Load the rules , no DSL
        	addRulesToThisPackage.addPackageFromDrl(new InputStreamReader(ruleSource));
        }
        
        //if we've specified a ruleflow, add this to the package
        if(ruleFlowFileName!=null){
        	
        	//Load the rules , expanding them using the DSL Specified
            InputStream ruleFlowSource = RuleRunner.class.getClassLoader().getResourceAsStream(ruleFlowFileName);
        	
        	
            //We must be able to find all rule files
            if(null==ruleFlowSource){
            	throw new FileNotFoundException("Cannot find dsl file:"+ruleFlowFileName);
            } else {
            	log.info("found dsl file:"+ ruleFlowFileName);
            }
            
            addRulesToThisPackage.addRuleFlow(new InputStreamReader(ruleFlowSource));
            
        }
        
        
    }
    
    private void loadExcelRules(String excelRuleFile,PackageBuilder addRulesToThisPackage ) throws DroolsParserException, IOException{
    	
    	//This method is more flexible in finding resources on disk
        InputStream xlRuleSource = RuleRunner.class.getClassLoader().getResourceAsStream(excelRuleFile);

        if(null==xlRuleSource){
        	throw new FileNotFoundException("Cannot find file:"+excelRuleFile);
        } else {
        	log.info("found file:"+excelRuleFile);
        }
        
    	//first we compile the decision table into a whole lot of rules.
    	SpreadsheetCompiler compiler = new SpreadsheetCompiler();
    	String drl = compiler.compile(xlRuleSource, InputType.XLS);

    	//Show the DRL that is generated
    	log.debug(drl);
    	
    	////same as previous - we add the drl to our package
        addRulesToThisPackage.addPackageFromDrl(new StringReader(drl));
    }
    

    /**
     * Run the rules
     * @param rules - array of rule files that we need to load
     * @param dslFileName - optional dsl file name (can be null)
     * @param facts - Javabeans to pass to the rule engine
     * @param globals - global variables to pass to the rule engine
     * @param logger - handle to a logging object
     * @throws Exception
     */
    public void runStatelessRules(String[] rules,
    					 String dslFileName,
                         Collection<Object> facts,
                         HashMap<String,Object> globals,
                         String ruleFlowFileName,
                         ILogger logger) throws Exception {

    	
    	//The most common operation on a rulebase is to create a new rule session; either stateful or stateless.
    	RuleBase masterRulebase= loadRules(rules,dslFileName,ruleFlowFileName);
		
		//Create a new stateless session
        StatelessSession workingMemory = masterRulebase.newStatelessSession();

        for (String o : globals.keySet()){
       	 log.info( "Inserting global name: " + o+" value:"+globals.get(o) );
       	  workingMemory.setGlobal(o, globals.get(o));
        }
        
        //Add the logger
        log.info("Inserting handle to logger (via global)");
        workingMemory.setGlobal("log", logger);
        
      	log.info("==================== Calling Rule Engine ====================");
        
        //Fire using the facts
        workingMemory.execute(facts);
      
    	log.info("==================== Rules Complete ====================");
        
    }

    /**
     * Run the rules
     * @param rules - array of rule files that we need to load
     * @param dslFileName - optional dsl file name (can be null)
     * @param ruleFlowFileName - optional ruleFlow file name (can be null)
     * @param facts - Javabeans to pass to the rule engine
     * @param globals - global variables to pass to the rule engine
     * @param logger - handle to a logging object
     * @throws Exception
     */
    public StatefulSession getStatefulSession(String[] rules,
    					 String dslFileName,
    					 String ruleFlowFileName,
                         HashMap<String,Object> globals,
                         ILogger logger) throws Exception {

    	
    	//The most common operation on a rulebase is to create a new rule session; either stateful or stateless.
    	RuleBase masterRulebase= loadRules(rules,dslFileName,ruleFlowFileName);
		
		//Create a new stateful session
        StatefulSession workingMemory = masterRulebase.newStatefulSession();

        for (String o : globals.keySet()){
       	 log.info( "Inserting global name: " + o+" value:"+globals.get(o) );
       	  workingMemory.setGlobal(o, globals.get(o));
        }
        
        //Add the logger
        log.info("Inserting handle to logger (via global)");
        workingMemory.setGlobal("log", logger);
        
        return workingMemory;
        
    }


}
