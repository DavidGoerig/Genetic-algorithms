// Assessment 1 Bastien Lécussan

import java.lang.Math;
import java.util.*;

class Problem2 {

	public static boolean[] getSolution() {
		boolean[] solution = {true};

		//Creating a sample solution for the second problem
		//The higher the fitness, the better, but be careful of  the weight constraint!
		boolean[] sol2 = new boolean[100];
		for(int i=0;i< sol2.length; i++){
			sol2[i]= (Math.random()>0.5);
		}

		return sol2;
	}
}
