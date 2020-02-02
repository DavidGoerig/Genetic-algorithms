/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {
	private int solSize = 100;
	private int popSize = 100000;
	private int nbrSampleSelection = 100;
	private int nbrCrossPoint = 10;
	private double mutRate = 20;
	private double crossRate = 80;
	enum StopCond {
		CONTINUE,
		STOP
	}
	private ArrayList<boolean[]> samp  = new ArrayList<boolean[]>();
	private ArrayList<boolean[]> sampTemp = new ArrayList<boolean[]>();
	private ArrayList<double[]> valFitWeightArray;
	private StopCond stopCondition;
	private int solIndexx;
	private long startT;

	private boolean[] solution;
	private double globalMax = 0;

	/**
	 * @desc Genetic algorithm for the problem 1 in the assessment
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	GeneticAlgoBool(long startT) {
		this.startT = startT;
		stopCondition = StopCond.CONTINUE;
		createSamplesArraysB();
		valFitWeightArray = new ArrayList<double[]>();
	}

	/**
	 * @desc create the samples for creating the population (in arraylist)
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	private void createSamplesArraysB() {
		for (int i = 0; i < popSize; i++) {
			boolean[] temp = new boolean[solSize];
			for (int j = 0; j < solSize; j++) {
				temp[j] = (Math.random()>0.5);
			}
			this.samp.add(temp);
		}
	}

	/**
	 * @desc calc all the fitness in the population
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	private void computeFitnessOnSampleB() {
		double[] fit = Assess.getTest2(samp.get(0));
		int solIndex = 0;
		double max = fit[0];
		double weight = fit[1];

		for (int i = 0; i < popSize; i++) {
			fit = Assess.getTest2(samp.get(i));
			valFitWeightArray.add(fit);
			if (fit[0] > max && fit[1] <= 500) {
				max = fit[0];
				weight = fit[1];
				solIndex = i;
			}
		}
		long endT=System.currentTimeMillis();
		if (((endT - startT)/1000.0) >= 15 ) {
			stopCondition = StopCond.STOP;
			solIndexx = solIndex;
		}
	}

	/**
	 * @desc selection function, select the best samples using tournament algo
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	private boolean[] selectionB() {
		double[] fit = Assess.getTest2(samp.get(0));
		double max = fit[0];
		double weight = fit[1];

		int selected_samp = 0;
		int tmp;

		for (int i = 0; i < nbrSampleSelection; i++) {
			tmp = (int)(Math.random() * popSize) % popSize;
			while (tmp >= popSize) {
				tmp = (int)(Math.random() * popSize) % popSize;
			}
			fit = valFitWeightArray.get(tmp);
			if (max < fit[0]  && fit[1] <= 600) {
				max = fit[0];
				weight = fit[1];
				selected_samp = tmp;
			}
		}
		if (max >= globalMax) {
			globalMax = max;
			solution = samp.get(selected_samp);
		}
		return samp.get(selected_samp).clone();
	}

	/**
	 * @desc crossover on two candidates
	 * @param candidate1 candidate2 nbrCrossoverPoints
	 * @author David Goerig
	 * @id djg53
	 */
	private void crossOnCandidatB(boolean[] candidate1, boolean[] candidate2, int nbCrossoverPoints) {
		int rdm = 1;
		int pointNbr = 1;
		boolean isPair = true;
		double random = Math.random() * 100;
		if (random >= 50)
			isPair = false;
		if (Math.random() * 100 < crossRate) {
			rdm = (int)((Math.random() * nbCrossoverPoints) % nbCrossoverPoints);
			if (rdm != 0)
				pointNbr = rdm;
			for (int i = 0; i < pointNbr; i++) {
				for (int j = (int)((solSize / (float) pointNbr) * i);
					 j < (int)((solSize / (float) pointNbr) * (i + 1)); j++) {
					if (i % 2 == 0 && isPair) {
						candidate1[j] = candidate2[j];
					} else if (i % 2 != 0 && !isPair) {
						candidate1[j] = candidate2[j];
					}
				}
			}
		}
	}

	/**
	 * @desc apply mutation on some candidates
	 * @param candidate (double)
	 * @author David Goerig
	 * @id djg53
	 */
	private void mutOnCandidatB(boolean[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() * 100 < mutRate) {
				solution[i] = !solution[i];
			}
		}
	}

	/**
	 * @desc
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	private void createConcurrentSampleB() {
		int index;
		ArrayList<boolean[]> temporary = new ArrayList<boolean[]>();

		sampTemp.clear();
		for (int i = 0; i < popSize; i++) {
			boolean[] solution = selectionB();

			index = (int)(Math.random() * popSize) % popSize;
			while (index >= popSize) {
				index = (int)(Math.random() * popSize) % popSize;
			}
			mutOnCandidatB(solution);
			crossOnCandidatB(solution, samp.get(index), nbrCrossPoint);
			sampTemp.add(solution);
		}
		temporary = samp;
		samp = sampTemp;
		sampTemp = temporary;
	}

	/**
	 * @desc
	 * @return
	 * @author David Goerig
	 * @id djg53
	 */
	public boolean[] getSol() {
		computeFitnessOnSampleB();
		while (stopCondition == StopCond.CONTINUE) {
			createConcurrentSampleB();
			computeFitnessOnSampleB();
		}
		if ((Assess.getTest2(samp.get(solIndexx)))[0] < (Assess.getTest2(solution)[0]))
			return solution;
		return samp.get(solIndexx);
		/*boolean[] ret = new boolean[100];
		for (int j = 0; j < 100; j++) {
			ret[j] = (Math.random()>0.5);
		}
		return ret;*/
	}
}
