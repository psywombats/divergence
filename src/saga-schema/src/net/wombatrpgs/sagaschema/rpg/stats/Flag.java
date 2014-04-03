/**
 *  Flag.java
 *  Created on Feb 28, 2014 6:04:18 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.stats;

import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.FlagStat;

/**
 * Enum of all flags in Saga.
 */
public enum Flag implements FlagStatLinkable {
	
	HUMAN,
	AQUATIC,
	DRACONIC,
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
