/*	Date: 05/02/2016
 * 
 *  This program was written by Ricardo Sá Loureiro Ferreira da Silva,
 * 	a Computer Science student in the Faculty of Sciences, University of Oporto, Portugal.
 * 	This program was written in Java and its goal is
 *  to implement a variant of the original Davis-Putnam algorithm from 1960 [DP60,DLL62].
 *  I will try to briefly describe what each function does in the comments.
 *  
 *  Feel free to use my code for projects and learning. If someone happens to do it I would
 *  appreciate to be given credit.
 *  
 *  Any questions ask away,
 *  Thanks
 *  Ricardo Ferreira da Silva
 * 
 */
import java.util.*;
public class Main {
	
	// We will store the value of literals in this structure as we go along.
	static HashMap<String,Boolean> literalMap = new HashMap<String,Boolean>();
	public static void main(String args[])
	{
		// Main array where we will store our initial clauses.
		ArrayList<Clause> Clauses = new ArrayList<Clause>();
		Scanner in = new Scanner(System.in);
		// Creating data structure.
		System.out.println("How many clauses does your formula in the Conjuctive Normal Form have?");
		int nClauses = in.nextInt();
		in.nextLine();
		System.out.println("Please introduce your clauses.");
		System.out.println("(Input description in the readme file.");
		for(int i=0;i<nClauses;i++)
		{
			String literalsList = in.nextLine();
			String[] literals = literalsList.split(" ");
			Clause clause = new Clause();
			for(int i1=0;i1<literals.length;i1++)
			{
				clause.addLiteral(literals[i1]);
			}
			Clauses.add(clause);
		}
		// End of data structure
		if(DLL(Clauses))
		{
			System.out.println("Result: The formula is satisfactable.");
		}
		else
		{
			System.out.println("Result: The formula is  not satisfactable.");
		}
		
	}
	/*  Returns first literal found.
	 * (To be improved by choosing literal with most ocurrences
	 * wich will allow to remove more clauses at once.)
	 */
	static String pickLiteral(ArrayList<Clause> Clauses)
	{
		for(Clause c: Clauses)
		{
			return c.literals.get(0);
		}
		return "";
	}
	/*	Checks if the formula has any empty clauses. 
	 *  It should not happen. If a clause with one literal is deminished/cut
	 *  then the formula is not satisfactable.
	 */
	static boolean hasEmptyClause(ArrayList<Clause> Clauses)
	{
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 0)
			{
				return true;
			}
		}
		return false;
	}
	// This is a simple function to print a formula in a readable fashion.
	static void printClauses(ArrayList<Clause> Clauses)
	{
		String formula = "{";
		boolean first = true;
		if(Clauses.size() == 0)formula = "{EMPTY}";
		else
		{
			for(Clause c: Clauses)
			{
				if(first)
				{
					formula += c.printClause();
					first = false;
				}
				else formula += " && "+c.printClause();
			}
			formula += "}";
		}
		System.out.println(formula);
	}
	/*	This is the main algorithm.
	 * 	It receives a formula and processes it until 
	 * 	it can return true or false. 
	 */
	static boolean DLL(ArrayList<Clause> Clauses)
	{
		//Unitary Propagation
		while(true)
		{	
			String literalToRemove = searchSingleLiteral(Clauses);
			if(!literalToRemove.equals("NotFoundYet"))
			{
				printClauses(Clauses);
				System.out.println("Performing unitary propagation with: "+literalToRemove);
				removeClauses(literalToRemove,Clauses);
				cutClauses(literalToRemove,Clauses);
				printClauses(Clauses);
				if(Clauses.size() == 0) 
				{
					System.out.println("All clauses removed. Returning true.");
					return true;
				}
				if(hasFalsehood(Clauses)) 
				{
					System.out.println("Falsehood detected. Returning false.");
					return false;
				}
				else if(hasEmptyClause(Clauses))
				{
					System.out.println("Empty clause detected. Returning false.");
					return false;
				}
			}
			else
			{
				System.out.println("No single literals.");
				System.out.println("Cannot perform unitary propagation.");
				break;
			}
		}
		ArrayList<Clause> copy1 = new ArrayList<Clause>();
		ArrayList<Clause> copy2 = new ArrayList<Clause>();
		for(Clause c: Clauses)
		{
			Clause c2 = new Clause();
			for(String s: c.literals)
			{
				c2.addLiteral(s);
			}
			copy1.add(c2);
		}
		for(Clause c: Clauses)
		{
			Clause c2 = new Clause();
			for(String s: c.literals)
			{
				c2.addLiteral(s);
			}
			copy2.add(c2);
		}
		Clause clause1 = new Clause();
		Clause clause2 = new Clause();
		String l1 = pickLiteral(Clauses);
		String l2 = "";
		
		if(l1.startsWith("-")) l2 = l1.substring(1);
		else l2 = "-"+l1;
		clause1.addLiteral(l1);
		clause2.addLiteral(l2);
		copy1.add(clause1);
		copy2.add(clause2);
		//Moment of the truth
		System.out.println("Adding clause: ["+l1+"]");
		if(DLL(copy1) == true)
		{
			return true;
		}
		else
		{
			System.out.println("Trying opposite clause: ["+l2+"]");
			return DLL(copy2);
		}
	}
	/* This function gathers all single literals and then searches all
	 * the clauses for single opposites. If one is found, then the whole
	 * formula must be false. (True is returned).
	 */
	static boolean hasFalsehood(ArrayList<Clause> Clauses)
	{
		ArrayList<String> singleLiterals = new ArrayList<String>();
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 1)
			{
				singleLiterals.add(c.literals.get(0));
			}
		}
		for(String sl : singleLiterals)
		{
			String sl_opposite;
			if(sl.startsWith("-")) sl_opposite = sl.substring(1);
			else sl_opposite = "-"+sl;
			for(Clause c: Clauses)
			{
				if(c.literals.size() == 1)
				{
					if(c.literals.get(0).equals(sl_opposite))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	/*	This function takes in a literal and removes -literal
	 * 	from all clauses that have it.
	 * 	It should be noted that if a clause is left empty by this function
	 * 	then the formula is not satisfactable.
	 */
	static void cutClauses(String literal,ArrayList<Clause> Clauses)
	{
		String cutLiteral;
		if(literal.startsWith("-")) cutLiteral = literal.substring(1);
		else cutLiteral = "-"+literal;
		for(Clause c: Clauses)
		{
			c.literals.remove(cutLiteral);
		}
	}
	/*	This function takes in a literal and removes all 
	 * 	clauses where it occurs.  
	 */
	static void removeClauses(String literal,ArrayList<Clause> Clauses)
	{
		ArrayList<Clause> clausesToRemove = new ArrayList<Clause>();
		for(Clause c: Clauses)
		{
			for(String l: c.literals)
			{
				if(l.equals(literal))
				{
					clausesToRemove.add(c);
				}	
			}
		}
		for(Clause c : clausesToRemove)
		{
			Clauses.remove(c);
		}	
	}
	/*	This function finds a single literal.
	 * (To be optimized by finding the single literal with most occurrences).	 *
	 */
	static String searchSingleLiteral(ArrayList<Clause> Clauses)
	{
		String literalToRemove = "NotFoundYet";
		for(Clause c: Clauses)
		{
			if(c.literals.size() == 1)
			{
				literalToRemove = c.literals.get(0);
				if(literalToRemove.startsWith("-"))
				{
					literalMap.put(literalToRemove.substring(1),false);
				}
				else
				{
					literalMap.put(literalToRemove,true);
				}
				break;
			}
		}
		return literalToRemove;
	}
}
