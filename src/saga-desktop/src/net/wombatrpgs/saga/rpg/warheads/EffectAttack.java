/**
 *  EffectAttack.java
 *  Created on Apr 23, 2014 3:11:29 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.data.MissType;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectAttackMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

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
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcPower
	 * (net.wombatrpgs.saga.rpg.Chara)
	 */
	@Override
	protected int calcPower(Chara user) {
		int temp = mdo.power;
		int power = 0;
		if (mdo.attackStat != null) {
			temp *= Math.ceil((float) user.get(mdo.attackStat) / 4f);
			power = user.get(mdo.attackStat);
		}
		power += (temp + MGlobal.rand.nextInt(temp));
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcDamage
	 * (int, net.wombatrpgs.saga.rpg.Chara)
	 */
	@Override
	protected int calcDamage(int power, Chara target) {
		if (mdo.defendStat != null && !weak(target)) {
			power -= target.get(mdo.defendStat);
		}
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#hits
	 * (Battle, net.wombatrpgs.saga.rpg.Chara, net.wombatrpgs.saga.rpg.Chara, float)
	 */
	@Override
	protected boolean hits(Battle battle, Chara user, Chara target, float roll) {
		if (mdo.miss == MissType.ALWAYS_HITS) return true;
		Stat agi = Stat.AGI;
		int temp = 100 - (target.get(agi) + shielding(battle, target) - user.get(agi));
		float chance = (float) temp / 100f;
		return roll < chance;
	}

}
