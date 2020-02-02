// Assessment 1 Bastien Lécussan

import java.lang.Math;
import java.util.*;

class Example {

	private static final String NAME = "David Goerig";
	private static final String LOGIN = "djg53";

	public static void main(String[] args){
		//Do not delete/alter the next line
		long startT=System.currentTimeMillis();
	    Problem1 problem1 = new Problem1();
		final double[] sol1 = problem1.getSolution();
		final boolean[] sol2 = Problem2.getSolution();

		//Once completed, your code must submit the results you generated, including your name and login:
		//Use and adapt  the function below:
		Assess.checkIn(NAME, LOGIN, sol1, sol2);

        //Do not delete or alter the next line
        long endT= System.currentTimeMillis();
		System.out.println("Total execution time was: " +  ((endT - startT)/1000.0) + " seconds");
	  }
}
