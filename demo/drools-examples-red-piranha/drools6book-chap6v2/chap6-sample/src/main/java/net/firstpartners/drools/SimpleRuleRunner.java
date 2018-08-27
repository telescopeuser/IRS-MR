/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;

/**
 * Simple Rule Runner Based on code from the Drools Samples (Banking Tutorial)
 * 
 * @author PBrowne
 * 
 */
public class SimpleRuleRunner {

	private Log log = LogFactory.getLog(getClass());

	public SimpleRuleRunner() {
	}

	public void runRules(String[] rules, Object fact, String globalName,
			Object globalValue) throws Exception {

		// Convert object to List
		List<Object> facts = new ArrayList<Object>();
		facts.add(fact);
		
		// Convert Globals to HashMap
		HashMap<String, Object> globals = new HashMap<String, Object>();
		globals.put(globalName, globalValue);

		// Call the overloaded method
		runRules(rules, facts, globals);

	}

	
	/**
	 * Run the rules note that the rules files are currently only loaded the
	 * first time, then cached
	 * 
	 * @param rules
	 * @param facts
	 * @param globals
	 * @throws Exception
	 */
	public void runRules(String[] rules, List<Object> facts,
			HashMap<String, Object> globals) throws Exception {

		// From docs : quote
		// A Rule Base instance is thread safe, in the sense that you can have
		// the one instance shared across threads in your application,
		// which may be a web application, for instance.
		// The most common operation on a rulebase is to create a new rule
		// session; either stateful or stateless.
		KnowledgeBase masterRulebase = loadRules(rules);

		// Create a new stateless session
		StatelessKieSession knowledgeSession = masterRulebase.newStatelessKieSession();
		
		

		for (String o : globals.keySet()) {
			log.info("Inserting global name: " + o + " value:" + globals.get(o));
			knowledgeSession.setGlobal(o, globals.get(o));
		}
		
		// Add the logger
				log.info("Inserting handle to logger (via global)");
				knowledgeSession.setGlobal("log",
						LogFactory.getLog(SimpleRuleRunner.class));

				log.info("==================== Calling Rule Engine ====================");
				
				knowledgeSession.execute(facts);
				

	}

	private KnowledgeBase loadRules(String[] rules) throws Exception {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		for (int i = 0; i < rules.length; i++) {

			String ruleFile = rules[i];

			try{
			log.info("Loading file via Classpath: " + ruleFile);
			kbuilder.add(ResourceFactory.newClassPathResource(ruleFile,
					SimpleRuleRunner.class), ResourceType.DRL);
			} catch (RuntimeException re) {
				log.info("File not found as classpath - trying to load as filename");
				kbuilder.add(ResourceFactory.newFileResource(ruleFile),ResourceType.DRL);
				
			}
			

		}

		Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);

		return kbase;

	}



}
