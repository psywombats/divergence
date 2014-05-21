/**
 *  EffectPassive.java
 *  Created on Apr 25, 2014 3:31:24 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectPassiveMDO;

/**
 * Passive stat-granting items.
 */
public class EffectPassive extends AbilEffect {
	
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

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return false; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.Intent, net.wombatrpgs.saga.rpg.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(Intent intent, IntentListener listener) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void resolve(Intent intent) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#assignRandomTargets
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void assignRandomTargets(Intent intent) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#onMapUse
	 * (net.wombatrpgs.saga.screen.TargetSelectable)
	 */
	@Override
	public void onMapUse(TargetSelectable caller) {
		MGlobal.reporter.err("Unusable ability");
	}

}
