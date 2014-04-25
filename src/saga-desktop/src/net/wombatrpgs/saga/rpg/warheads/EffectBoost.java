/**
 *  EffectBoost.java
 *  Created on Apr 25, 2014 3:07:44 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.SagaStats;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectBoostMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Boost your stat for the duration of the battle.
 */
public class EffectBoost extends AbilEffect {
	
	protected EffectBoostMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The item to create for
	 */
	public EffectBoost(EffectBoostMDO mdo, CombatItem item) {
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
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#resolve
	 * (net.wombatrpgs.saga.rpg.Intent)
	 */
	@Override
	public void resolve(Intent intent) {
		Battle battle = intent.getBattle();
		Chara user = intent.getActor();
		String username = user.getName();
		Stat stat = mdo.stat;
		String statname = stat.getFullName();
		String itemname = intent.getItem().getName();
		String tab = SConstants.TAB;
		List<Chara> targets = new ArrayList<Chara>();
		switch (mdo.projector) {
		case ALLY_PARTY: case PLAYER_PARTY_ENEMY_GROUP:
			targets.addAll(intent.getTargets());
			break;
		case SINGLE_ALLY: case USER:
			targets.add(intent.getTargets().get(0));
			break;
		}
		
		int power = mdo.power;
		if (mdo.powerStat != null) {
			power += user.get(mdo.powerStat);
		}
		battle.println(username + " uses " + itemname + ".");
		for (Chara victim : intent.getTargets()) {
			String victimname = victim.getName();
			int boost = power;
			boolean limited = false;
			if (mdo.cap != 0 && victim.get(stat) + boost > mdo.cap) {
				boost = mdo.cap - victim.get(stat);
				limited = true;
			}
			SagaStats mod = new SagaStats();
			mod.setStat(stat, boost);
			battle.applyBoost(victim, mod);
			if (boost > 0) {
				if (limited) {
					battle.println(tab + victimname + "'s " + statname + 
							" is up to " + mdo.cap + ".");
				} else {
					battle.println(tab + victimname + "'s " + statname + 
							" is up by " + boost + ".");
				}
			} else {
				battle.println(tab + "Nothing happens.");
			}
		}
	}

}
