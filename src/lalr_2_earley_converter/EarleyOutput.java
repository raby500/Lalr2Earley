package lalr_2_earley_converter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EarleyOutput {
	public static Map<Integer, List<String>> earley_grammar = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
	public static String Grammar;
	public static void print(){
		
		for(int level : earley_grammar.keySet()){
			
			System.out.println("level = " + Integer.toString(level) );
			
			for (String rule_str :earley_grammar.get(level) ){
				System.out.println(rule_str);
			}
		}
		
	}
}

