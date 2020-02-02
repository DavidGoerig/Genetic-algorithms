/*
* Assessment 1
* David Goerig
* djg53
 */

import java.lang.Math;
import java.util.*;

class Example {
	public static void main(String[] args){
		//Do not delete/alter the next line
		long startT=System.currentTimeMillis();

		//Edit this according to your name and login
		String name="David Goerig";
		String login = "djg53";

		GeneticAlgo ga = new GeneticAlgo();
		double[] sol1 = ga.getSol();
		boolean[] sol2 = GeneticAlgoBool.getSol();

		//System.out.println("These are the instructions of how to use the problem library.  Please make sure you follow the instructions carefully.");


		//System.out.println("For the first problem, you need to use Assess.getTest1(double, double).");

		//An example solution consists of an array  of doubles of size 50
		//Allowed values are between -5 and +5 (this is not actually checked, but no point in going beyond these values)
		//Lower fitness is better. The optimal fitness is 0 (i.e. no negative fitness values).
		//int solSize=20;
		//generate a sample solution like so:

		//double[] sol1= new double[solSize];
		//for(int j=0; j<solSize;j++){sol1[j] = Math.random()*Math.round(5.12*(Math.random() - Math.random()));}


		//get the fitness for a candidate solution in problem 1 like so
		//double fit =Assess.getTest1(sol1);


		/*System.out.println("The fitness of your example Solution is: "+ fit);


		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Now let us turn to the second problem:");
		System.out.println("A sample solution in this case is a boolean array of size 100.");
		System.out.println("I now create a random sample solution and get the weight and utility:");



		//Creating a sample solution for the second problem
		//The higher the fitness, the better, but be careful of  the weight constraint!
		boolean[] sol2 = new boolean[100];
		for(int i=0;i< sol2.length; i++){
			sol2[i]= (Math.random()>0.5);
		}

		//Now checking the fitness of the candidate solution
		double[] tmp =(Assess.getTest2(sol2));

		//The index 0 of tmp gives the weight. Index 1 gives the utility
		System.out.println("The weight is: " + tmp[0]);
		System.out.println("The utility is: " + tmp[1]);
		*/
		//Once completed, your code must submit the results you generated, including your name and login:
		//Use and adapt  the function below:
		Assess.checkIn(name,login,sol1,sol2);

		//Do not delete or alter the next line
		long endT= System.currentTimeMillis();
		System.out.println("Total execution time was: " +  ((endT - startT)/1000.0) + " seconds");



	}


}
