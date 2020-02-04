/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {

	// The maximum amount of time for the program to run
	private final int AVAILABLE_TIME = 9;
	// The number of candidate into a solution.
	private final int SOLUTION_SIZE = 100;
	// The number of candidate into a population. (MUST CONTAINS AT LEAST 2 SOLUTIONS)
	private final int POPULATION_SIZE = 15000;
	// The mutation rate.
	private final double MUTATION_RATE = 0.02;
	// The crossover rate.1
	private final double CROSSOVER_RATE = 0.95;
	// The number of particpant to the tournament selection.
	private final int NB_PARTICIPANT_TOURNAMENT = 100;
	// The number of crossover points into the crossover.
	private final int NB_CROSSOVER_POINTS = 100;

	// The counter on how many times did the fitness stand under a threshold.
	// Defined to stop the GA and fine local minima.
	private int counterFitnessStall;
	// The array that contains all the current population.
	private boolean[][] population;
	// The array that contains the fitness values for each double[] into "population" array.
	private double[][] fitnessValues;
	// The temporary array that contains the new population from the current population.
	private boolean[][] newPopulation;
	// It contains the previous closest fitness value to 0.
	private boolean isAcceptable;
	// It contains the index of this value in the population array.
	private int indexOfAcceptableSolution;
	private long startT;

	// Intialize the different variables
	GeneticAlgoBool(long startT) {
		this.startT = startT;
		population = getInitialPopulation();
		fitnessValues = new double[POPULATION_SIZE][2];
		newPopulation = new boolean[POPULATION_SIZE][SOLUTION_SIZE];
		isAcceptable = false;
		counterFitnessStall = 0;
	}

	/**
	 * Constructs a random population with the size defined by the class.
	 * @return The new random population
	 */
	private boolean[][] getInitialPopulation() {
		boolean[][] population = new boolean[POPULATION_SIZE][SOLUTION_SIZE];

		for (int i = 0; i < POPULATION_SIZE; i++) {
			for (int j = 0; j < SOLUTION_SIZE; j++) {
				population[i][j] = Math.random() > 0.5 ? true : false;
			}
		}
		return population;
	}

	/**
	 * Updates the fitness value of the different solutions.
	 * @return The minimum fitness value found.
	 */
	private void updateFitnessValuesCurrentPopulation() {
		double[] bestFitness = null;
		int index = -1;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			fitnessValues[i] = Assess.getTest2(population[i]);
			if (bestFitness == null) {
				bestFitness = fitnessValues[i];
				index = i;
			} else if (fitnessValues[i][0] <= 500) {
				if (bestFitness[0] > 500) {
					bestFitness = fitnessValues[i];
					index = i;
				} else if (fitnessValues[i][1] > bestFitness[1]) {
					bestFitness = fitnessValues[i];
					index = i;
				}
			} else {
				if (fitnessValues[i][0] < bestFitness[0]) {
					bestFitness = fitnessValues[i];
					index = i;
				}
			}
		}
		indexOfAcceptableSolution = index;
	}

	/**
	 * Tournament selection on the current population with the defined parameter by the class.
	 * @return The selected solution
	 */
	private boolean[] tournamentSelection() {
		double[] bestFitness = null;
		int index = -1;
		int tmpIdx;

		for (int i = 0; i < NB_PARTICIPANT_TOURNAMENT; i++) {
			tmpIdx = (int)(Math.random() * POPULATION_SIZE);
			if (tmpIdx >= POPULATION_SIZE) {
				tmpIdx = POPULATION_SIZE - 1;
			}
			if (bestFitness == null) {
				bestFitness = fitnessValues[tmpIdx];
				index = tmpIdx;
			} else if (fitnessValues[tmpIdx][0] <= 500) {
				if (bestFitness[0] > 500) {
					bestFitness = fitnessValues[tmpIdx];
					index = tmpIdx;
				} else if (fitnessValues[tmpIdx][1] > bestFitness[1]) {
					bestFitness = fitnessValues[tmpIdx];
					index = tmpIdx;
				}
			} else {
				if (fitnessValues[tmpIdx][0] < bestFitness[0]) {
					bestFitness = fitnessValues[tmpIdx];
					index = tmpIdx;
				}
			}
		}
		System.out.println(bestFitness[0]);
		return population[index].clone();
	}

	/**
	 * Computes mutation on the new population in order to change the results.
	 * The mutation is made on the solution and stored in it, make sure it is a copy of your original solution
	 */
	private void mutation(boolean[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				solution[i] = !solution[i];
			}
		}
	}

	/**
	 * Crossover throughs the current population with the defined rate
	 * The crossover is made on the parent1 and stored in it, make sure it is a copy of your original solution
	 */
	private void crossover(boolean[] parent1, boolean[] parent2, int nbCrossoverPoints) {
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
		boolean[][] tmp;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			boolean[] solution = tournamentSelection();

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

	/**
	 * Find the solution of the given problem 1 through genetic algorithm.
	 * @return The acceptable solution
	 */
	public boolean[] getSol() {
		long tmpT;

		updateFitnessValuesCurrentPopulation();
		while (isAcceptable == false) {
			getNewPopulation();
			updateFitnessValuesCurrentPopulation();
			tmpT = System.currentTimeMillis();
			if ((tmpT - startT) / 1000.0 > AVAILABLE_TIME) {
				isAcceptable = true;
			}
		}
		return population[indexOfAcceptableSolution];
	}
}
