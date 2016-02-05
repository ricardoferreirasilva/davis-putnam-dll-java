import java.util.*;
public class Clause implements java.lang.Cloneable
{
	ArrayList<String> literals;
	Clause()
	{
		this.literals = new ArrayList<String>();
	}
	void addLiteral(String literal)
	{
		literals.add(literal);
	}
	String printClause()
	{
		String clause = "[";
		boolean first = true;
		for(String l : literals)
		{
			if(first)
			{
				clause += l;
				first = false;
			}
			else
			{
				clause += " || "+l;
			}
		}
		return clause+"]";
	}
}
