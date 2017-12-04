package lalr_2_earley_converter;

public class MyRule {

	enum RuleType {
		Ignore, E2E_Binary, E2E_Unary, E2E_Link, E2ANY, ANY2E, E2E_Symetry;
	}

	public RuleType type = RuleType.Ignore;
	public String rule_str = "";
	public String arg0 = "";
	public String arg1 = "";
	public String arg2 = "";
	public String op = "NO-OP"; // you can't use "NO-OP" as your operation
	public String prefix = "";
	public String suffix = "";
	public int current_level = -1;
	public int next_level = -1;
	public String assoc = "";

	MyRule(String rule) {
		rule_str = rule;
		String[] splitStr = rule.trim().split("\\s+");
		//System.out.println(rule_str);
		//System.out.println("rule size = " + splitStr.length);
		
		//E to any handling
		boolean e_to_any_flag = true;
		String right_side_concat = "";
		if (splitStr[0].equals("E")) {
			for (int i = 1; i < splitStr.length; i++) {
				right_side_concat += (" " + splitStr[i]);
				if (splitStr[i].equals("E")) {
					e_to_any_flag = false;
				}
			}
		}
		if (e_to_any_flag) {
			type = RuleType.E2ANY;
			arg0 = splitStr[0];
			arg1 = right_side_concat;
		} else {
			if (splitStr.length == 5) {
				// rule E -> E op E
				if (splitStr[0].equals("E") && splitStr[2].equals("E") && splitStr[4].equals("E")) {
					type = RuleType.E2E_Binary;
					arg0 = splitStr[0];
					arg1 = splitStr[2];
					op = splitStr[3];
					arg2 = splitStr[4];
				} else if (splitStr[0].equals("E") && splitStr[3].equals("E") && !splitStr[2].equals("E")
						&& !splitStr[4].equals("E")) {
					// E -> x E x , example E -> ( E )
					type = RuleType.E2E_Symetry;
					arg0 = splitStr[0];
					arg1 = splitStr[3];
					prefix = splitStr[2];
					suffix = splitStr[4];
				} else {
					System.out.println("unkown rule 1");
				}
			}
			// rule E -> op E
			else if (splitStr.length == 4) {
				// rule E -> op E
				if (splitStr[0].equals("E") && !splitStr[2].equals("E") && splitStr[3].equals("E")) {
					type = RuleType.E2E_Unary;
					arg0 = splitStr[0];
					op = "una" + splitStr[2];
					arg1 = splitStr[3];
				} else if (splitStr[0].equals("E") && splitStr[2].equals("E") && splitStr[3].equals("E")) {
					// E -> E E (op is empty) or E -> E  E (op is a space)
					if (rule_str.length() == 8 || rule_str.length() == 10) {
						type = RuleType.E2E_Binary;
						arg0 = splitStr[0];
						arg1 = splitStr[2];
						arg2 = splitStr[3];
						// 8 op is empty , 10 op is space
						op = (rule_str.length() == 8) ? "" : " ";
					} else {
						System.out.println("unkown rule 2");
					}

				} else {
					System.out.println("unkown rule 3");
				}
			} else if (splitStr.length == 3) {
				if (splitStr[0].equals("E") && splitStr[2].equals("E")) {
					type = RuleType.E2E_Link;
					arg0 = splitStr[0];
					arg1 = splitStr[2];
				} else if (!splitStr[0].equals("E") && splitStr[2].equals("E")) {
					type = RuleType.ANY2E;
					arg0 = splitStr[0];
					arg1 = splitStr[2];
				} else {
					System.out.println("unkown rule 4");
					assert(false);
				}
			} else {
				System.out.println("Rule Create Error, rule is missing critical arguments");
			}
		}

	}

	String get_complete_rule() {
		String current_level_str = (current_level > -1) ? Integer.toString(current_level) : "";
		String next_level_str = (next_level > -1) ? Integer.toString(next_level) : "";

		if (type == RuleType.E2E_Binary) {

			String actual_first_arg = arg1 + current_level_str;
			String actual_second_arg = arg2 + next_level_str;

			if (assoc.equals("R")) {
				actual_first_arg = arg2 + next_level_str;
				actual_second_arg = arg1 + current_level_str;
			}
			String op_str = " " + op + " ";
			if (op.equals("")) {
				op_str = " ";
			}
			return (arg0 + current_level_str + " -> " + actual_first_arg + op_str + actual_second_arg);

		} else if (type == RuleType.E2E_Unary) {
			return (arg0 + current_level_str + " -> " + op.substring(3) + " " + arg1 + current_level_str);
		} else if (type == RuleType.E2E_Link) {
			return arg0 + current_level_str + " -> " + arg1 + next_level_str;
		} else if (type == RuleType.E2ANY) {
			return arg0 + "0" + arg1;
		} else if (type == RuleType.ANY2E) {
			return arg0 + " -> " + arg1 + "0";
		} else if (type == RuleType.E2E_Symetry) {
			return arg0 + current_level_str + " -> " + prefix + " " + arg1 + next_level_str + " " + suffix;
		} else { // Ignore type (do nothing)
			return rule_str;
		}
	}

}
