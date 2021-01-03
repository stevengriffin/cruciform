package com.cruciform.utils;

/**
 * An int wrapper for setting component priorities in a system.
 * Denotes what layer of the screen the entity will be drawn in. Higher is better.
 */
public final class Priority implements Comparable<Priority> {
	private final int value;
	
	public Priority(int value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		Priority othervalue = (Priority) other;
		if (othervalue == null) {
			return false;
		}
		return this.value == othervalue.value;
	}

	@Override
	public String toString() {
		return "Priority: [" + value + "]";
	}

	@Override
	public int compareTo(Priority other) {
		if (other.value == this.value) {
			return 0;
		} else if (other.value > this.value) {
			return -1;
		}
		return 1;
	}
}
