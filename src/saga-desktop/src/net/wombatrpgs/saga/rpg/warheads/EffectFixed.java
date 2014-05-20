/**
 *  EffectFixed.java
 *  Created on Apr 25, 2014 11:42:04 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectFixedMDO;

/**
 * Guns, bows, whips, and grenades.
 */
public class EffectFixed extends EffectCombat {
	
	protected EffectFixedMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	item			The data to create for
	 */
	public EffectFixed(EffectFixedMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcPower
	 * (Battle, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected int calcPower(Battle battle, Chara user) {
		return mdo.base + MGlobal.rand.nextInt(mdo.range);
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#calcDamage
	 * (Battle, int, net.wombatrpgs.saga.rpg.chara.Chara)
	 */
	@Override
	protected int calcDamage(Battle battle, int power, Chara target) {
		if (mdo.defenseStat != null) {
			power -= target.get(mdo.defenseStat);
		}
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectCombat#combatHits
	 * (net.wombatrpgs.saga.rpg.Battle, net.wombatrpgs.saga.rpg.chara.Chara,
	 * net.wombatrpgs.saga.rpg.chara.Chara, float)
	 */
	@Override
	protected boolean combatHits(Battle battle, Chara user, Chara target, float roll) {
		int temp = 100 - (user.get(mdo.accStat) + mdo.accuracy - shielding(battle, target));
		if (mdo.dodgeStat != null) {
			temp += target.get(mdo.dodgeStat);
		}
		float chance = (float) temp / 100f;
		return roll > chance;
	}

}
