/**
 * Author: Luis R. de la Vega López
 */

package edu.uprm.ece.icom4035.polynomial;

import java.util.Iterator;
import java.util.StringTokenizer;
import edu.uprm.ece.icom4035.list.*;

public class PolynomialImp implements Polynomial {

	//private fields
	private ListFactory<Term> factory;
	private List<Term> terms;

	/**
	 * Creates a new PolynomialImp object with value 0.
	 */
	public PolynomialImp(){
		this.factory = new ArrayListFactory<Term>();
		this.terms = this.factory.newInstance();
		this.fromString("0");
	}

	/**
	 * Creates a new PolynomialImp object that represents the given polynomial string
	 * @param string - Polynomial string representation
	 */
	public PolynomialImp(String string) {
		this.factory = new ArrayListFactory<Term>();
		this.terms = this.factory.newInstance();
		this.fromString(string);
	}

	@Override
	public Iterator<Term> iterator() {
		return this.terms.iterator();
	}

	@Override
	public Polynomial add(Polynomial P2) {
		String value = "";
		double coefficient = 0;
		int exponent = 0;
		int index = 0;
		boolean usedTerm = false;
		for(Term p2: P2){
			usedTerm = false; //I have not yet added the current term to the string variable value
			while(index < this.terms.size()){
				if(this.terms.get(index).getExponent() > p2.getExponent()){
					coefficient = this.terms.get(index).getCoefficient();
					exponent = this.terms.get(index).getExponent();
				}
				else if(this.terms.get(index).getExponent() == p2.getExponent()){
					coefficient = (this.terms.get(index).getCoefficient() + p2.getCoefficient());
					exponent = this.terms.get(index).getExponent();
					usedTerm = true; //I have added the current term to the string variable value
				}
				else if(this.terms.get(index).getExponent() < p2.getExponent()){
					break;
				}

				value+= this.value(coefficient, exponent);
				index++;
			}
			if(!usedTerm) //If I have not yet added the current term to the string variable value then I will add it now
				value+= this.value(p2.getCoefficient(), p2.getExponent());
		}
		//Adds the remaining terms (if there are any) after the for loop ends
		while(index < this.terms.size()){
			coefficient = this.terms.get(index).getCoefficient();
			exponent = this.terms.get(index).getExponent();

			value+= this.value(coefficient, exponent);
			index++;
		}

		value = value.substring(0, value.length()-1);
		return new PolynomialImp(value);
	}

	@Override
	public Polynomial subtract(Polynomial P2) {
		if(this.equals(P2)){
			return new PolynomialImp();
		}

		Polynomial p = P2.multiply(-1);
		return this.add(p); //Easy enough
	}

	@Override
	public Polynomial multiply(Polynomial P2) {
		if(P2.equals(new PolynomialImp()))
			return new PolynomialImp();
		List<Polynomial> polynomials = new ArrayListFactory<Polynomial>().newInstance();
		int newExponent = 0;
		double newCoefficient = 0;
		for(Term p2: P2){
			String value = "";
			for(int i = 0; i < this.terms.size(); i++){
				newCoefficient = this.terms.get(i).getCoefficient()*p2.getCoefficient();
				newExponent = this.terms.get(i).getExponent() + p2.getExponent();

				value+= this.value(newCoefficient, newExponent);
			}

			value = value.substring(0, value.length()-1);
			polynomials.add(new PolynomialImp(value));

		}

		Polynomial result = polynomials.get(0);
		for(int i = 1; i < polynomials.size(); i++){
			result = result.add(polynomials.get(i));
		}

		return result;
	}

	@Override
	public Polynomial multiply(double c) {
		if(c == 0){
			return new PolynomialImp();
		}
		String value = "";
		int newCoefficient = 0;
		for(int i = 0; i < this.terms.size(); i++){
			newCoefficient = (int) (this.terms.get(i).getCoefficient() * c);
			value += this.value(newCoefficient, this.terms.get(i).getExponent());
		}
		value = value.substring(0, value.length()-1);
		return new PolynomialImp(value);
	}

