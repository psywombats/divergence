/**
 *  EffectDebuff.java
 *  Created on Sep 13, 2014 12:50:15 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.battle.Intent;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectDebuffMDO;

/**
 * Somehow this didn't get made in the first round of RPG code, but it reduces
 * an enemy's stat.
 */
public class EffectDebuff extends EffectEnemyTarget {
	
	protected EffectDebuffMDO mdo;

	/**
	 * Creates a new debuff effect from data.
	 * @param	mdo				The data to create from
	 * @param item
	 */
	public EffectDebuff(EffectDebuffMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#onAffect
	 * (net.wombatrpgs.saga.rpg.battle.Battle, net.wombatrpgs.saga.rpg.battle.Intent,
	 * net.wombatrpgs.saga.rpg.chara.Chara, net.wombatrpgs.saga.rpg.chara.Chara, int)
	 */
	@Override
	protected void onAffect(Battle battle, Intent intent, Chara user, Chara victim, int power) {
		String victimname = victim.getName();
		String tab = SConstants.TAB;
		String statName = mdo.drainStat.getFullName();
		int debuffPower = power;
		if (mdo.defendStat != null) {
			debuffPower -= victim.get(mdo.defendStat);
		}
		int finalStat = victim.get(mdo.drainStat) - debuffPower;
		if (finalStat < 0) {
			debuffPower = victim.get(mdo.drainStat);
		}
		if (debuffPower > 0) {
			SagaStats mod = new SagaStats();
			mod.setStat(mdo.drainStat, debuffPower);
			battle.applyBoost(victim, mod);
			battle.println(tab + victimname + "'s " + statName + " is down by "
					+ debuffPower + ".");
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#calcPower
	 * (net.wombatrpgs.saga.rpg.battle.Battle, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected int calcPower(Battle battle, Chara user) {
		int power = mdo.power;
		if (mdo.attackStat != null) {
			power += user.get(mdo.attackStat) / 4;
		}
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#hits
	 * (net.wombatrpgs.saga.rpg.battle.Battle,net.wombatrpgs.saga.rpg.chara.Chara,
	 * net.wombatrpgs.saga.rpg.chara.Chara, int, float)
	 */
	@Override
	protected boolean hits(Battle battle, Chara user, Chara target, int power, float roll) {
		return true;
	}
	
}
