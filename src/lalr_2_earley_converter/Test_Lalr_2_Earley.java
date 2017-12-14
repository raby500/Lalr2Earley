package lalr_2_earley_converter;

import java.io.*;

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

	public static void test_grammar(String file_prefix, BabyLexer lex, String prec_assoc_array[], String grammar[],
			String program[]) throws GrammarException, IOException, ClassNotFoundException {
		Generate.convert(prec_assoc_array, grammar);
		System.out.print(EarleyOutput.Grammar);
		Grammar g = new Grammar(EarleyOutput.Grammar);
		EarleyParser parser = new EarleyParser(g);
		Tree node;
		PrettyPrint outputter = new PrettyPrint(System.out);

		int index = 0;

		for (String buffer : program) {
			index++;
			System.out.println("\n***** Sentence Is: \n" + buffer);
			System.out.println("");
			List<Word> tokens = lex.tokenize(buffer);
			ArrayList<Tree> pt = parser.parseSentence(new SimpleSentence(tokens));
			String file_name = file_prefix + "-expected-result-" + index + ".ser" ;
			// Serialize
			if (LalrInput.Serialize_Flag) {
				FileOutputStream fileOut = new FileOutputStream(file_name);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(pt);
				out.close();
				fileOut.close();
			} else { // DeSerialize
				FileInputStream fileIn = new FileInputStream(file_name);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				@SuppressWarnings("unchecked")
				ArrayList<Tree> expeted_tree = (ArrayList<Tree>) in.readObject();
				in.close();
				fileIn.close();

				if (expeted_tree.equals(pt)) {
					System.out.println(
							file_prefix + "-Test-" + index + "*********************PASS*********************");
				} else {
					System.out.println(
							file_prefix + "-Test-" + index + "*********************FAILED*********************");
				}

			}
			
			for (int i = 0; i < pt.size(); i++) {
				node = (Tree) pt.get(i);
				outputter.print(node, true, false, 0);
			}
			System.out.println(pt);
		}

	}

	public static void main(String args[]) throws GrammarException, IOException, ClassNotFoundException {

		BabyLexer lex = new BabyLexer();
		lex.declareToken(Pattern.compile("\\d+"), "NUM");
		lex.declareToken(Pattern.compile("[a-zA-Z]+\\w*"), "ID");
		test_grammar("Calc", lex, LalrInput.calac_prec_assoc_array, LalrInput.calc_grammar, LalrInput.calc_program);
		test_grammar("Logic", lex, LalrInput.logic_prec_assoc_array, LalrInput.logic_grammar, LalrInput.logic_program);
	}

}
