package com.cruciform.weapons;

import com.badlogic.gdx.utils.Array;
import com.cruciform.utils.AbstractRuleHandler;
import com.cruciform.utils.CoolDownMetro;
import com.cruciform.utils.CoolDownMutator;
import com.cruciform.utils.WrappedIncrementor;

public class CoolDownRuleHandler extends AbstractRuleHandler<CoolDownMutator> {
	
	private CoolDownMetro defaultCoolDown = new CoolDownMetro(1.0f);
	
	public CoolDownRuleHandler(final WrappedIncrementor pattern) {
		super(pattern);
	}

	public void setDefaultDoolDown(CoolDownMetro coolDown) {
		this.defaultCoolDown = coolDown;
	}
	
	public CoolDownMetro updateCoolDown(CoolDownMetro currentCoolDown) {
		final Array<CoolDownMutator> rules = map.get(pattern.getIndex());
		CoolDownMetro newCoolDown = currentCoolDown;
		if (rules != null) {
			for (int j = 0; j < rules.size; j++) {
				newCoolDown = rules.get(j).mutate(newCoolDown, pattern.getIndex());
			}
			return newCoolDown;
		} else {
			return defaultCoolDown;
		}
	}
}
