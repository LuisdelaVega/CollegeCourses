package edu.uprm.ece.icom4035.mytest;

import edu.uprm.ece.icom4035.polynomial.Polynomial;
import edu.uprm.ece.icom4035.polynomial.PolynomialImp;

public class MyTest {

	public static void main(String[] args) {

		PolynomialImp P1 = new PolynomialImp("3x^5+2x^4+x^3");
		PolynomialImp P2 = new PolynomialImp("x^2+2");

		Polynomial P3 = P1.add(P2);
		System.out.printf("Add-> P3: %s", P3);
		
		Polynomial P4 = P2.add(P1);
		System.out.printf("\nAdd-> P3: %s", P4);
		
		Polynomial P5 = P1.subtract(P2);
		System.out.printf("\nsubtract-> P3: %s", P5);
		
		Polynomial P6 = P2.subtract(P1);
		System.out.printf("\nsubtract-> P3: %s", P6);
		
		Polynomial P7 = P1.multiply(P2);
		System.out.printf("\nMultiply-> P3: %s", P7);
		
		Polynomial P8 = P2.multiply(P1);
		System.out.printf("\nMultiply-> P3: %s", P8);
		
		Polynomial P9 = P1.multiply(-1);
		System.out.printf("\nMultiply by -1-> P3: %s", P9);
		
		Polynomial P10 = P2.multiply(2);
		System.out.printf("\nMultiply by 2-> P3: %s", P10);
		
		Polynomial P11 = P1.derivative();
		System.out.printf("\nderivative-> P3: %s", P11);
		
		Polynomial P12 = P2.derivative();
		System.out.printf("\nderivative-> P3: %s", P12);
		
		Polynomial P13 = P1.indefiniteIntegral();
		System.out.printf("\nindefiniteIntegral-> P3: %s", P13);
		
		Polynomial P14 = P2.indefiniteIntegral();
		System.out.printf("\nindefiniteIntegral-> P3: %s", P14);
		
		double P15 = P1.definiteIntegral(0, 2);
		System.out.printf("\ndefiniteIntegral-> P3: %s", P15);
		
		double P16 = P2.definiteIntegral(1, 2);
		System.out.printf("\ndefiniteIntegral-> P3: %s", P16);
		
		double P17 = P1.evaluate(3);
		System.out.printf("\nevaluate-> P3: %s", P17);
		
		double P18 = P2.evaluate(0);
		System.out.printf("\nevaluate-> P3: %s", P18);
		
		boolean P19 = P1.equals(P2);
		System.out.printf("\nequals-> P3: %s", P19);
	}

}
