package ontopt.pen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class BabySteps
{
	static final String GRAMMAR = 
			"P -> | P S\n" +
			"S -> E ;\n" + 
			"S -> § = E ;\n" +
			"E -> E ^ X | X\n" +
			"X -> X + T | X - T | T\n" +
			"T -> T * F | T / F | F\n" +
			"F -> # | § | - F | ( E )";
	
	public static void main(String[] args) throws GrammarException
	{
		String program = "x = 12 ; 6 / x + ( 5 + 8 ) * 11 ;";
		BabyLexer lex = new BabyLexer();
		lex.declareToken(Pattern.compile("\\d+"), "#");
		lex.declareToken(Pattern.compile("\\w+"), "§");
		List<Word> tokens = lex.tokenize(program);
		Grammar g = new Grammar(GRAMMAR);
		EarleyParser e = new EarleyParser(g);
		ArrayList<Tree> pt = e.parseSentence(new SimpleSentence(tokens));
		System.out.println(pt);
	}
	
	public static class BabyLexer implements Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8192450900782066543L;
		private Map<String, Pattern> patterns;
		
		public BabyLexer()
		{
			patterns = new TreeMap<>();
		}
		
		public void declareToken(Pattern pat, String cat)
		{
			patterns.put(cat, pat);
		}
		
		public String cat(String token)
		{
			for (Entry<String, Pattern> e : patterns.entrySet()) {
				Matcher m = e.getValue().matcher(token);
				if (m.matches())
					return e.getKey();
			}
			return token;  // individual category
		}
		
		public static class Token extends Word
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -1992719405093944875L;
			public String text;
			
			public Token(String tag, String value)
			{
				super(tag);
				this.text = value;
			}
			
			@Override
			public String toString()
			{
				if (tag.equals(text)) { return tag; }
				else {
					return String.format("%s:%s", tag, text);
				}
			}
			
		    @Override
		    public boolean equals(Object obj) {
		        if (obj instanceof Token) {
		            return super.equals(obj) && text.equals(((Token) obj).text);
		        }
		        return false;
		    }
		}
		
		public List<Word> tokenize(String text)
		{
			List<Word> l = new ArrayList<>();
			for (String tok : text.split("\\s+")) {
				l.add(new Token(cat(tok), tok));
			}
			return l;
		}
		
		
	}
}
