package ontopt.pen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a grammar with the processing rules, read from a text file.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2009
 * </p>
 * <p>
 * Company: CISUC
 * </p>
 * 
 * @author Nuno Seco, Hugo Gonçalo Oliveira
 * @version 1.0
 */

public class Grammar
{
    /**
     * The ID for a sentence. All Phrase types that are in upper case in the grammar file wiil be attributed a
     * unique ID.
     */
    public static final Integer PARSE_ROOT = new Integer(9999);

    public static final Integer UNKNOWN_TERMINAL = new Integer(-1);

     public static final Integer EMPTY_TERMINAL = new Integer(-2);

    /**
     * The begining ID number for new phrases.
     */
    //public static final Integer PHRASE_LOWER_LIMIT = new Integer(8000);

    /**
     * The maximum number of phrase types that the parser can deal with,
     */
    public static final Integer VARIABLES_MAX_NUMBER = new Integer(1998);

    /**
     * A Hashmap for holding the mappings between the phrases and their Integer ID.
     */
    //private HashMap<String, Integer> variables;

    private HashMap<String, Integer> terminals;

    /**
     * The next variable to attributed to a new phrase type. It is initialized with (PHRASE_LOWER_LIMIT +
     * VARIABLES_MAX_NUMBER)
     */
    //private int nextVariableID;

    private int nextTerminalID;

    /**
     * A hashmap that holds the grammar rules in the Integer ID format. Each key corresponds to a head of a
     * rule. The value is a list of all possible bodies for the head.
     */
    private HashMap<Integer, ArrayList<Rule>> grammar;

    /**
     * A map that holds a mapping between Symbols and the bodies (of rules) in which it participates
     */
    private HashMap<Integer, ArrayList<Rule>> invertedGrammar;

    /**
     * The file where this grammar is written
     */
    private String grammarFile;
    
    /**
     * The constructor
     */
    public Grammar()
    {
    	this.grammarFile = null;
    	
        grammar = new HashMap<Integer, ArrayList<Rule>>();
        invertedGrammar= new HashMap<Integer, ArrayList<Rule>>();
        //variables = new HashMap<String, Integer>();
        //variables.put("RAIZ", PARSE_ROOT);
        terminals = new HashMap<String, Integer>();
        terminals.put("<?>", UNKNOWN_TERMINAL);
        terminals.put("<>", EMPTY_TERMINAL);
        //nextVariableID = PHRASE_LOWER_LIMIT.intValue() + VARIABLES_MAX_NUMBER.intValue();
        nextTerminalID = 1;
    }
    
    public Grammar(String grammarText)
    {
    	this();
    	for (Rule rule : prettyParse(grammarText, PARSE_ROOT))
    		addRule(rule);
    }

	public void setStartSymbol(String symbol)
    {
        terminals.put(symbol, PARSE_ROOT);
    }
    
    public boolean isTerminal(Integer symbol)
    {
    	return !grammar.containsKey(symbol);
    }
    
    public String getGrammarFileName()
    {
    	return grammarFile;
    }
    
    /**
     * Transforms an Integer ID in the corresponding String representation.
     * 
     * @param token
     *            The Integer to transform
     * @return The string representation
     */
    public String getDataType(Integer token)
    {
        if (token == null)
        {
            return "";
        }

    	for (Entry<String, Integer> e : terminals.entrySet())
    		if (e.getValue().equals(token))
    			return e.getKey();
    	return null;        	
    }

    /**
     * Get all rules that begin with the specified Head. A simple lookup in the hashtable.
     * 
     * @param head
     *            The head to lookup
     * @return A list of bodies beginning with that rule.
     */
    public ArrayList<Rule> getAllRulesWithHead(Integer head)
    {
        return grammar.get(head);
    }

    public ArrayList<Rule> getAllRulesWithHead(String head)
    {
        return grammar.get(getDataType(head));
    }

    public Integer getTerminal(String word)
    {
        Integer terminal;
        terminal = (Integer) terminals.get(word);

        if (terminal == null)
            return UNKNOWN_TERMINAL;

        return terminal;

    }

