package com.dblp.communities.utilities;

public class MinMax<E extends Comparable<E>> {

	private E min;
	private E max;
	
	public MinMax(E a, E b) {
		if (a.compareTo(b) < 0) {
			min = a;
			max = b;
		} else if (a.compareTo(b) > 0) {
			min = b;
			max = a;
		} else {
			min = a;
			max = b;
		}
	}
	
	public E min() {
		return min;
	}
	
	public E max() {
		return max;
	}
}
