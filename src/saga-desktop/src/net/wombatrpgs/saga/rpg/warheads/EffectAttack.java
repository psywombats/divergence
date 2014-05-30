/**
 *  EffectAttack.java
 *  Created on Apr 23, 2014 3:11:29 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.mutant.MutantEvent;
import net.wombatrpgs.sagaschema.rpg.abil.data.MissType;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;
import net.wombatrpgs.sagaschema.rpg.warheads.EffectAttackMDO;

/**
 * Inflcict damage or status on the enemy.
 */
public class EffectAttack extends EffectCombat {
	
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

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#onResolveComplete
	 * (net.wombatrpgs.saga.rpg.battle.Battle, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected void onResolveComplete(Battle battle, Chara user) {
		super.onResolveComplete(battle, user);
		switch (mdo.attackStat) {
		case AGI:
			user.recordEvent(MutantEvent.USED_AGI);
			break;
		case MANA:
			user.recordEvent(MutantEvent.USED_MANA);
			break;
		case STR:
			user.recordEvent(MutantEvent.USED_STR);
			break;
		default:
			// doesn't matter
		}
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcPower
	 * (Battle, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected int calcPower(Battle battle, Chara user) {
		return statDamage(user, mdo.attackStat, mdo.power);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcDamage
	 * (Battle, int, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected int calcDamage(Battle battle, int power, Chara target) {
		if (mdo.defendStat != null && !weak(target)) {
			power -= target.get(mdo.defendStat);
		}
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#combatHits
	 * (net.wombatrpgs.saga.rpg.battle.Battle, net.wombatrpgs.saga.rpg.chara.Chara,
	 * net.wombatrpgs.saga.rpg.chara.Chara, float)
	 */
	@Override
	protected boolean combatHits(Battle battle, Chara user, Chara target, float roll) {
		if (mdo.miss == MissType.ALWAYS_HITS) return true;
		Stat agi = Stat.AGI;
		int temp = 100 - (target.get(agi) + shielding(battle, target) - user.get(agi));
		float chance = (float) temp / 100f;
		return roll < chance;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#isPhysical()
	 */
	@Override
	protected boolean isPhysical() {
		return mdo.defendStat == Stat.DEF;
	}

}
