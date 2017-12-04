package lalr_2_earley_converter;

public class LalrInput {
	
	public static String program[] = 
		{
			"5 + 8 ^ 2",
			"4 + 1 + 5",
			"5 * x + 2"
		};
	
	// prec 2d array , unary operator start with "una"
	// format is "op, priority"
	//lower number higher prority
	public static String Prec_Assoc_Array[] = {
			"^,10,R",
			"una-,20,",
			"/,30,L",
			"*,30,L",
			"-,40,L",
			"+,40,L",
			
	};
	
	public static String Grammar[] = {
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

	/*public static String Prec_Assoc_Array[] = { 
			"|->,100,R",
			"/,100,R",
			":,100,R",
			"->,99,R",
			"=>,99,R",
			"<->,95,L",
			"\\/,85,L", //need to escape \ char
			"/\\,80,L", //need to escape \ char
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
			"U,50,L",
			"++,50,L",
			",10,L"
	};*/
	
	// format is "op , left or right"
	/*static String Assoc_Array[] = {
			"|->,R",
			"/,R",
			":,R",
			"->,R",
			"=>,R",
			"<->,L",
			"\\/,L", //need to escape \ char
			"/\\,L",
			"=,L",
			"≠,L",
			" ∈ ,L",
			"∉,L",
			" ,L",
			"||,L",
			"::,R",
			":+,L",
			"+,L",
			"-,L",
			"U,L",
			"++,L",
			",L"
	};*/

	
	
	/*public static String Grammar[] = { 
			"E -> E : E",
			"E -> E / E",
			"E -> E |-> E",
			"E -> E -> E",
			"E -> E => E",
			"E -> E <-> E",
			"E -> E \\/ E", //escaping \
			"E -> E /\\ E", //escaping \
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
			"E -> E U E",
			"E -> E ++ E",
			"E -> E E",
			"E -> { E }",
			"E -> ( E )",
			"E -> < >",
			"E -> ⊤",
			"E -> ⊥"

	};*/

}
