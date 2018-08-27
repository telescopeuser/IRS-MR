
package net.firstpartners.fit.fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.firstpartners.drools.SimpleRuleRunner;
import fit.Parse;

/**
 * This fixture uses the JSR94 API to feed objects to a rule engine and execute
 * the rules (stateless engine instance only).
 * 
 * It makes the assumption that the rule engine is making changes on the objects
 * asserted. So this may not be suitable for all rule engine types.
 * Its very simple at this stage, as it is all I needed.
 * 
 * @author Michael Neale
 */
public class Engine extends AbstractRulesTestingFixture {

	

	private static final String PACKAGE = "Package";
	private static final String RULESET = "Ruleset";
	private static final String INSERT = "Insert";
	private static final String EXECUTE = "Execute";
	private static final String ASSERT = "Assert";
	private static final String GLOBAL = "Global";
	
	//Rules, Facts, globals
	private ArrayList<String> ruleFiles = new ArrayList<String>();
	private ArrayList facts = new ArrayList();
	HashMap<String,Object> globals = new HashMap<String,Object>();


	/**
	 * Called on a row by row basis by Fit
	 */
	void processRow(Parse cell) {
		
			
		if (cell.text().equalsIgnoreCase(EXECUTE)) {
			try {
				executeRulesEngine();
			} catch (Exception e) {
				super.exception(cell, e);
			}
		} 
		
		if (cell.text().equalsIgnoreCase(ASSERT)
				|| cell.text().equalsIgnoreCase(INSERT)) {
			
			addDomainObjects(cell.more.text());
			
		} 
		
		if (cell.text().equalsIgnoreCase(GLOBAL))
				 {
			
			addGlobalObjects(cell.more.text());
			
		} 
		
		
		if (cell.text().equalsIgnoreCase(RULESET)
				|| cell.text().equalsIgnoreCase(PACKAGE)) {
			try {
				loadRules(cell.more.text());
			} catch (Exception e) {
				super.exception(cell, e);
			}
		}
		
		//Chain any cells related to this
    	super.right(cell);
    	super.right(cell.more);
    	super.right(cell.more.more);			
	}



	private void loadRules(String rulesetURL) {
		
		ruleFiles.add(rulesetURL);
		
		
	}

	void addDomainObjects(String objName) {
		if (objName.equalsIgnoreCase("all")) {
			for (Iterator iter = getDomainObjects().all().iterator(); iter.hasNext();) {
				facts.add(iter.next());				
			} 
		} else {
			facts.add(getDomainObjects().getObject(objName));
		}
	}
	
	void addGlobalObjects(String objName) {
		
		globals.put(objName,getDomainObjects().getObject(objName));
		
	}
	
	private void executeRulesEngine() throws Exception {
		

		
	
	//	globals.put("nextRepaymentDate", new RepaymentDate(2008,07,01));
		
		
		
		SimpleRuleRunner ruleRunner = new SimpleRuleRunner();
		String[] ruleArray= (String[])ruleFiles.toArray(new String [0]);
		
		ruleRunner.runRules(ruleArray, facts, globals);
	}
	
	

}
