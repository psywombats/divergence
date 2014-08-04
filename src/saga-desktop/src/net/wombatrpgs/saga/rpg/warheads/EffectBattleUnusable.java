/**
 *  EffectBattleUseable.java
 *  Created on May 30, 2014 12:14:25 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.battle.Intent;
import net.wombatrpgs.saga.rpg.battle.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.data.AbilEffectMDO;

/**
 * Any effect that cannot be used in battle.
 */
public abstract class EffectBattleUnusable extends AbilEffect {

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create the item from
	 * @param	item			The item to create the item for
	 */
	public EffectBattleUnusable(AbilEffectMDO mdo, CombatItem item) {
		super(mdo, item);
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return false; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.battle.Intent,
	 * net.wombatrpgs.saga.rpg.battle.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(Intent intent, IntentListener listener) {
		MGlobal.reporter.err("Unusable ability: " + mdo);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		MGlobal.reporter.err("Unusable ability: " + mdo);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#assignRandomTargets
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
	 */
	@Override
	public void assignRandomTargets(Intent intent) {
		MGlobal.reporter.err("Unusable ability: " + mdo);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.battle.Intent)
	 */
	@Override
	public void resolve(Intent intent) {
		MGlobal.reporter.err("Unusable ability: " + mdo);
	}

}
