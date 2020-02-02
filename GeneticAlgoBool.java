/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {
	private int solSize = 100;
	private int popSize = 10000;
	private int nbrSampleSelection = 100;
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

	/**
	 * @desc Genetic algorithm for the problem 1 in the assessment
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	GeneticAlgoBool() {
		stopCondition = StopCond.CONTINUE;
		createSamplesArrays();
		valFitWeightArray = new ArrayList<double[]>();
	}

	/**
	 * @desc create the samples for creating the population (in arraylist)
	 * @param /
	 * @author David Goerig
	 * @id djg53
	 */
	private void createSamplesArrays() {
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
	private void computeFitnessOnSample() {
		double[] fit = Assess.getTest2(samp.get(0));
		int solIndex = 0;
		double min = fit[0];
		double weight = fit[1];

		for (int i = 0; i < popSize; i++) {
			fit = Assess.getTest2(samp.get(i));
			valFitWeightArray.add(fit);
			if (fit[0] < min && fit[1] <= 500) {
				min = fit[0];
				weight = fit[1];
				solIndex = i;
			}
		}
		if (min == 0 && weight <= 500) {
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
	private boolean[] selection() {
		double[] fit = Assess.getTest2(samp.get(0));
		double min = fit[0];
		double weight = fit[1];

		int selected_samp = 0;
		int tmp;

		for (int i = 0; i < nbrSampleSelection; i++) {
			tmp = (int)(Math.random() * popSize) % popSize;
			while (tmp >= popSize) {
				tmp = (int)(Math.random() * popSize) % popSize;
			}
			fit = valFitWeightArray.get(tmp);
			if (min > fit[0]  && fit[1] <= 500) {
				min = fit[0];
				weight = fit[1];
				selected_samp = tmp;
			}
		}
		//System.out.println(min);
		return samp.get(selected_samp).clone();
	}

	/**
	 * @desc apply mutation on some candidates
	 * @param candidate (double)
	 * @author David Goerig
	 * @id djg53
	 */
	private void mutOnCandidat(boolean[] solution) {
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
	private void createConcurrentSample() {
		int index;
		ArrayList<boolean[]> temporary = new ArrayList<boolean[]>();

		sampTemp.clear();
		for (int i = 0; i < popSize; i++) {
			boolean[] solution = selection();

			index = (int)(Math.random() * popSize) % popSize;
			while (index >= popSize) {
				index = (int)(Math.random() * popSize) % popSize;
			}
			mutOnCandidat(solution);
			// TODO crossOnCandidat(solution, samp.get(index), nbrCrossPoint);
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
	public static boolean[] getSol() {
		/*computeFitnessOnSample();
		while (stopCondition == StopCond.CONTINUE) {
			createConcurrentSample();
			computeFitnessOnSample();
		}
		return samp.get(solIndexx);*/
		boolean[] ret = new boolean[100];
		for (int j = 0; j < 100; j++) {
			ret[j] = (Math.random()>0.5);
		}
		return ret;
	}
}
