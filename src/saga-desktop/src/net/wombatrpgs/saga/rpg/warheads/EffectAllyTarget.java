/**
 *  EffectAllyTarget.java
 *  Created on Apr 26, 2014 6:38:35 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.chara.Party;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectAllyTargetMDO;

/**
 * Any friendly ability that targets allies.
 */
public abstract class EffectAllyTarget extends AbilEffect {
	
	protected EffectAllyTargetMDO mdo;
	
	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectAllyTarget(EffectAllyTargetMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/** @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#isBattleUsable() */
	@Override public boolean isBattleUsable() { return true; }

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyIntent
	 * (net.wombatrpgs.saga.rpg.Intent, net.wombatrpgs.saga.rpg.Intent.IntentListener)
	 */
	@Override
	public void modifyIntent(Intent intent, IntentListener listener) {
		switch (mdo.projector) {
		case ALLY_PARTY: case PLAYER_PARTY_ENEMY_GROUP:
			intent.clearTargets();
			intent.addTargets(intent.getBattle().getPlayer().getAll());
			listener.onIntent(intent);
			break;
		case SINGLE_ALLY:
			int index = intent.inferAlly();
			intent.clearTargets();
			intent.getBattle().selectAlly(index, intent.genDefaultListener(listener));
			break;
		case USER:
			intent.clearTargets();
			intent.addTargets(intent.getActor());
			listener.onIntent(intent);
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#modifyEnemyIntent
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void modifyEnemyIntent(Intent intent) {
		Party enemy = intent.getBattle().getEnemy();
		switch (mdo.projector) {
		case ALLY_PARTY:
			intent.addTargets(enemy.getAll());
			break;
		case PLAYER_PARTY_ENEMY_GROUP: case SINGLE_ALLY:
			List<Integer> indexes = new ArrayList<Integer>();
			for (int i = 0; i < enemy.groupCount(); i += 1) {
				if (intent.getBattle().isEnemyAlive(i)) {
					indexes.add(i);
				}
			}
			int index = indexes.get(MGlobal.rand.nextInt(indexes.size()));
			intent.addTargets(enemy.getGroup(index));
			break;
		case USER:
			intent.addTargets(intent.getActor());
			break;
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#assignRandomTargets
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void assignRandomTargets(Intent intent) {
		switch (mdo.projector) {
		case ALLY_PARTY:
			assignRandomParty(intent);
			break;
		case PLAYER_PARTY_ENEMY_GROUP:
			assignRandomGroup(intent);
			break;
		case SINGLE_ALLY:
			assignRandomTarget(intent);
			break;
		case USER:
			intent.addTargets(intent.getActor());
			break;
		}
	}

}
