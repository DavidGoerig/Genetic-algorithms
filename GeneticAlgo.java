/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgo {

	private int solSize = 20;
	private int popSize = 15000;
	private int nbrSampleSelection = 50;
	private int nbrCrossPoint = 10;
	private double mutRate = 0.1;
	private double mutVal = 0.1;
	private double crossRate = 0.95;
	enum StopCond {
		CONTINUE,
		STOP
	}
	private ArrayList<double[]> samp;
	private ArrayList<double[]> sampTemp;
	private double[] valFitArray;
	private StopCond stopCondition;
	private int indexOfAcceptableSolution;

	GeneticAlgo() {
		samp = getInitialPopulation();
		valFitArray = new double[popSize];
		sampTemp = new ArrayList<double[]>();
		stopCondition = StopCond.CONTINUE;
	}

	private ArrayList<double[]> getInitialPopulation() {
		ArrayList<double[]> samp = new ArrayList<double[]>();

		for (int i = 0; i < popSize; i++) {
			double[] temp = new double[solSize];
			for (int j = 0; j < solSize; j++) {
				temp[j] = Math.random() * Math.round(5.12 * (Math.random() - Math.random()));
			}
			samp.add(temp);
		}
		return samp;
	}

	private void updateFitnessValuesCurrentPopulation() {
		double min = -1;
		int index = -1;

		for (int i = 0; i < popSize; i++) {
			valFitArray[i] = Assess.getTest1(samp.get(i));
			if (i == 0 || valFitArray[i] < min) {
				min = valFitArray[i];
				index = i;
			}
		}
		//System.out.println(min);
		if (min == 0 || min < 1) {
			stopCondition = StopCond.STOP;
			indexOfAcceptableSolution = index;
		}
	}

	private double[] tournamentSelection() {
		double min = -1;
		int index = -1;
		int tmpIdx;

		for (int i = 0; i < nbrSampleSelection; i++) {
			tmpIdx = (int)(Math.random() * popSize);
			if (tmpIdx >= popSize) {
				tmpIdx = popSize - 1;
			}
			if (i == 0 || valFitArray[tmpIdx] < min) {
				min = valFitArray[tmpIdx];
				index = tmpIdx;
			}
		}
		//System.out.println(min);
		return samp.get(index).clone();
	}

	private void mutation(double[] solution) {
		for (int i = 0; i < solution.length; i++) {
			if (Math.random() < mutRate) {
				solution[i] += (Math.random() - Math.random()) * mutVal;
			}
		}
	}

	private void crossover(double[] parent1, double[] parent2, int nbCrossoverPoints) {
		if (Math.random() < crossRate) {
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

	private void getNewPopulation() {
		int tmpIdx;
		ArrayList<double[]> tmp = new ArrayList<double[]>();

		sampTemp.clear();
		for (int i = 0; i < popSize; i++) {
			double[] solution = tournamentSelection();

			tmpIdx = (int)(Math.random() * popSize);
			if (tmpIdx >= popSize) {
				tmpIdx = popSize - 1;
			}
			mutation(solution);
			crossover(solution, samp.get(tmpIdx), nbrCrossPoint);
			sampTemp.add(solution);
		}
		tmp = samp;
		samp = sampTemp;
		sampTemp = tmp;
	}

	private double[] try_one_more(double[] good) {
		int to_ret = 0;
		ArrayList<double[]> final_ar = new ArrayList<double[]>();
		double min = 50.0;
	
		final_ar.add(good);
		for(int i = 0; i < good.length;i++) {
			double[] solTest = new double[solSize];
			double[] solTestR = new double[solSize];
			double[] solTestRt = new double[solSize];
			double[] solTest2dec = new double[solSize];
			double[] solTest5dec = new double[solSize];
			for (int j=0; j<solSize;j++) {
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
		while (stopCondition == StopCond.CONTINUE) {
			getNewPopulation();
			updateFitnessValuesCurrentPopulation();
		}
		return try_one_more(samp.get(indexOfAcceptableSolution));
	}
}