    public ArrayList<Rule> getRulesContaining(Integer symbol)
    {
        return invertedGrammar.get(symbol);
    }

    public ArrayList<Rule> getRulesContaining(String symbol)
    {
        Integer integer = getDataType(symbol);
        if (integer != null)
            return getRulesContaining(integer);

        return null;
    }

    public Set<String> getAllTerminals()
    {
        return terminals.keySet();
    }

    /**
     * A String representation of this class. It Returns a string in a chart like format with all the rules.
     * 
     * @return The string representation
     */
    public String toString()
    {
        String s = new String();
        Iterator<Integer> it = grammar.keySet().iterator();

        while (it.hasNext())
        {
            s += grammar.get(it.next()).toString() + "\n";
        }

        return s;
    }

    /**
     * Adds a rule into the grammar hashmap. It first checks if the head already exists and simply adds a new
     * rule to the list. Otherwise it creates a list with the rule.
     * 
     * @param rule
     *            the rule to be added
     */
    public void addRule(Rule rule)
    {
        ArrayList<Rule> rules;

        if ((rules = grammar.get(rule.getHead())) == null)
        {
            rules = new ArrayList<Rule>();
        }

        rules.add(rule);
        grammar.put(rule.getHead(), rules);
    }

    @SuppressWarnings("unused")
	private void addRuleToInvertedGrammar(PhraseRule rule)
    {
        ArrayList<Rule> rules;
        ArrayList<Integer> body = rule.getBody();
        Integer element;

        for (Iterator<Integer> i = body.iterator(); i.hasNext();)
        {
            element = i.next();
            if ((rules = invertedGrammar.get(element)) == null)
            {
                rules = new ArrayList<Rule>();
            }

            rules.add(rule);
            invertedGrammar.put(element, rules);
        }
    }

    /**
     * Transforms a string representation into its corresponding ID.
     * 
     * @param token
     *            The string representation
     * @return The corresponding Integer ID.
     */
	public Integer getDataType(String token)
	{
		Integer id;

		if ((id = (Integer) terminals.get(token)) == null)
		{
			terminals.put(token, nextTerminalID);
			id = nextTerminalID;
			nextTerminalID++;
		}

		return id;
	}

	/***    Grammar Pretty-Parser    ***/
	
	public List<Rule> prettyParse(String rulesText, Integer startId)
	{
		List<Rule> l = new ArrayList<>();
		Integer headId = startId;
		for (String line : rulesText.split("\n")) {
			l.addAll(prettyParseRule(line, headId));
			headId = null;
		}
		return l;
	}
	
	private List<Rule> prettyParseRule(String line, Integer headId)
	{
		String[] leftright = line.split("\\s+->\\s+", 2);
		String head = leftright[0].trim();
		String[] bodies = leftright[1].trim().split("(^|\\s+)\\|\\s+");
		List<Rule> alts = new ArrayList<>();
		if (headId != null) terminals.put(head, headId);
		for (String body : bodies) {
			body = body.trim();
			ArrayList<Integer> pBody = body.equals("") ? new ArrayList<Integer>() :
				mkSymbols(body.split("\\s+"));
			alts.add(new PhraseRule(1, "", getDataType(head), pBody, this));
		}
		return alts;
	}
	
	private ArrayList<Integer> mkSymbols(String... symbols)
	{
		ArrayList<Integer> l = new ArrayList<>();
		for (String sym : symbols) {
			l.add(getDataType(sym));
		}
		return l;
	}	
}

/**
 * int index = line.indexOf(WEIGHT_SEPARATOR); if (index > 0 && index < line.length()) { weight =
 * Integer.parseInt(line.substring(0, index).trim()); line = line.substring(index + WEIGHT_SEPARATOR.length() +
 * 1); } else { weight = Rule.DEFAULT_WEIGHT; }
 * 
 * index = line.indexOf(ANNOTATION_SEPARATOR); if (index > 0 && index < line.length()) { annotation =
 * line.substring(0, index).trim(); line = line.substring(index + ANNOTATION_SEPARATOR.length() + 1); }
 * 
 * 
 */
