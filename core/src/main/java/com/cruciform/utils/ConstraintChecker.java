package com.cruciform.utils;

import java.util.List;

public class ConstraintChecker<T> {
	public static class Constraint<T> {
		private final T a;
		private final T b;
		
		public Constraint(T a, T b) {
			this.a = a;
			this.b = b;
		}
		
		public boolean shouldValidate(T otherA, T otherB) {
			return a == otherA && b == otherB;
		}
		
		public boolean validate(T otherA, T otherB, int indexA, int indexB) {
			return indexA < indexB;
		}
	}
	
	private final List<T> elements;
	private final List<Constraint<T>> constraints;

	public ConstraintChecker(List<T> elements, List<Constraint<T>> constraints) {
		this.elements = elements;
		this.constraints = constraints;
	}
	
	public boolean validate() {
		for (Constraint<T> c : constraints) {
			for (int i = 0; i < elements.size(); i++) {
				T a = elements.get(i);
				for (int j = 0; j < elements.size(); j++) {
					T b = elements.get(j);
					if (c.shouldValidate(a, b)) {
						if (!c.validate(a, b, i, j)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
}
