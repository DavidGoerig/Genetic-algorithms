/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgo {

	private final int SOLUTION_SIZE = 20;
	private final int POPULATION_SIZE = 15000;
	private final double MUTATION_RATE = 0.1;
	private final double MUTATION_VALUE = 0.1;
	private final double CROSSOVER_RATE = 0.95;
	private final int NB_PARTICIPANT_TOURNAMENT = 50;
	private final int NB_CROSSOVER_POINTS = 10;

	private ArrayList<double[]> population;
	private double[] fitnessValues;
	private ArrayList<double[]> newPopulation;
	private boolean isAcceptable;
	private int indexOfAcceptableSolution;

	GeneticAlgo() {
		population = getInitialPopulation();
		fitnessValues = new double[POPULATION_SIZE];
		newPopulation = new ArrayList<double[]>();
		isAcceptable = false;
	}

	private ArrayList<double[]> getInitialPopulation() {
		ArrayList<double[]> population = new ArrayList<double[]>();

		for (int i = 0; i < POPULATION_SIZE; i++) {
			double[] temp = new double[SOLUTION_SIZE];
			for (int j = 0; j < SOLUTION_SIZE; j++) {
				temp[j] = Math.random() * Math.round(5.12 * (Math.random() - Math.random()));
			}
			population.add(temp);
		}
		return population;
	}

	private void updateFitnessValuesCurrentPopulation() {
		double min = -1;
		int index = -1;

		for (int i = 0; i < POPULATION_SIZE; i++) {
			fitnessValues[i] = Assess.getTest1(population.get(i));
			if (i == 0 || fitnessValues[i] < min) {
				min = fitnessValues[i];
				index = i;
			}
		}
		//System.out.println(min);
		if (min == 0 || min < 1) {
			isAcceptable = true;
			indexOfAcceptableSolution = index;
		}
	}

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
		return population.get(index).clone();
	}

	private void mutation(double[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				solution[i] += (Math.random() - Math.random()) * MUTATION_VALUE;
			}
		}
	}

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

	private void getNewPopulation() {
		int tmpIdx;
		ArrayList<double[]> tmp = new ArrayList<double[]>();

		newPopulation.clear();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			double[] solution = tournamentSelection();

			tmpIdx = (int)(Math.random() * POPULATION_SIZE);
			if (tmpIdx >= POPULATION_SIZE) {
				tmpIdx = POPULATION_SIZE - 1;
			}
			mutation(solution);
			crossover(solution, population.get(tmpIdx), NB_CROSSOVER_POINTS);
			newPopulation.add(solution);
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
			double[] solTest2dec = new double[SOLUTION_SIZE];
			double[] solTest5dec = new double[SOLUTION_SIZE];
			for (int j=0; j<SOLUTION_SIZE;j++) {
				solTest[j] = good[i];
				solTestR[j] = Math.round(good[i]*10000.0)/10000.0;
				solTestRt[j] = Math.round(good[i]*1000.0)/1000.0;
				solTest2dec[j] = Math.round(good[i]*100.0)/100.0;
				solTest5dec[j] = Math.round(good[i]*100000.0)/100000.0;
			}
			final_ar.add(solTest);
			final_ar.add(solTestR);
			final_ar.add(solTestRt);
			final_ar.add(solTest2dec);
			final_ar.add(solTest5dec);
		}
		for (int f = 0; f<final_ar.size();f++) {
			if(Assess.getTest1(final_ar.get(f)) < min) {
				to_ret = f;
				min = Assess.getTest1(final_ar.get(f));
			}
		}
		return final_ar.get(to_ret);
	}

	public double[] getSol() {
		updateFitnessValuesCurrentPopulation();
		while (isAcceptable == false) {
			getNewPopulation();
			updateFitnessValuesCurrentPopulation();
		}
		return try_one_more(population.get(indexOfAcceptableSolution));
	}
}
