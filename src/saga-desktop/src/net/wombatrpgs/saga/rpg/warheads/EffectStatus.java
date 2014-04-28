/**
 *  EffectStatus.java
 *  Created on Apr 28, 2014 7:09:32 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.warheads;

import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Intent;
import net.wombatrpgs.sagaschema.rpg.abil.data.warheads.EffectStatusMDO;

/**
 * Inflicts status conditions on enemies.
 */
public class EffectStatus extends EffectEnemyTarget {
	
	protected EffectStatusMDO mdo;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to construct from
	 * @param	item			The item to construct for
	 */
	public EffectStatus(EffectStatusMDO mdo, CombatItem item) {
		super(mdo, item);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#onAffect
	 * (net.wombatrpgs.saga.rpg.Battle, net.wombatrpgs.saga.rpg.Intent,
	 * net.wombatrpgs.saga.rpg.Chara, net.wombatrpgs.saga.rpg.Chara, int)
	 */
	@Override
	protected void onAffect(Battle battle, Intent intent, Chara user, Chara victim, int power) {
		String tab = SConstants.TAB;
		String victimname = victim.getName();
		String inflictString = mdo.status.getInflictString();
		victim.setStatus(mdo.status);
		battle.println(tab + victimname + inflictString + ".");
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#calcPower
	 * (net.wombatrpgs.saga.rpg.Battle, net.wombatrpgs.saga.rpg.Chara)
	 */
	@Override
	protected int calcPower(Battle battle, Chara user) {
		int power = mdo.hit;
		if (mdo.accStat != null) {
			power += user.get(mdo.accStat);
		}
		return power;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.warheads.EffectEnemyTarget#hits
	 * (net.wombatrpgs.saga.rpg.Battle, net.wombatrpgs.saga.rpg.Chara,
	 * net.wombatrpgs.saga.rpg.Chara, int, float)
	 */
	@Override
	protected boolean hits(Battle battle, Chara user, Chara target, int power, float roll) {
		String tab = SConstants.TAB;
		if (target.getStatus() != null) {
			if (target.getStatus().getPriority() > mdo.status.getPriority()) {
				battle.println(tab + "Nothing happens.");
				return false;
			}
		}
		int temp = power;
		if (mdo.evadeStat != null) {
			temp -= target.get(mdo.evadeStat);
		}
		float chance = (float) temp / 100f;
		boolean hit = roll < chance;
		if (hit) {
			return true;
		} else {
			battle.println(tab + "Nothing happens.");
			return false;
		}
	}

}
