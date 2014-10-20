package com.cruciform.utils;

public class WrappedIncrementor {
	private final int max;
	private final int min;
	private int index = 0;

	public interface Accepter {
		public boolean execute(int index, WrappedIncrementor incrementor);
	}
	
	public WrappedIncrementor(final int max) {
		this.max = max;
		this.min = 0;
	}
	
	public WrappedIncrementor(final int max, final int min, final int initialIndex) {
		this.max = max;
		this.min = min;
		this.index = initialIndex;
	}
	
	public void increment() {
		index++;
		if (index > max) {
			index = min;
		}
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getSize() {
		return max - min + 1;
	}
	
	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}
	
	public boolean isValidIndex(int indexToCheck) {
		return indexToCheck <= max && indexToCheck >= min;
	}

}
