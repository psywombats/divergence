/**
 *  EffectAttack.java
 *  Created on Apr 23, 2014 3:11:29 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectAttackMDO;

/**
 * Inflcict damage or status on the enemy.
 */
public class EffectAttack extends AbilEffect {
	
	protected EffectAttackMDO mdo;

	/**
	 * Creates a new effect.
	 * @param	mdo				The data to create effect from
	 * @param	item			The item to create effect for
	 */
	public EffectAttack(EffectAttackMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isMapUsable() */
	@Override public boolean isMapUsable() { return false; }

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return true; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.Intent, net.wombatrpgs.saga.rpg.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(final Intent intent, final IntentListener listener) {
		switch (mdo.projector) {
		case SINGLE_ENEMY:
			Chara selected = null;
			if (intent.getTargets().size() > 0) {
				selected = intent.getTargets().get(0);
			}
			intent.getBattle().selectSingleEnemy(selected, intent.genDefaultListener(listener));
			break;
		case GROUP_ENEMY:
			int group = intent.inferSelectedGroup();
			intent.getBattle().selectEnemyGroup(group, intent.genDefaultListener(listener));
			break;
		case ALL_ENEMY:
			intent.clearTargets();
			intent.addTargets(intent.getBattle().getEnemy().getAll());
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		Party player = intent.getBattle().getPlayer();
		switch (mdo.projector) {
		case SINGLE_ENEMY: case GROUP_ENEMY:
			Chara target = player.getFront(MGlobal.rand.nextInt(player.size()));
			intent.addTargets(target);
			break;
		case ALL_ENEMY:
			intent.addTargets(player.getAll());
			break;
		}
	}

}
