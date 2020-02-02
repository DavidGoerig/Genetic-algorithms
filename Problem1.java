// Assessment 1 Bastien Lécussan

import java.lang.Math;
import java.util.*;

// This class computes the genetic algorithm for the given problem 1 of the assessment.
class Problem1 {

	// The number of candidate into a solution.
	private final int SOLUTION_SIZE = 20;
	// The number of candidate into a population. (MUST CONTAINS AT LEAST 2 SOLUTIONS)
	private final int POPULATION_SIZE = 15000;
	// The mutation rate.
	private final double MUTATION_RATE = 0.1;
	// The mutation limit. If there is a mutation, the changes will be:
	// VALUE - MUTATION_VALUE < NEW_VALUE < VALUE + MUTATION_VALUE
	private final double MUTATION_VALUE = 0.1;
	// The crossover rate.1
	private final double CROSSOVER_RATE = 0.95;
	// The number of particpant to the tournament selection.
	private final int NB_PARTICIPANT_TOURNAMENT = 50;
	// The number of crossover points into the crossover.
	private final int NB_CROSSOVER_POINTS = 10;

	// The array that contains all the current population.
	private double[][] population;
	// The array that contains the fitness values for each double[] into "population" array.
	private double[] fitnessValues;
	// The temporary array that contains the new population from the current population.
	private double[][] newPopulation;
	// It contains the previous closest fitness value to 0.
	private boolean isAcceptable;
	// It contains the index of this value in the population array.
	private int indexOfAcceptableSolution;

	// Intialize the different variables
	Problem1() {
		population = getInitialPopulation();
		fitnessValues = new double[POPULATION_SIZE];
		newPopulation = new double[POPULATION_SIZE][SOLUTION_SIZE];
		isAcceptable = false;
	}

	/**
	 * Constructs a random population with the size defined by the class.
	 * @return The new random population
	 */
	private double[][] getInitialPopulation() {
		double[][] population = new double[POPULATION_SIZE][SOLUTION_SIZE];

		for (int i = 0; i < POPULATION_SIZE; i++) {
			for (int j = 0; j < SOLUTION_SIZE; j++) {
				population[i][j] = Math.random() * Math.round(5.12 * (Math.random() - Math.random()));
			}
		}
		return population;
	}

	/**
	 * Updates the fitness value of the different solutions.
	 */
	private void updateFitnessValuesCurrentPopulation() {
		double min = -1;
		int index = -1;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			fitnessValues[i] = Assess.getTest1(population[i]);
			if (i == 0 || fitnessValues[i] < min) {
				min = fitnessValues[i];
				index = i;
			}
		}
		if (min == 0 || min < 1) {
			isAcceptable = true;
			indexOfAcceptableSolution = index;
		}
	}

	/**
	 * Tournament selection on the current population with the defined parameter by the class.
	 * @return The selected solution
	 */
	private double[] tournamentSelection() {
		double min = -1;
		int index = -1;
		int tmpIdx;

		for (int i = 0; i < NB_PARTICIPANT_TOURNAMENT; i++) {
			tmpIdx = (int)(Math.random() * POPULATION_SIZE);
			if (tmpIdx >= POPULATION_SIZE) {
				tmpIdx = POPULATION_SIZE - 1;
			}
			if (i == 0 || fitnessValues[tmpIdx] < min) {
				min = fitnessValues[tmpIdx];
				index = tmpIdx;
			}
		}
		//System.out.println(min);
		return population[index].clone();
	}

	/**
	 * Computes mutation on the new population in order to change the results.
	 * The mutation is made on the solution and stored in it, make sure it is a copy of your original solution
	 */
	private void mutation(double[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				solution[i] += (Math.random() - Math.random()) * MUTATION_VALUE;
			}
		}
	}

	/**
	 * Crossover throughs the current population with the defined rate
	 * The crossover is made on the parent1 and stored in it, make sure it is a copy of your original solution
	 */
	private void crossover(double[] parent1, double[] parent2, int nbCrossoverPoints) {
		if (Math.random() < CROSSOVER_RATE) {
			int rdmValue = (int)(Math.random() * nbCrossoverPoints);
			int nbPoints = rdmValue >= 1 ? rdmValue : 1;
			boolean odd = Math.random() > 0.5 ? true : false;

			for (int i = 0; i < nbPoints; i++) {
				for (int j = (int)((SOLUTION_SIZE / (float) nbPoints) * i);
					 j < (int)((SOLUTION_SIZE / (float) nbPoints) * (i + 1)); j++) {
					if (i % 2 == 0 && odd) {
						parent1[j] = parent2[j];
					} else if (i % 2 != 0 && !odd) {
						parent1[j] = parent2[j];
					}
				}
			}
		}
	}

	/**
	 * Update the new population by computing
	 * "Selection (tournament)"
	 * "mutation"
	 * "crossover"
	 */
	private void getNewPopulation() {
		int tmpIdx;
		double[][] tmp;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			double[] solution = tournamentSelection();

			tmpIdx = (int)(Math.random() * POPULATION_SIZE);
			if (tmpIdx >= POPULATION_SIZE) {
				tmpIdx = POPULATION_SIZE - 1;
			}
			mutation(solution);
			crossover(solution, population[tmpIdx], NB_CROSSOVER_POINTS);
			newPopulation[i] = solution;
		}
		tmp = population;
		population = newPopulation;
		newPopulation = tmp;
	}

	private double[] try_one_more(double[] good) {
		int to_ret = 0;
		ArrayList<double[]> final_ar = new ArrayList<double[]>();
		double min = 50.0;
	
		final_ar.add(good);
		for(int i = 0; i < good.length;i++) {
			double[] solTest = new double[SOLUTION_SIZE];
			double[] solTestR = new double[SOLUTION_SIZE];
			double[] solTestRt = new double[SOLUTION_SIZE];
			for (int j=0; j<SOLUTION_SIZE;j++) {
				solTest[j] = good[i];
				solTestR[j] = Math.round(good[i]*10000.0)/10000.0;
				solTestRt[j] = Math.round(good[i]*1000.0)/1000.0;
			}
			final_ar.add(solTest);
			final_ar.add(solTestR);
			final_ar.add(solTestRt);
		}
		for (int f = 0; f<final_ar.size();f++) {
			if(Assess.getTest1(final_ar.get(f)) < min) {
				System.out.println("Fitness chelou= " + Assess.getTest1(final_ar.get(f)));
				to_ret = f;
				min = Assess.getTest1(final_ar.get(f));
				System.out.println(min);
			}
		}
		return final_ar.get(to_ret);
	}

	/**
	 * Find the solution of the given problem 1 through genetic algorithm.
	 * @return The acceptable solution
	 */
	public double[] getSolution() {
		updateFitnessValuesCurrentPopulation();
		while (isAcceptable == false) {
			getNewPopulation();
			updateFitnessValuesCurrentPopulation();
		}
		return try_one_more(population[indexOfAcceptableSolution]);
		/*for (int i = -50000; i < 50000; i++) {
			for (int j=0; j < SOLUTION_SIZE; j++) {
				population[0][j] = i / 10000.0;
			}
			if (Assess.getTest1(population[0]) == 0) {
				return population[0];
			}
		}
		return population[0];*/
	}
}
