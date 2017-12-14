package ontopt.pen;

import java.io.Serializable;

public class Word implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2301231701098328415L;
	public String tag;
	
	public Word(String tag)
	{
		this.tag = tag;
	}
	
	@Override
	public String toString()
	{
		return tag;
	}
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            return tag.equals(((Word) obj).tag);
        }
        return false;
    }
}
