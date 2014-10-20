package com.cruciform.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public abstract class AbstractRuleHandler<T> {
	protected final WrappedIncrementor pattern;
	protected IntMap<Array<T>> map = new IntMap<>();
	
	public AbstractRuleHandler(final WrappedIncrementor pattern) {
		this.pattern = pattern;
	}

	protected void incrementPattern() {
		pattern.increment();
	}
	
	/**
	 * Adds a rule for all indices of the pattern for which the accepter returns true.
	 */
	public AbstractRuleHandler<T> addRule(T rule, WrappedIncrementor.Accepter accepter) {
		for (int index = pattern.getMin(); index <= pattern.getMax(); index++) {
			if (accepter.execute(index, pattern)) {
				addRule(rule, index);
			}
		}
		return this;
	}
	
	/**
	 * Adds a rule for one predefined index of the pattern.
	 */
	public AbstractRuleHandler<T> addRule(T rule, int index) {
		Array<T> rules = map.get(index, new Array<T>());
		rules.add(rule);
		map.put(index, rules);
		return this;
	}

	/**
	 * Adds a rule for all indices of the pattern.
	 */
	public AbstractRuleHandler<T> addRule(T rule) {
		for (int index = pattern.getMin(); index <= pattern.getMax(); index++) {
			addRule(rule, index);
		}
		return this;
	}
}
