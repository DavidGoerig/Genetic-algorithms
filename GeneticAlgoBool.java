/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {
	private int solSize = 100;
	private int popSize = 15000;
	private int nbrSampleSelection = 50;
	private int nbrCrossPoint = 10;
	private double mutRate = 1;
	private double crossRate = 90;
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
		double max = fit[1];
		double weight = fit[0];

		for (int i = 0; i < popSize; i++) {
			fit = Assess.getTest2(samp.get(i));
			valFitWeightArray.add(fit);
			if (fit[1] > max && fit[0] <= 500) {
				max = fit[1];
				weight = fit[0];
				solIndex = i;
			}
		}
		long endT=System.currentTimeMillis();
		if ((weight == 500) || (((endT - startT)/1000.0) >= 15 )) {
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
		double max = fit[1];
		double weight = fit[0];

		int selected_samp = 0;
		int tmp;

		for (int i = 0; i < nbrSampleSelection; i++) {
			tmp = (int)(Math.random() * popSize) % popSize;
			while (tmp >= popSize) {
				tmp = (int)(Math.random() * popSize) % popSize;
			}
			fit = valFitWeightArray.get(tmp);
			if (max < fit[1]  && fit[0] <= weight) {
				max = fit[1];
				weight = fit[0];
				selected_samp = tmp;
			}
		}

		if (weight <= 500)
			System.out.println("----------------------------------------");	
		System.out.println("Utility: " + max + "Weight" + weight);
		//System.out.println(min););
		if (max >= globalMax && weight <= 500) {
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
	private void mutOnCandidatB(boolean[] sol) {
		for (int i = 0; i < sol.length; i++) {
			if (Math.random() * 100 < mutRate) {
				if (sol[i] == true)
					sol[i] = false;
				else
					sol[i] = true;
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
			boolean[] sol = selectionB();

			index = (int)(Math.random() * popSize) % popSize;
			while (index >= popSize) {
				index = (int)(Math.random() * popSize) % popSize;
			}
			mutOnCandidatB(sol);
			crossOnCandidatB(sol, samp.get(index), nbrCrossPoint);
			sampTemp.add(sol);
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
		System.out.println("HMMM:" + Assess.getTest2(samp.get(solIndexx))[1] + " Weight: " + Assess.getTest2(samp.get(solIndexx))[0]);
		/*System.out.println("HMMM:" + Assess.getTest2(solution)[1]);
		if ((Assess.getTest2(samp.get(solIndexx)))[1] < (Assess.getTest2(solution)[1]))
			return solution;*/
		return samp.get(solIndexx);
		/*boolean[] ret = new boolean[100];
		for (int j = 0; j < 100; j++) {
			ret[j] = (Math.random()>0.5);
		}
		return ret;*/
	}
}
