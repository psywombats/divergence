/**
 *  EffectPassive.java
 *  Created on Apr 25, 2014 3:31:24 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectPassiveMDO;

/**
 * Passive stat-granting items.
 */
public class EffectPassive extends EffectBattleUnuseable {
	
	protected EffectPassiveMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectPassive(EffectPassiveMDO mdo, CombatItem item) {
		super(mdo, item);
		this.item = item;
		
		SagaStats stats = item.getStatset();
		stats.combine(new SagaStats(mdo.stats));
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isMapUsable() */
	@Override public boolean isMapUsable() { return false; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#onMapUse
	 * (net.wombatrpgs.saga.screen.TargetSelectable)
	 */
	@Override
	public void onMapUse(TargetSelectable caller) {
		MGlobal.reporter.err("Unusable ability");
	}

}
