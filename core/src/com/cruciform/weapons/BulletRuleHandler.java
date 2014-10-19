package com.cruciform.weapons;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.cruciform.utils.WrappedIncrementor;

public class BulletRuleHandler {
	public float spanAngle = 360.0f;
	public float originAngle = 0.0f;
	public int spokes = 6;
	private final WrappedIncrementor pattern;
	private Array<BulletConfig> bullets = new Array<BulletConfig>();
	private IntMap<Array<BulletRule>> map = new IntMap<>();
	
	public BulletRuleHandler(WrappedIncrementor pattern) {
		this.pattern = pattern;
	}
	
	public Array<BulletConfig> fire() {
		pattern.increment();
		bullets.clear();
		Array<BulletRule> rules = map.get(pattern.getIndex());
		for (float i = originAngle; i < originAngle + spanAngle; i += (spanAngle - originAngle)/spokes) {
			BulletConfig config = new BulletConfig();
			config.angle = i;
			config.patternIndex = pattern.getIndex();
			if (rules != null) {
				for (int j = 0; j < rules.size; j++) {
					rules.get(j).mutateBullet(config);
				}
			}
			bullets.add(config);
		}
		return bullets;
	}
	
	public BulletRuleHandler addRule(BulletRule rule, int index) {
		Array<BulletRule> rules = map.get(index, new Array<BulletRule>());
		rules.add(rule);
		map.put(index, rules);
		return this;
	}
	
	public BulletRuleHandler addRule(BulletRule rule) {
		for (int index = pattern.getMin(); index < pattern.getMax(); index++) {
			Array<BulletRule> rules = map.get(index, new Array<BulletRule>());
			rules.add(rule);
			map.put(index, rules);
		}
		return this;
	}
}
