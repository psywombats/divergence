/**
 *  Chara.java
 *  Created on Apr 2, 2014 10:53:07 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.sagaschema.rpg.chara.CharacterMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.chara.data.Resistable;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * An in-game character. Not called Character so as not to conflict with the one
 * in java.lang.
 */
public class Chara {
	
	protected CharacterMDO mdo;
	
	protected SagaStats stats;
	
	/**
	 * Creates a new character from data template.
	 * @param	mdo				The data to create from
	 */
	public Chara(CharacterMDO mdo) {
		this.mdo = mdo;
		this.stats = new SagaStats(mdo.stats);
	}
	
	/** @return The current value of the requested stat */
	public float get(Stat stat) { return stats.stat(stat); }
	
	/** @return The current value of the request flag */
	public boolean is(Flag flag) { return stats.flag(flag); }
	
	/**
	 * Checks if this character resists a certain damage or status type.
	 * @param	type			The damage or status to check
	 * @return					True if this character resists that type
	 */
	public boolean resists(Resistable type) {
		for (Flag flag : type.getResistFlag()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Checks if this character is weak to a certain damage or status type.
	 * @param	type			The damage or status to check
	 * @return					True if this character resists that type
	 */
	public boolean isVulnerableTo(Resistable type) {
		for (Flag flag : type.getWeakFlag()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Modifies this character's stats in accordance with the given set.
	 * @param	mod				The values to modify by
	 * @param	decombine		True to decombine rather than apply stats
	 */
	protected void applyStatset(SagaStats mod, boolean decombine) {
		float oldMHP = mod.stat(Stat.MHP);
		if (mdo.race != Race.ROBOT) {
			mod.setStat(Stat.MHP,  0);
		}
		if (decombine) {
			stats.decombine(mod);
		} else {
			stats.combine(mod);
		}
		mod.setStat(Stat.MHP, oldMHP);
	}

}
