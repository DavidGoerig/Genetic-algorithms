/*
 * Assessment 1
 * David Goerig
 * djg53
 */

import java.lang.Math;
import java.util.*;

class GeneticAlgoBool {

	public static boolean[] getSol() {
		boolean[] solution = {true};

		boolean[] sol2 = new boolean[100];
		for(int i=0;i< sol2.length; i++){
			sol2[i]= (Math.random()>0.5);
		}

		return sol2;
	}
}
