package edu.uprm.ece.icom4035.polynomial;

public interface Term {
	
	public double getCoefficient();
	
	public int getExponent();
	
	public double evaluate(double x);
	
}
