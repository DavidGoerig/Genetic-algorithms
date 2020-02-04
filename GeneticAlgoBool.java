/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {
	private int solSize;
	private int popSize;
	private int nbrSampleSelection;
	private int nbCrossPoint;
	private double mutRate;
	private double crossRate;


	private boolean[][] population;
	private double[][] fitnessValues;
	private boolean[][] newPopulation;
	private boolean isAcceptable;
	private int indexOfAcceptableSolution;
	private long startT;

	// Intialize the different variables
	GeneticAlgoBool(long startT, int solSize, int popSize, int nbrSampleSelection, int nbrCrossPoint, double mutRate, double crossRate) {
		this.solSize = solSize;
		this.popSize = popSize;
		this.nbrSampleSelection = nbrSampleSelection;
		this.nbCrossPoint = nbrCrossPoint;
		this.mutRate = mutRate;
		this.crossRate = crossRate;

		this.startT = startT;
		population = getInitialPopulation();
		fitnessValues = new double[popSize][2];
		newPopulation = new boolean[popSize][solSize];
		isAcceptable = false;
	}

	/**
	 * Constructs a random population with the size defined by the class.
	 * @return The new random population
	 */
	private boolean[][] getInitialPopulation() {
		boolean[][] population = new boolean[popSize][solSize];

		for (int i = 0; i < popSize; i++) {
			for (int j = 0; j < solSize; j++) {
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

		for (int i = 0; i < popSize; i++) {
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

		for (int i = 0; i < nbrSampleSelection; i++) {
			tmpIdx = (int)(Math.random() * popSize);
			if (tmpIdx >= popSize) {
				tmpIdx = popSize - 1;
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
		//System.out.println(bestFitness[0]);
		return population[index].clone();
	}

	/**
	 * Computes mutation on the new population in order to change the results.
	 * The mutation is made on the solution and stored in it, make sure it is a copy of your original solution
	 */
	private void mutation(boolean[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() * 100 % 100 < mutRate) {
				solution[i] = !solution[i];
			}
		}
	}

	/**
	 * Crossover throughs the current population with the defined rate
	 * The crossover is made on the parent1 and stored in it, make sure it is a copy of your original solution
	 */
	private void crossover(boolean[] parent1, boolean[] parent2, int nbCrossoverPoints) {
		if (Math.random() * 100 % 100 < crossRate) {
			int rdmValue = (int)(Math.random() * nbCrossoverPoints);
			int nbPoints = rdmValue >= 1 ? rdmValue : 1;
			boolean odd = Math.random() > 0.5 ? true : false;

			for (int i = 0; i < nbPoints; i++) {
				for (int j = (int)((solSize / (float) nbPoints) * i);
					 j < (int)((solSize / (float) nbPoints) * (i + 1)); j++) {
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

		for (int i = 0; i < popSize; i++) {
			boolean[] solution = tournamentSelection();

			tmpIdx = (int)(Math.random() * popSize);
			if (tmpIdx >= popSize) {
				tmpIdx = popSize - 1;
			}
			mutation(solution);
			crossover(solution, population[tmpIdx], nbCrossPoint);
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
			if ((tmpT - startT) / 1000.0 > 14) {
				isAcceptable = true;
			}
		}
		return population[indexOfAcceptableSolution];
	}
}