	@Override
	public Polynomial derivative() {
		String value = "";
		int newExponent = 0;
		double newCoefficient = 0;
		for(Term t: this){
			if(t.getExponent() > 0){
				newCoefficient = t.getCoefficient() * t.getExponent();
				newExponent = t.getExponent()-1;

				value+= this.value(newCoefficient, newExponent);
			}
		}

		value = value.substring(0, value.length()-1);
		return new PolynomialImp(value);
	}

	@Override
	public Polynomial indefiniteIntegral() {
		String value = "";
		int newExponent = 0;
		double newCoefficient = 0;
		for(Term t: this){
			newExponent = t.getExponent() + 1;
			newCoefficient = t.getCoefficient()/(newExponent*1.0);

			value+= this.value(newCoefficient, newExponent);
		}

		value += "1"; //Represents the unknown constant value
		return new PolynomialImp(value);
	}

	@Override
	public double definiteIntegral(double a, double b) {
		Polynomial integral = this.indefiniteIntegral();
		double result = integral.evaluate(b) - integral.evaluate(a); 
		return result;
	}

	@Override
	public int degree() {
		return this.terms.get(0).getExponent();
	}

	@Override
	public double evaluate(double x) {
		double result = 0;
		for(Term t: this){
			result+= t.evaluate(x);
		}
		return result;
	}

	@Override
	public boolean equals(Polynomial P) {
		return this.toString().equals(P.toString());
	}

	/**
	 * Creates the terms of the polynomial from the given string in the constructor
	 * @param str - Polynomial string representation
	 */
	private void fromString(String str) {
		StringTokenizer strTok = new StringTokenizer(str, "+");
		String nextStr = null;
		Term nextTerm = null;
		this.terms.clear();
		while (strTok.hasMoreElements()) {
			nextStr = (String) strTok.nextElement();
			nextTerm = TermImp.fromString(nextStr);
			// private method to store a new term into a polynomial
			this.addTerm(nextTerm);
		}

	}

	/**
	 * Calls the add method of the ArrayList<Term>() class.
	 * @param nextTerm - The next term to be added to the list
	 */
	private void addTerm(Term nextTerm) {
		this.terms.add(nextTerm);
	}

	/**
	 * Returns a string representation of the target polynomial.
	 */
	public String toString(){
		String result = "";
		for(int i = 0; i < this.terms.size() - 1; i++){
			if(this.terms.get(i).getExponent() > 1)
				result = result + this.terms.get(i).getCoefficient() + "0x^" + this.terms.get(i).getExponent() + "+";
			else if(this.terms.get(i).getExponent() == 1)
				result = result + this.terms.get(i).getCoefficient() + "0x+";
		}
		if(this.terms.get(this.terms.size()-1).getExponent() > 1
				|| this.terms.get(this.terms.size()-1).getExponent() < 0){
			result = result + this.terms.get(this.terms.size()-1).getCoefficient()
					+ "0x^" + this.terms.get(this.terms.size()-1).getExponent();
		}
		else if(this.terms.get(this.terms.size()-1).getExponent() == 1){
			result = result + this.terms.get(this.terms.size()-1).getCoefficient() +"0x";
		}
		else if(this.terms.get(this.terms.size()-1).getExponent() == 0){
			result = result + this.terms.get(this.terms.size()-1).getCoefficient() +"0";
		}

		return result;
	}

	/**
	 * Returns the corresponding string representation of each coefficient and exponent of a polynomial.
	 * @param - newCoefficient The coefficient of the variable x
	 * @param - newExponent The degree the variable x is elevated to
	 * @return String representation of a part of a polynomial
	 */
	private String value(double newCoefficient, int newExponent){
		String value = "";
		if(newCoefficient != 0){
			value+=newCoefficient;
		}
		else{
			return value;
		}

		if(newExponent > 1){
			value+= "x^" + newExponent + "+";
		}
		else if(newExponent == 1){
			value+= "x" + "+";
		}
		else if(newExponent == 0){
			value+= "+";
		}

		return value;
	}
}
