/**
 *  EffectNothing.java
 *  Created on Apr 25, 2014 2:18:29 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.battle.Intent;
import net.wombatrpgs.saga.rpg.battle.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectNothingMDO;

/**
 * Items unusable in battle and in the field.
 */
public class EffectNothing extends AbilEffect {

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectNothing(EffectNothingMDO mdo, CombatItem item) {
		super(mdo, item);
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isMapUsable() */
	@Override public boolean isMapUsable() { return false; }

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return false; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.battle.Intent, net.wombatrpgs.saga.rpg.battle.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(Intent intent, IntentListener listener) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
	 */
	@Override
	public void resolve(Intent intent) {
		MGlobal.reporter.err("Unusable ability");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#assignRandomTargets
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
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
