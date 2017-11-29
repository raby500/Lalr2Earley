import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Generate {

	public static void main(String[] args) {

		//create prec map from prec array
		Map<String, Integer> prec_map = new HashMap<String, Integer>();
		String prec_vals = String.join(",", LalrInput.Prec_Array);

		String[] prec_parts = prec_vals.split(",");

		for (int i = 0; i < prec_parts.length; i += 2) {
			prec_map.put(prec_parts[i], Integer.parseInt(prec_parts[i + 1]));
		}

		/*for (String name : prec_map.keySet()) {
			String key = name.toString();
			String value = prec_map.get(name).toString();
			System.out.println(key + " " + value);
		}*/

		//create assoc map from assoc array
		Map<String, String> assoc_map = new HashMap<String, String>();
		String vals = String.join(",", LalrInput.Assoc_Array);

		String[] parts = vals.split(",");

		for (int i = 0; i < parts.length; i += 2) {
			assoc_map.put(parts[i], parts[i + 1]);
		}

		// TreeMap to be sorted by default used reverse order for high to low order 
		Map<Integer, List<Rule>> prec_rule_map = new TreeMap<Integer, List<Rule>>(Collections.reverseOrder());
		//parsing the grammar and creating a rule and at the same time 
		//grouping all rules with the same priority
		for (String s : LalrInput.Grammar) {
			Rule rule = new Rule(s);
			int priority = prec_map.getOrDefault(rule.op,0); // for special rules the priority is 0
			//System.out.println("inserting rule to priority " + priority + " and rule op is " + rule.op);
			//System.out.println("rule type = " + rule.type);
			rule.assoc = assoc_map.getOrDefault(rule.op, "NO-OP");
			if (prec_rule_map.get(priority) == null) {
				prec_rule_map.put(priority, new ArrayList<Rule>());
			}
			prec_rule_map.get(priority).add(rule);
		}
		
		//final output
		//List<Rule> final_rules_list = new ArrayList<Rule>();
		int prio_index = 0;
		for (prio_index = 0; prio_index < prec_rule_map.keySet().size(); prio_index++) {
			int current_prio = (int) prec_rule_map.keySet().toArray()[prio_index];
			int next_prio = (int) prec_rule_map.keySet().toArray()[0];// 0; // default level is zero , lowest level will link to zero level
			if (prio_index < prec_rule_map.keySet().size() - 1) {
				next_prio = (int) prec_rule_map.keySet().toArray()[prio_index + 1];
			}
			//System.out.printf("current_prio = %d , next_prio =%d %n", current_prio, next_prio);

			for (Rule rule : prec_rule_map.get(current_prio)) {
				rule.current_level = current_prio;
				rule.next_level = next_prio;
				//final_rules_list.add(rule);
			}
			if ( prio_index < prec_rule_map.keySet().size() - 1 ) { // will not link E0 -> E highest
				Rule link_levels_rule = new Rule("E -> E");
				link_levels_rule.current_level = current_prio;
				link_levels_rule.next_level = next_prio;
				prec_rule_map.get(link_levels_rule.current_level).add(0,link_levels_rule);
				//final_rules_list.add(link_levels_rule);
			}
			
			if (prio_index == 0) { // special case rule (E -> highest level)
				Rule first_rule = new Rule("E -> E");
				first_rule.current_level = -1;
				first_rule.next_level = current_prio;
				prec_rule_map.get(first_rule.next_level).add(0,first_rule); // insert to start of the list
				//final_rules_list.add(first_rule);
			}
		}
		//fill output
		for(int level : prec_rule_map.keySet()){
			
			if(EarleyOutput.earley_grammar.get(level) == null){
				EarleyOutput.earley_grammar.put(level, new ArrayList<String>());
			}
			
			List<String> output_list = EarleyOutput.earley_grammar.get(level);
			for (Rule rule :prec_rule_map.get(level) ){
				output_list.add(rule.get_complete_rule());
			}
		}
		
		//print output
		EarleyOutput.print();
	}

}
