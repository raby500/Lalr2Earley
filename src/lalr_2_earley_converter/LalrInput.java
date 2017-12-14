package lalr_2_earley_converter;

public class LalrInput {
	
	//when Serialize_Flag is up , the test program will store the output trees into serialized objects
	//when Serialize_Flag is down , the test program will read the stored expected serialized trees and compare
	public static boolean Serialize_Flag = false;
	
	public static String calc_program[] = 
		{
			"5 + 8 ^ 2",
			"4 + 1 + 5",
			"5 * x + 2",
			"5 * ( x + 2 )" ,
			"5 ^ 7 ^ 2",
			"5 - 3 * 2 / 4 + 6"
		};
	
	// prec 2d array , unary operator start with "una"
	// format is "op, priority"
	//lower number higher prority
	public static String calac_prec_assoc_array[] = {
			"^,10,R",
			"una-,20,",
			"/,30,L",
			"*,30,L",
			"-,40,L",
			"+,40,L",
			
	};
	
	public static String calc_grammar[] = {
			"E -> E + E",
			"E -> E - E",
			"E -> - E",
			"E -> E * E",
			"E -> E / E",
			"E -> E ^ E",
			"E -> ( E )",
			"E -> NUM",
			"E -> ID",
			
	};
	
	public static String logic_program[] = 
		{	
			"x = y <-> y = x",
			"x = 1 ∧ y = x + 2 -> y = 3",
			"x ∈ A :: B :: C",
			"x ∈ A ⋃ B",
			"x ∈ A ∨ x ∈ B",
			"x ∈ A ∨ x ∈ B ∧ x = 5",
			"x ≠ ( ~ x )"
			
		};
	
	public static String logic_prec_assoc_array[] = { 
			"|->,100,R",
			"/,100,R",
			":,100,R",
			"->,99,R",
			"=>,99,R",
			"<->,95,L",
			"\\/,85,L", //need to escape \ char
			"∨,85,L",
			"/\\,80,L", //need to escape \ char
			"∧,80,L",
			"una¬,75,",
			"una~,75,",
			"=,70,L",
			"≠,70,L",
			"∈,70,L",
			"∉,70,L",
			" ,70,L",
			"||,70,L",
			"::,60,R",
			":+,60,L",
			"+,50,L",
			"-,50,L",
			"⋃,50,L",
			"++,50,L",
			",10,L"
	};

	
	
	public static String logic_grammar[] = { 
			"E -> E : E",
			"E -> E / E",
			"E -> E |-> E",
			"E -> E -> E",
			"E -> E => E",
			"E -> E <-> E",
			"E -> E \\/ E", //escaping \
			"E -> E ∨ E",
			"E -> E /\\ E", //escaping \
			"E -> E ∧ E",
			"E -> ¬ E",
			"E -> ~ E",
			"E -> E = E",
			"E -> E ≠ E",
			"E -> E ∈ E",
			"E -> E ∉ E",
			"E -> E   E",
			"E -> E || E",
			"E -> E :: E",
			"E -> E :+ E",
			"E -> E + E",
			"E -> E - E",
			"E -> E ⋃ E",
			"E -> E ++ E",
			"E -> E E",
			"E -> { E }",
			"E -> ( E )",
			"E -> < >",
			"E -> ⊤",
			"E -> ⊥",
			"E -> NUM",
			"E -> ID"

	};

}
