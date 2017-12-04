package ontopt.pen;

public class Word
{
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
}
