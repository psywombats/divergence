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
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.saga.screen.TargetSelectable;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectBoostMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Boost your stat for the duration of the battle.
 */
public class EffectBoost extends EffectAllyTarget {
	
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
	 * @see net.wombatrpgs.saga.rpg.warheads.AbilEffect#onMapUse
	 * (net.wombatrpgs.saga.screen.TargetSelectable)
	 */
	@Override
	public void onMapUse(TargetSelectable caller) {
		MGlobal.reporter.err("Unusable ability");
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
