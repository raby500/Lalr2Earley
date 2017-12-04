package lalr_2_earley_converter;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ontopt.pen.EarleyParser;
import ontopt.pen.Grammar;
import ontopt.pen.GrammarException;
import ontopt.pen.PrettyPrint;
import ontopt.pen.SimpleSentence;
import ontopt.pen.Tree;
import ontopt.pen.Word;
import ontopt.pen.BabySteps.BabyLexer;

public class Test_Lalr_2_Earley {
	public static void main(String args[]) throws GrammarException {
		Generate.convert(LalrInput.Prec_Assoc_Array, LalrInput.Grammar);
		System.out.print(EarleyOutput.Grammar);
		Grammar g = new Grammar(EarleyOutput.Grammar);
		EarleyParser parser = new EarleyParser(g);
		Tree node;
		PrettyPrint outputter = new PrettyPrint(System.out);

		BabyLexer lex = new BabyLexer();
		lex.declareToken(Pattern.compile("\\d+"), "NUM");
		lex.declareToken(Pattern.compile("[a-zA-Z]+\\w*"), "ID");
		
		for (String buffer: LalrInput.program){
			System.out.println("\n***** Derivacoes para: \n" + buffer);
			System.out.println("");
			List<Word> tokens = lex.tokenize(buffer);
			ArrayList<Tree> pt = parser.parseSentence(new SimpleSentence(tokens));
			for (int i = 0; i < pt.size(); i++) {
				node = (Tree) pt.get(i);
				outputter.print(node, true, false, 0);
			}
			System.out.println(pt);
		}
		
		
		
	}
	
	
}
