package edu.uprm.ece.icom4035.polynomial;

import java.util.Iterator;

public interface Polynomial extends Iterable<Term> {
	
	/**
	 * Performs the addition of two polynomials.
	 * @param P2 - Polynomial to be added to the target polynomial
	 * @return The polynomial representation of the addition
	 */
	public Polynomial add(Polynomial P2);
	
	/**
	 * Performs the subtraction of two polynomials.
	 * @param P2 - Polynomial to be subtracted to the target polynomial
	 * @return The polynomial representation of the subtraction
	 */
	public Polynomial subtract(Polynomial P2);
	
	/**
	 * Performs the multiplication of two polynomials.
	 * @param P2 - Polynomial to be multiplied to the target polynomial
	 * @return The polynomial representation of the multiplication
	 */
	public Polynomial multiply(Polynomial P2);
	
	/**
	 * Multiplies the target polynomial by a scalar value
	 * @param c - Scalar value
	 * @return The polynomial representation of the multiplication
	 */
	public Polynomial multiply(double c);

	/**
	 * Differentiates the target polynomial
	 * @return The polynomial representation of the differentiation
	 */
	public Polynomial derivative();
	/**
	 * Solves the integral of the the target polynomial for an indefinite interval
	 * @return The polynomial representation of the integral
	 */
	public Polynomial indefiniteIntegral();
	
	/**
	 * Solves the integral of the the target polynomial for the given interval.
	 * @param a - First point in the interval of integration
	 * @param b - Last point in the interval of integration
	 * @return The value of the result for the given interval
	 */
	public double definiteIntegral(double a, double b);
	
	/**
	 * Looks for the degree of the polynomial by checking the highest value of the exponents
	 * @return The degree of the polynomial
	 */
	public int degree();
	
	/**
	 * Evaluates the target polynomial at the given value of x.
	 * @param x - The value to evaluate
	 * @return The result of the evaluation
	 */
	public double evaluate(double x);

	/**
	 * Checks if two polynomials are equal to one another.
	 * @param P - The polynomial to compare with the target
	 * @return True if the two polynomials are the same. False if the two polynomials are different.
	 */
	boolean equals(Polynomial P);
	

	

}
