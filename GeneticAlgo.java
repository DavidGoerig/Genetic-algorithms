/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgo {
	private int solSize;
	private int popSize;
	private int nbrSampleSelection;
	private int nbrCrossPoint;
	private double mutRate;
	private double crossRate;


	private double mutVal = 0.1;
	enum StopCond {
		CONTINUE,
		STOP
	}
	private ArrayList<double[]> samp  = new ArrayList<double[]>();
	private ArrayList<double[]> sampTemp = new ArrayList<double[]>();
	private double[] valFitArray;
	private StopCond stopCondition  = StopCond.CONTINUE;
	private int solIndexx;
	private int convergeCounter = 0;
	private double converge;
	private double convergeOld;

	/**
	 * @desc Genetic algorithm for the problem 1 in the assessment
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	GeneticAlgo(int solSize, int popSize, int nbrSampleSelection, int nbrCrossPoint, double mutRate, double crossRate) {
		this.solSize = solSize;
		this.popSize = popSize;
		this.nbrSampleSelection = nbrSampleSelection;
		this.nbrCrossPoint = nbrCrossPoint;
		this.mutRate = mutRate;
		this.crossRate = crossRate;
		valFitArray= new double[popSize];
		createSamplesArrays();
	}

	/**
	 * @desc create the samples for creating the population (in arraylist)
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	private void createSamplesArrays() {
		for (int i = 0; i < popSize; i++) {
			double[] temp = new double[solSize];
			for (int j = 0; j < solSize; j++) {
				temp[j] = Math.random() * Math.round(5.12 * (Math.random() - Math.random()));
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
	private int computeFitnessOnSample() {
		double min = Assess.getTest1(samp.get(0));
		int solIndex = 0;

		for (int i = 0; i < popSize; i++) {
			valFitArray[i] = Assess.getTest1(samp.get(i));
			if (valFitArray[i] < min) {
				min = valFitArray[i];
				solIndex = i;
			}
		}
		if (min < 1) {
			stopCondition = StopCond.STOP;
			return solIndex;
		}
		return (1);
	}

	/**
	 * @desc random in a max
	 * @param borne
	 * @author David Goerig
	 * @id djg53
	 */

	private int random_in_range(int range) {
		int rdm = (int)(Math.random() * range) % range;
		while (rdm >= range) {
			rdm = (int)(Math.random() * range) % range;
		}
		return rdm;
	}
	/**
	 * @desc selection function, select the best samples using tournament algo
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	private double[] selection() {
		double min = valFitArray[0];
		int selected_samp = 0;
		int tmp;
		int[] indexRdm = new int[nbrSampleSelection];
		for (int i = 0; i < nbrSampleSelection; i++) {
			indexRdm[i] = random_in_range(popSize);
		}
		for (int i = 0; i < nbrSampleSelection; i++) {
			if (min > valFitArray[indexRdm[i]]) {
				min = valFitArray[indexRdm[i]];
				selected_samp = indexRdm[i];
			}
		}
		converge_fct(min, selected_samp);
		return samp.get(selected_samp).clone();
	}

	private void converge_fct(double min, int selected_samp) {
		if (convergeCounter == 0) {
			converge = min;
			convergeOld = 0;
		}
		convergeCounter += 1;
		if (convergeCounter % 100000 == 0) {
			convergeOld = converge;
			converge = min;
			//System.out.println("--------------------------------------------------------------" + converge + " " + convergeOld);
			//if (converge >= convergeOld * 0.9999999999999 && converge <= convergeOld * 1.0099999999999) {
			if (converge - convergeOld < 0.001  && converge - convergeOld > -0.001) {
				solIndexx = selected_samp;
				stopCondition = StopCond.STOP;
			}
		}
	}

	/**
	 * @desc apply mutation on some candidates
	 * @param candidate (double)
	 * @author David Goerig
	 * @id djg53
	 */
	private void mutOnCandidat(double[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() * 100 < mutRate) {
				solution[i] += (Math.random() * Math.random()  - Math.random() * Math.random()) * mutVal;
			}
		}
	}

	/**
	 * @desc crossover on two candidates
	 * @param candidate1 candidate2 nbrCrossoverPoints
	 * @author David Goerig
	 * @id djg53
	 */
	private void crossOnCandidat(double[] candidate1, double[] candidate2, int nbCrossoverPoints) {
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
	 * @desc create concurre
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	private void createConcurrentSample() {
		int index;
		double[] solution;
		int[] indexRdm = new int[popSize];
		ArrayList<double[]> temporary = new ArrayList<double[]>();

		sampTemp.clear();
		for (int i = 0; i < popSize; i++) {
			index = (int)(Math.random() * popSize) % popSize;
			while (index >= popSize) {
				index = (int)(Math.random() * popSize) % popSize;
			}
			indexRdm[i] = index;
		}
		for (int j = 0; j < popSize; j++) {
			solution = selection();
			mutOnCandidat(solution);
			crossOnCandidat(solution, samp.get(indexRdm[j]), nbrCrossPoint);
			sampTemp.add(solution);
		}
		temporary = samp;
		samp = sampTemp;
		sampTemp = temporary;
	}

	/**
	 * @desc sorry for that function
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	private double[] try_one_more(double[] good) {
		int to_ret = 0;
		ArrayList<double[]> final_ar = new ArrayList<double[]>();
		double min = 50.0;
	
		final_ar.add(good);
		double[] roundOne = new double[solSize];
		double[] roundOneb = new double[solSize];
		double[] roundOnec = new double[solSize];
		double[] roundOned = new double[solSize];
		for(int r=0; r < good.length;r++) {
			roundOne[r] = Math.round(good[r]*1000.0)/1000.0;
			roundOneb[r] = Math.round(good[r]*100.0)/100.0;
			roundOned[r] = Math.round(good[r]*10.0)/10.0;
			roundOnec[r] = Math.round(good[r]*10000.0)/10000.0;
		}
		final_ar.add(roundOne);
		final_ar.add(roundOneb);
		final_ar.add(roundOnec);
		final_ar.add(roundOned);
		for(int i = 0; i < good.length;i++) {
			double[] solTest = new double[solSize];
			double[] solTestR = new double[solSize];
			double[] solTestRt = new double[solSize];
			double[] solTest2dec = new double[solSize];
			double[] solTest3decBisA = new double[solSize];
			double[] solTest3decBisB = new double[solSize];
			double[] solTest5dec = new double[solSize];
			for (int j=0; j<solSize;j++) {
				solTest[j] = good[i];
				solTestR[j] = Math.round(good[i]*10000.0)/10000.0;
				solTestRt[j] = Math.round(good[i]*1000.0)/1000.0;
				solTest3decBisA[j] = solTestRt[j] + 0.001;
				solTest3decBisB[j] = solTestRt[j] - 0.001;
				solTest2dec[j] = Math.round(good[i]*100.0)/100.0;
				solTest5dec[j] = Math.round(good[i]*100000.0)/100000.0;
			}
			final_ar.add(solTest);
			final_ar.add(solTest3decBisA);
			final_ar.add(solTest3decBisB);
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

	/**
	 * @desc
	 * @param
	 * @author David Goerig
	 * @id djg53
	 */
	public double[] getSol() {
		solIndexx = computeFitnessOnSample();
		while (stopCondition == StopCond.CONTINUE) {
			createConcurrentSample();
			solIndexx = computeFitnessOnSample();
		}
		return try_one_more(samp.get(solIndexx));
	}
}
