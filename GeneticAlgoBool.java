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


	private boolean[][] samp;
	private boolean[][] sampTemp;
	private double[][] valFitArray;


	private boolean stopCondition;
	private int solIndexx;
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
		samp = createSamplesArray();
		valFitArray = new double[popSize][2];
		sampTemp = new boolean[popSize][solSize];
		stopCondition = false;
	}

	/**
	 * Constructs a random population with the size defined by the class.
	 * @return The new random population
	 */
	private boolean[][] createSamplesArray() {
		boolean[][] samp = new boolean[popSize][solSize];

		for (int i = 0; i < popSize; i++) {
			for (int j = 0; j < solSize; j++) {
				samp[i][j] = Math.random() > 0.5 ? true : false;
			}
		}
		return samp;
	}

	/**
	 * Updates the fitness value of the different solutions.
	 * @return The minimum fitness value found.
	 */
	private void computeFitnessOnSample() {
		double[] topFindedFitness = null;
		int solutionIndex = -1;

		for (int i = 0; i < popSize; i++) {
			valFitArray[i] = Assess.getTest2(samp[i]);
			if (topFindedFitness == null) {
				topFindedFitness = valFitArray[i];
				solutionIndex = i;
			} else if (valFitArray[i][0] <= 500) {
				if (topFindedFitness[0] > 500) {
					topFindedFitness = valFitArray[i];
					solutionIndex = i;
				} else if (valFitArray[i][1] > topFindedFitness[1]) {
					topFindedFitness = valFitArray[i];
					solutionIndex = i;
				}
			} else {
				if (valFitArray[i][0] < topFindedFitness[0]) {
					topFindedFitness = valFitArray[i];
					solutionIndex = i;
				}
			}
		}
		solIndexx = solutionIndex;
	}

	/**
	 * Tournament selection on the current population with the defined parameter by the class.
	 * @return The selected solution
	 */
	private boolean[] selection() {
		double[] topFindedFitness = null;
		int solutionIndex = -1;
		int comparingIndex;

		for (int i = 0; i < nbrSampleSelection; i++) {
			comparingIndex = (int)(Math.random() * popSize);
			if (comparingIndex >= popSize) {
				comparingIndex = popSize - 1;
			}
			if (topFindedFitness == null) {
				topFindedFitness = valFitArray[comparingIndex];
				solutionIndex = comparingIndex;
			} else if (valFitArray[comparingIndex][0] <= 500) {
				if (topFindedFitness[0] > 500) {
					topFindedFitness = valFitArray[comparingIndex];
					solutionIndex = comparingIndex;
				} else if (valFitArray[comparingIndex][1] > topFindedFitness[1]) {
					topFindedFitness = valFitArray[comparingIndex];
					solutionIndex = comparingIndex;
				}
			} else {
				if (valFitArray[comparingIndex][0] < topFindedFitness[0]) {
					topFindedFitness = valFitArray[comparingIndex];
					solutionIndex = comparingIndex;
				}
			}
		}
		//System.out.println(bestFitness[0]);
		return samp[solutionIndex].clone();
	}

	/**
	 * Computes mutation on the new population in order to change the results.
	 * The mutation is made on the solution and stored in it, make sure it is a copy of your original solution
	 */
	private void mutOnCandidat(boolean[] indiviu) {
		for (int i = 0; i < indiviu.length; i++) {
			if (Math.random() * 100 % 100 < mutRate) {
				indiviu[i] = !indiviu[i];
			}
		}
	}

	/**
	 * Crossover throughs the current population with the defined rate
	 * The crossover is made on the parent1 and stored in it, make sure it is a copy of your original solution
	 */
	private void crossOnCandidat(boolean[] to_cross_1, boolean[] to_cross_2, int cross_pt_nbr) {
		if (Math.random() * 100 % 100 < crossRate) {
			int random = (int)(Math.random() * cross_pt_nbr);
			int nbPoints = random >= 1 ? random : 1;
			boolean odd = Math.random() > 0.5 ? true : false;

			for (int i = 0; i < nbPoints; i++) {
				for (int j = (int)((solSize / (float) nbPoints) * i);
					 j < (int)((solSize / (float) nbPoints) * (i + 1)); j++) {
					if (i % 2 == 0 && odd) {
						to_cross_1[j] = to_cross_2[j];
					} else if (i % 2 != 0 && !odd) {
						to_cross_1[j] = to_cross_2[j];
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
	private void createConcurrentSample() {
		int tmpIdx;
		boolean[][] tmp;

		for (int i = 0; i < popSize; i++) {
			boolean[] solution = selection();

			tmpIdx = (int)(Math.random() * popSize);
			if (tmpIdx >= popSize) {
				tmpIdx = popSize - 1;
			}
			mutOnCandidat(solution);
			crossOnCandidat(solution, samp[tmpIdx], nbCrossPoint);
			sampTemp[i] = solution;
		}
		tmp = samp;
		samp = sampTemp;
		sampTemp = tmp;
	}

	/**
	 * Find the solution of the given problem 1 through genetic algorithm.
	 * @return The acceptable solution
	 */
	public boolean[] getSol() {
		long tmpT;

		computeFitnessOnSample();
		while (stopCondition == false) {
			createConcurrentSample();
			computeFitnessOnSample();
			tmpT = System.currentTimeMillis();
			if ((tmpT - startT) / 1000.0 > 14) {
				stopCondition = true;
			}
		}
		return samp[solIndexx];
	}
}
