package com.cruciform.utils;

import com.badlogic.gdx.utils.TimeUtils;

public class Score {
	private static long score;
	private static int multiplier;
	private static int damageForNextMultiplier;
	private static final int DAMAGE_PER_MULT_INCREASE = 100;
	private static final long MULTIPLIER_DECAY_TIME = 2000;
	private static long lastDamagerEventTime;
	private static int creditsUsed;
	
	public static void init() {
		score = 0;
		multiplier = 1;
		damageForNextMultiplier = 0;
		lastDamagerEventTime = TimeUtils.millis();
		creditsUsed = 1;
	}
	
	public static void incrementFromDamagerEvent(final float damage, final float currentHealth) {
		final int effectiveDamage = (int) (currentHealth > damage ? damage : currentHealth);
		score += effectiveDamage*multiplier;
		damageForNextMultiplier += effectiveDamage;
		while (damageForNextMultiplier >= DAMAGE_PER_MULT_INCREASE) {
			damageForNextMultiplier -= DAMAGE_PER_MULT_INCREASE;
			multiplier++;
		}
		lastDamagerEventTime = TimeUtils.millis();
	}
	
	public static void update() {
		if (TimeUtils.timeSinceMillis(lastDamagerEventTime) > MULTIPLIER_DECAY_TIME) {
			lastDamagerEventTime = TimeUtils.millis();
			multiplier = multiplier > 1 ? multiplier - 1 : 1;
		}
	}
	
	public static long getScore() {
		return score;
	}
	
	public static int getMultiplier() {
		return multiplier;
	}

	public static void useCredit() {
		creditsUsed++;
	}
	
	public static int getCreditsUsed() {
		return creditsUsed;
	}
	
}
