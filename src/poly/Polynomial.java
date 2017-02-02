package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		Node front = this.poly;
		Polynomial newp = new Polynomial();
		
		while ((front != null) && (p.poly != null))
		{
			if(front.term.degree == p.poly.term.degree)
			{
				if (front.term.coeff + p.poly.term.coeff != 0)
				{
					newp.poly = addToEnd(newp.poly, new Node((front.term.coeff + p.poly.term.coeff), front.term.degree, null));
					front = front.next;
					p.poly = p.poly.next;
				}
				
				else
				{
					front = front.next;
					p.poly = p.poly.next;
				}
			}
			
			else if (front.term.degree < p.poly.term.degree)
			{
				newp.poly = addToEnd(newp.poly, new Node(front.term.coeff, front.term.degree, null));
				front = front.next;;
			}
			
			else
			{
				newp.poly = addToEnd(newp.poly, new Node(p.poly.term.coeff, p.poly.term.degree, null));
				p.poly = p.poly.next;
			}
		}
		//When out of terms in poly
		while (front != null)
		{
			newp.poly = addToEnd(newp.poly, new Node(front.term.coeff, front.term.degree, null));
			front = front.next;
		}
		//When out of terms in front
		while (p.poly != null)
		{
			newp.poly = addToEnd(newp.poly, new Node(p.poly.term.coeff, p.poly.term.degree, null));
			p.poly = p.poly.next;
		}
		
		return newp;
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		if ((this == null) || this.poly == null)
		{
			return null;
		}
		
		if ((p == null) || (p.poly == null))
		{
			return null;
		}
		
		Polynomial finalP = new Polynomial();
		finalP.poly = new Node (0, 0, null);
		
		Node origPtr = this.poly; //Ptr for first file polynomial
		
		
		
		Node prodTerm = null; //Stores term product
		
		while (true)
		{
			if (origPtr == null)
			{
				break;
			}
			
			if (origPtr.term.coeff == 0)
			{
				origPtr = origPtr.next;
				continue;
			}
			
			Node multPtr = p.poly; //Ptr for second file poly
			Node finalPtr = finalP.poly; //Ptr for product poly
			while (true)
			{
				if (multPtr == null)
				{
					break;
				}
				
				if (multPtr.term.coeff == 0)
				{
					multPtr = multPtr.next;
					continue;
				}
				
				prodTerm = multHelp(origPtr, multPtr);
				
				finalPtr = addNode(finalPtr, prodTerm);
				
				multPtr = multPtr.next;
			}
			
			origPtr = origPtr.next;
		}
		
		finalP.poly = finalP.poly.next;
		
		return finalP;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		/** COMPLETE THIS METHOD **/
		float result = 0f;
		Node ptr = this.poly;
		
		while(ptr != null)
		{
			result += (ptr.term.coeff * power(x, ptr.term.degree));
			ptr = ptr.next;
		}
		
		return result;
	}
	
	//Helper Methods

	private float power(float x, int degree)
	{
		float answer = 1;
		
		if (degree == 0)
		{
			return 1;
		}
		
		for (int i = 0; i < degree; i++)
		{
			answer *= x;
		}
		
		return answer;
	}
	
	private Node multHelp(Node x, Node y)
	{
		if ((x == null) || (y == null))
		{
			return null;
		}
		
		else
		{
			return (new Node((x.term.coeff * y.term.coeff), (x.term.degree + y.term.degree), null));
		}
	}
	
	private Node addToEnd(Node front, Node newpol)
	{
		if (front == null)
		{
			return newpol;
		}
		
		if (newpol.term.degree == 0)
		{
			return newpol;
		}
		
		for (Node ptr = front; ptr != null; ptr = ptr.next)
		{
			if (ptr.next == null)
			{
				ptr.next = newpol;
				break;
			}
		}
		
		return front;
	}
	
	private Node addNode(Node prev, Node n)
	{
		Node ptr = prev;
		
		while (true)
		{
			if (ptr.next == null)
			{
				break;
			}
			
			if (ptr.next.term.degree < n.term.degree)
			{
				ptr = ptr.next;
			}
			
			else //ptr.term.degree > n.term.degree
			{
				break;
			}
			
		}
		
		if (ptr.next == null)
		{
			ptr.next = n;
			return n;
		}
		
		if (ptr.next.term.degree == n.term.degree)
		{
			ptr.next.term.coeff = (ptr.next.term.coeff + n.term.coeff);
			return ptr;
		}
		
		n.next = ptr.next;
		ptr.next = n;
		
		return ptr;
	}
	
	//Helper Methods
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
