package ontopt.pen;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

//import javax.swing.tree.TreeNode;

/**
 * <p>
 * Class from PEN Parser.
 * </p>
 * <p>
 * This class represents a node in the derivation tree.
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

public class Tree implements Externalizable, Comparable<Tree>
{

    /**
     * The label associated to this node
     */
    public Word root;

    /**
     * The children of this node
     */
    public LinkedList<Tree> subtrees;

    /**
     * The parent of this node
     */
    protected Tree parent;

    /* Attached info */
    
    protected String annotation;

    protected int weight;


    /**
     * The constructor.
     */
    public Tree()
    {
        subtrees = new LinkedList<Tree>();
    }

    /**
     * The constructor
     * 
     * @param label
     *            Default label to use
     */
    public Tree(Word label, int weight, String annotation)
    {
        this();
        this.root = label;
        this.weight = weight;
        this.annotation = annotation;
    }

    /**
     * Creates a copy of this node without the parents. Used for Serialization across a network
     * 
     * @return A clone of this node
     */
    public Tree getCloneOfNode()
    {
        return getCloneOfNode(this);
    }

    /**
     * Creates a copy of the node without the parents. Used for Serialization across a network
     * 
     * @param original
     *            the node to clone
     * @return A clone of the node
     */
    private Tree getCloneOfNode(Tree original)
    {
        Tree clone = new Tree(original.root, original.weight, original.annotation);
        Iterator<Tree> children = original.getSubtrees().iterator();

        while (children.hasNext())
        {
            clone.addChild(getCloneOfNode(children.next()));
        }
        return clone;
    }

    /**
     * Sets the label of this node
     * 
     * @param label
     *            The label of the node
     */
    public void setLabel(Word label)
    {
        this.root = label;
    }

    /**
     * Sets the parent of this node
     * 
     * @param parent
     */
    public void setParent(Tree parent)
    {
        this.parent = parent;
    }

    /**
     * Sets the children of this node
     * 
     * @param children
     *            the list of children
     */
    public void setChildren(LinkedList<Tree> children)
    {
        this.subtrees = children;
    }

    /**
     * Gets the left most node
     * 
     * @return the left most node
     */
    public Tree getLeftCorner()
    {
        return getLeftCorner(this);
    }

    /**
     * Gets the left most node
     * 
     * @param node
     *            The from where to start the search
     * @return The left most node
     */
    private Tree getLeftCorner(Tree node)
    {
        Tree leftChild = (Tree) node.getSubtrees().get(0);

        if (leftChild.getSubtrees().size() == 0)
        {
            return leftChild;
        }
        else
        {
            return getLeftCorner(leftChild);
        }
    }

    /**
     * Adds a child to this node
     * 
     * @param child
     *            The child to add
     */
    public void addChild(Tree child)
    {
        child.parent = this;
        subtrees.add(child);
    }

    /**
     * Strips the leaves off the tree, if they are words
     * 
     * @return A string representing the sentence
     */
    public String stripSentence()
    {
        String sentence = "";

        for (Iterator<Tree> i = getTerminals(this).iterator(); i.hasNext();)
        {
            sentence += i.next().toString() + " ";
        }

        return sentence.trim();
    }

    /**
     * Returns a list of leaves
     * 
     * @return a list of leaves
     */
    public LinkedList<Tree> getTerminals()
    {
        return getTerminals(this);
    }

    /**
     * Returns a list of leaves
     * 
     * @param node
     *            The node from which to get the leaves from.
     * @return A list of leaves
     */
    private LinkedList<Tree> getTerminals(Tree node)
    {
        LinkedList<Tree> terminals = new LinkedList<Tree>();
        Tree n;
        Iterator<Tree> nodeChildren = node.getSubtrees().iterator();

        while (nodeChildren.hasNext())
        {
            n = nodeChildren.next();
            if (n.isLeaf())
            {
                terminals.add(n);
            }
            else
            {
                terminals.addAll(getTerminals(n));
            }
        }

        return terminals;
    }

    /**
     * Returns a list of nodes with a specified label
     * 
     * @param POSLabel
     *            The label to search for
     * @return A list of nodes with the specified label
     */
    public LinkedList<Tree> getNodes(String POSLabel)
    {
        return getNodes(this, POSLabel);
    }

    /**
     * 
     * @param node
     *            The node to from where to start the search
     * @param POSLabel
     *            the label to search for
     * @return A list nodes with the specified label.
     */
    private LinkedList<Tree> getNodes(Tree node, String POSLabel)
    {
        LinkedList<Tree> pos = new LinkedList<Tree>();
        Iterator<Tree> nodeChildren = node.getSubtrees().iterator();
        Word label = node.root;

        if (label.equals(POSLabel) || POSLabel.equals(""))
        {
            pos.add(node);
        }

        while (nodeChildren.hasNext())
        {
            pos.addAll(getNodes(nodeChildren.next(), POSLabel));
        }

        return pos;
    }

    /**
     * Implementation of the equals method. Overrides the deafult implementation.
     * 
     * @param o
     *            The object to compare to.
     * @return True if they are equal, false otherwise
     */
    public boolean equals(Object o)
    {
        if (!(o instanceof Tree))
        {
            return false;
        }

        Tree n = (Tree) o;

        if (this.root.equals(n.root))
        {
            return isEqual(this.getSubtrees(), n.getSubtrees());
        }

        return false;
    }

    /**
     * Checks if this node subsumes the parameter
     * 
     * @param anotherNode
     *            The node to compare to
     * @return Return true if this node subsumes anotherNode
     */
    protected boolean subsumes(Tree anotherNode)
    {
        if (this.root.equals(anotherNode.root))
        {
            return isSubsumed(this.getSubtrees(), anotherNode.getSubtrees());
        }

        return false;
    }

    /**
     * The recursive method for checking subsumation. Checks if the subsumer list subsumes the subsumee list
     * 
     * @param subsumer
     *            The supposed subsumer list
     * @param subsumee
     *            The supposed subsumee list
     * @return True if the first list subsumes the second
     */
    private boolean isSubsumed(LinkedList<Tree> subsumer, LinkedList<Tree> subsumee)
    {
        if (subsumer.size() == 0 && subsumee.size() == 0)
        {
            return true;
        }

        if (subsumer.size() > subsumee.size())
        {
            return false;
        }

        Tree nodeOfSubsumer;
        Tree nodeOfSubsumee;
        LinkedList<Tree> childrenOfSubsumer = new LinkedList<Tree>();
        LinkedList<Tree> childrenOfSubsumee = new LinkedList<Tree>();
        int k = 0;
        boolean found = false;

        for (int i = 0; i < subsumer.size(); i++)
        {
            if (k >= subsumee.size())
            {
                return false;
            }

            found = false;
            nodeOfSubsumer = subsumer.get(i);
            nodeOfSubsumee = subsumee.get(k);

            if (!(nodeOfSubsumer.root.equals(nodeOfSubsumee.root) && nodeOfSubsumer.parent.root.equals(nodeOfSubsumee.parent.root)))
            {
                if (subsumer.size() == subsumee.size() || k == subsumee.size() - 1)
                {
                    return false;
                }
                else
                // check if the rest of the nodes exist at this level
                {
                    k++;
                    // childrenOfSubsumee.addAll(nodeOfSubsumee.getChildren());
                    for (; k < subsumee.size(); k++)
                    {
                        nodeOfSubsumee = subsumee.get(k);
                        if ((nodeOfSubsumer.root.equals(nodeOfSubsumee.root) && nodeOfSubsumer.parent.root.equals(nodeOfSubsumee.parent.root)))
                        {
                            found = true;
                            break;
                        }
                        // childrenOfSubsumee.addAll(nodeOfSubsumee.getChildren());
                    }
                    if (!found)
                    {
                        return false;
                    }
                }
            }

            k++;

            childrenOfSubsumee.addAll(nodeOfSubsumee.getSubtrees());
            childrenOfSubsumer.addAll(nodeOfSubsumer.getSubtrees());
        }

        return isSubsumed(childrenOfSubsumer, childrenOfSubsumee);
    }

    /**
     * Checks if the two lists have the same nodes
     * 
     * @param tree1
     * @param tree2
     * @return
     */
    private boolean isEqual(LinkedList<Tree> tree1, LinkedList<Tree> tree2)
    {
        Tree node1;
        Tree node2;
        LinkedList<Tree> children1 = new LinkedList<Tree>();
        LinkedList<Tree> children2 = new LinkedList<Tree>();
        Iterator<Tree> i1;
        Iterator<Tree> i2;

        if (tree1.size() != tree2.size())
        {
            return false;
        }

        if (tree1.size() == 0)
        {
            return true;
        }

        i1 = tree1.iterator();
        i2 = tree2.iterator();
        while (i1.hasNext())
        {
            node1 = i1.next();
            node2 = i2.next();

            if (!(node1.root.equals(node2.root) && node1.parent.root.equals(node2.parent.root)))
            {
                return false;
            }
            else
            {
                children1.addAll(node1.getSubtrees());
                children2.addAll(node2.getSubtrees());
            }
        }

        return isEqual(children1, children2);
    }

    // Methods needed for TreeNode Interface
    public LinkedList<Tree> getSubtrees()
    {
        return subtrees;
    }

    /*public TreeNode getParent()
    {
        return parent;
    }*/

    /*public int getIndex(TreeNode node)
    {
        return children.indexOf(node);
    }*/

    public int getChildCount()
    {
        return subtrees.size();
    }

    /*public TreeNode getChildAt(int index)
    {
        return (TreeNode) children.get(index);
    }*/

    public boolean isLeaf()
    {
        return (subtrees == null || subtrees.size() == 0);
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public boolean isRoot()
    {
        return (parent == null);
    }

    public String toString()
    {
    	if (getChildCount() > 0) {
	    	StringBuffer sb = new StringBuffer();
	    	for (Tree child : getSubtrees()) {
	    		if (sb.length() > 0) sb.append(", ");
	    		sb.append(child);
	    	}
	    	return root + "[" + sb + "]";
    	}
    	else
    		return root.toString();
    }

    public Enumeration<Tree> children()
    {
        return (new Vector<Tree>(subtrees)).elements();
    }

    // Methods needed for Serialization
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(parent);
        out.writeObject(subtrees);
        out.writeObject(root);
    }

    @SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        parent = (Tree) in.readObject();
        subtrees = (LinkedList<Tree>) in.readObject();
        root = (Word) in.readObject();
    }

    public int getWeight()
    {
        return weight;
    }

    public String getAnnotation()
    {
        return annotation;
    }

    public int getScore()
    {
        int score = this.weight;
        for (Enumeration<Tree> en = children(); en.hasMoreElements();)
            score += en.nextElement().getScore();

        return score;
    }

    public int compareTo(Tree anotherNode)
    {
        return getScore() - anotherNode.getScore();
    }

    public Word getLabel()
    {
        return root;
    }

}