package ontopt.pen;

import java.io.OutputStream;
import java.io.PrintStream;


/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class is used to print trees containing the derivation of sentences.
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

public class PrettyPrint
{
    private PrintStream _out;

    public PrettyPrint(OutputStream out)
    {
        _out = new PrintStream(out);
    }

    public void print(Tree node, boolean printScores, boolean printWeights, int ident)
    {
    	_out.print(getIdent(ident));
    	_out.print((node.isLeaf() ? "> " : ""));
    	_out.print((printScores ? node.getScore() + ":" : ""));
    	_out.print("[" + node.root + "]");
    	_out.print((printWeights ? "(" + node.weight + ")" : ""));
    	_out.print(node.getAnnotation() != "" ? "(@ " + node.getAnnotation() + ")" : "");
    	_out.println();
    	_out.flush();

         ident++;
         for (Tree t : node.getSubtrees())
         {
             print(t, printScores, printWeights, ident);
         }
    }
    
    public void print(Tree node, int ident)
    {
    	print(node, true, true, ident);
    }

    private String getIdent(int ident)
    {
        String buffer = "";
        for (int i = 0; i < ident; i++)
        {
            //buffer += "\t";
        	buffer += "   ";
        }
        return buffer;
    }
}
