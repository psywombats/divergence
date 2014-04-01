/**
 *  Flag.java
 *  Created on Feb 28, 2014 6:04:18 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.data;

import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.FlagStat;

/**
 * Enum of all flags in Saga.
 */
public enum Flag implements FlagStatLinkable {
	
	RESIST_WEAPON,
	RESIST_DAMAGE,
	RESIST_CONFUSE,
	RESIST_STATUS,
	RESIST_FIRE,
	RESIST_COLD,
	RESIST_EARTH,
	RESIST_THUNDER,
	RESIST_ELEMENTAL,
	WEAK_FIRE,
	WEAK_COLD,
	WEAK_EARTH,
	WEAK_THUNDER,
	UNDEAD;

	private FlagStat flag;
	
	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable#getFlag()
	 */
	@Override
	public FlagStat getFlag() {
		if (flag == null) {
			flag = new FlagStat(this.name());
		}
		return flag;
	}

}
