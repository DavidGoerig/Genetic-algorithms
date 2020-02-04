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



	enum StopCond {
		CONTINUE,
		STOP
	}
	private StopCond stopCondition;
	private int solIndexx;
	private long startT;

	/**
	 * @desc Genetic algorithm for the problem 2 in the assessment
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	GeneticAlgoBool(long startT, int solSize, int popSize, int nbrSampleSelection, int nbrCrossPoint, double mutRate, double crossRate) {
		this.solSize = solSize;
		this.popSize = popSize;
		this.nbrSampleSelection = nbrSampleSelection;
		this.nbCrossPoint = nbrCrossPoint;
		this.mutRate = mutRate;
		this.crossRate = crossRate;
		stopCondition = StopCond.CONTINUE;
		initVars(popSize, 2, solSize, false);
		this.startT = startT;
		createSamplesArray();
	}

	/**
	 * @desc init variables
	 * @param popSize arraySize solSize stopCondition
	 * @author David Goerig
	 * @id djg53
	 */
	private void initVars(int popSize, int arraySize, int solSize, boolean stopCondition) {
		valFitArray = new double[popSize][arraySize];
		sampTemp = new boolean[popSize][solSize];
		stopCondition = stopCondition;
		samp = new boolean[popSize][solSize];
	}

	/**
	 * @desc create the all the population
	 * @param /
	 * @return /
	 * @author David Goerig
	 * @id djg53
	 */
	private void createSamplesArray() {
		for (int i = 0; i < popSize; i++) {
			for (int j = 0; j < solSize; j++)
				samp[i][j] = (Math.random() > 0.5);
		}
	}

	/**
	 * @desc calc all the fitness in the population
	 * @param /
	 * @return best indiv in the pop index
	 * @author David Goerig
	 * @id djg53
	 */
	private int computeFitnessOnSample() {
		double[] fit = Assess.getTest2(samp[0]);
		double maxWeight = fit[0];
		double maxUtil = fit[1];
		int solutionIndex = 0;
		double weight;
		double util;

		for (int i = 0; i < popSize; i++) {
			valFitArray[i] = Assess.getTest2(samp[i]);
			weight = valFitArray[i][0];
			util = valFitArray[i][1];
			if (weight > 500) {
				if (weight < maxWeight) {
					maxWeight = valFitArray[i][0];
					maxUtil = valFitArray[i][1];
					solutionIndex = i;
				}
			} else {
				if (maxWeight <= 500) {
					if (util > maxUtil) {
						maxWeight = valFitArray[i][0];
						maxUtil = valFitArray[i][1];
						solutionIndex = i;
					}
				} else {
					maxWeight = valFitArray[i][0];
					maxUtil = valFitArray[i][1];
					solutionIndex = i;
				}
			}
		}
		return solutionIndex;
	}

	/**
	 * @desc crossover between two candidates
	 * @param candidate1, candidate 2, cross_pt_nbr
	 * @return
	 * @author David Goerig
	 * @id djg53
	 */
	private void crossOnCandidat(boolean[] candidate1, boolean[] candidate2, int cross_pt_nbr) {
		int rdm = 1;
		int pointNbr = 1;
		boolean isPair = true;
		double random = Math.random() * 100;
		if (random >= 50)
			isPair = false;
		if (Math.random() * 100 < crossRate) {
			rdm = (int)((Math.random() * nbCrossPoint) % nbCrossPoint);
			if (rdm != 0)
				pointNbr = rdm;
			for (int i = 0; i < pointNbr; i++) {
				int intervalSize = (int)((solSize / (float) pointNbr) * i);
				int intervalCounter = (int)((solSize / (float) pointNbr) * (i + 1));
				for (int j = intervalSize; j < intervalCounter; j++) {
					if (i % 2 == 0 && isPair == true) {
						candidate1[j] = candidate2[j];
					} else if (i % 2 != 0 && isPair == false) {
						candidate1[j] = candidate2[j];
					}
				}
			}
		}
	}

	/**
	 * @desc select the best indiv in a defined range (nbrSample selection)
	 * @param /
	 * @return selected indiv
	 * @author David Goerig
	 * @id djg53
	 */
	private boolean[] selection() {
		double weight;
		double util;

		int[] indexRdm = new int[nbrSampleSelection];
		for (int i = 0; i < nbrSampleSelection; i++) {
			indexRdm[i] = (int)(Math.random() * popSize) % popSize;
			while (indexRdm[i] >= popSize) {
				indexRdm[i] = (int)(Math.random() * popSize) % popSize;
			}
		}
		double[] fit = Assess.getTest2(samp[indexRdm[0]]);
		double maxWeight = fit[0];
		double maxUtil = fit[1];
		int solutionIndex = indexRdm[0];

		for (int j = 0; j < nbrSampleSelection; j++) {
			weight = valFitArray[indexRdm[j]][0];
			util = valFitArray[indexRdm[j]][1];
			if (weight > 500) {
				if (weight < maxWeight) {
					maxWeight = weight;
					maxUtil = util;
					solutionIndex = indexRdm[j];
				}
			} else {
				if (maxWeight <= 500) {
					if (util > maxUtil) {
						maxWeight = weight;
						maxUtil = util;
						solutionIndex = indexRdm[j];
					}
				} else {
					maxWeight = weight;
					maxUtil = util;
					solutionIndex = indexRdm[j];
				}
			}
		}
		return samp[solutionIndex].clone();
	}

	/**
	 * @desc mutation on a candidate depending on the range
	 * @param the future xmen
	 * @return /
	 * @author David Goerig
	 * @id djg53
	 */
	private void mutOnCandidat(boolean[] indiviu) {
		for (int i = 0; i < indiviu.length; i++) {
			if (Math.random() * 100 % 100 < mutRate) {
				if (indiviu[i] == true)
					indiviu[i] = false;
				else
					indiviu[i] = true;
			}
		}
	}

	/**
	 * @desc create next sample with mut, cross and selection
	 * @param /
	 * @return
	 * @author David Goerig
	 * @id djg53
	 */
	private void createConcurrentSample() {
		int index;
		boolean[][] stock;
		int[] indexRdm = new int[popSize];
		for (int i = 0; i < popSize; i++) {
			index = (int)(Math.random() * popSize);
			if (index >= popSize) {
				index = popSize - 1;
			}
		}
		for (int i = 0; i < popSize; i++) {
			boolean[] solution = selection();
			index = indexRdm[i];
			mutOnCandidat(solution);
			crossOnCandidat(solution, samp[index], nbCrossPoint);
			sampTemp[i] = solution;
		}
		stock = samp;
		samp = sampTemp;
		sampTemp = stock;
	}

	/**
	 * @desc
	 * @param /
	 * @return
	 * @author David Goerig
	 * @id djg53
	 */
	public boolean[] getSol() {
		long tmpT;

		solIndexx = computeFitnessOnSample();
		while (stopCondition == StopCond.CONTINUE) {
			createConcurrentSample();
			solIndexx = computeFitnessOnSample();
			tmpT = System.currentTimeMillis();
			if ((tmpT - startT) / 1000.0 > 14) {
				stopCondition = StopCond.STOP;
			}
		}
		return samp[solIndexx];
	}
}
