package lalr_2_earley_converter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Generate {

	 public static void convert(String[] Prec_Assoc_Array , String[] Grammar) {

		
		//create assoc map from Prec_Assoc_Array
		Map<String, String> assoc_map = new HashMap<String, String>();
		//create prec map from Prec_Assoc_Array
		Map<String, Integer> prec_map = new HashMap<String, Integer>();
		String prec_assoc_vals = String.join(",", Prec_Assoc_Array);

		String[] prec_assoc_parts = prec_assoc_vals.split(",");
		
		//System.out.println(prec_assoc_vals);

		for (int i = 0; i < prec_assoc_parts.length; i += 3) {
			prec_map.put(prec_assoc_parts[i], Integer.parseInt(prec_assoc_parts[i + 1]));
			assoc_map.put(prec_assoc_parts[i], prec_assoc_parts[i + 2]);
		}


		// TreeMap to be sorted by default used reverse order for high to low order 
		Map<Integer, List<MyRule>> prec_rule_map = new TreeMap<Integer, List<MyRule>>(Collections.reverseOrder());
		//parsing the grammar and creating a rule and at the same time 
		//grouping all rules with the same priority
		for (String s : Grammar) {
			MyRule rule = new MyRule(s);
			int priority = prec_map.getOrDefault(rule.op,0); // for special rules the priority is 0
			//System.out.println("inserting rule to priority " + priority + " and rule op is " + rule.op);
			//System.out.println("rule type = " + rule.type);
			rule.assoc = assoc_map.getOrDefault(rule.op, "NO-OP");
			if (prec_rule_map.get(priority) == null) {
				prec_rule_map.put(priority, new ArrayList<MyRule>());
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

			for (MyRule rule : prec_rule_map.get(current_prio)) {
				rule.current_level = current_prio;
				rule.next_level = next_prio;
				//final_rules_list.add(rule);
			}
			if ( prio_index < prec_rule_map.keySet().size() - 1 ) { // will not link E0 -> E highest
				MyRule link_levels_rule = new MyRule("E -> E");
				link_levels_rule.current_level = current_prio;
				link_levels_rule.next_level = next_prio;
				prec_rule_map.get(link_levels_rule.current_level).add(0,link_levels_rule);
				//final_rules_list.add(link_levels_rule);
			}
			
			if (prio_index == 0) { // special case rule (E -> highest level)
				MyRule first_rule = new MyRule("E -> E");
				first_rule.current_level = -1;
				first_rule.next_level = current_prio;
				prec_rule_map.get(first_rule.next_level).add(0,first_rule); // insert to start of the list
				//final_rules_list.add(first_rule);
			}
		}
		
		String grammar_str = "";
		//fill output
		for(int level : prec_rule_map.keySet()){
			
			if(EarleyOutput.earley_grammar.get(level) == null){
				EarleyOutput.earley_grammar.put(level, new ArrayList<String>());
			}
			
			List<String> output_list = EarleyOutput.earley_grammar.get(level);
			for (MyRule rule :prec_rule_map.get(level) ){
				String rule_str = rule.get_complete_rule();
				grammar_str += rule_str +"\n";
				output_list.add(rule_str);
			}
		}
		grammar_str = grammar_str.trim();
		EarleyOutput.Grammar = grammar_str;
		//System.out.print(EarleyOutput.Grammar);
		//System.out.print("END END END");
		//print output
		//EarleyOutput.print();
	}

}
