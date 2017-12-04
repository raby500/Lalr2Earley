package ontopt.pen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class validates the grammar's syntax. 
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

public class GrammarValidator
{
    private Grammar grammar;

    private HashSet<Rule> validated;

    public GrammarValidator(Grammar grammar)
    {
        this.grammar = grammar;

        validated = new HashSet<>();
    }

    public void validate() throws GrammarException
    {
        validate(Grammar.PARSE_ROOT);
    }

    private void validate(Integer root) throws GrammarException
    {
        if (true) //root.intValue() > Grammar.PHRASE_LOWER_LIMIT)
        {
            List<Rule> rules = grammar.getAllRulesWithHead(root);
            if (rules == null || rules.isEmpty())
            {
            	return;/*
                throw new GrammarException("Couldn't find rule for symbol: "
                		+ grammar.getDataType(root)
                		+ " (#" + root
                		+ ") in " + grammar.getGrammarFileName());*/
            }

            ArrayList<Integer> body;

            for (Rule rule : rules) 
            {
                if (validated.contains(rule))
                    continue;

                validated.add(rule);

                body = ((PhraseRule) rule).getBody();

                for (Integer token : body)
                {
                    validate(token);
                }
            }
        }
    }
}