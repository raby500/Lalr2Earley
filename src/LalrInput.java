
public class LalrInput {
	// prec 2d array , unary operator start with "una"
	// format is "op, priority"
	static String Prec_Array[] = { 
			"|->,100",
			"/,100",
			":,100",
			"->,99",
			"=>,99",
			"<->,95",
			"\\/,85", //need to escape \ char
			"/\\,80", //need to escape \ char
			"una¬,75",
			"una~,75",
			"=,70",
			"≠,70",
			"∈,70",
			"∉,70",
			" ,70",
			"||,70",
			"::,60",
			":+,60",
			"+,50",
			"-,50",
			"U,50",
			"++,50",
			",10"
	};
	
	// format is "op , left or right"
	static String Assoc_Array[] = {
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
	};

	static String Grammar[] = { 
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

	};

}
