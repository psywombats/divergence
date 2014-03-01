/**
 *  Flag.java
 *  Created on Feb 28, 2014 6:04:18 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.data;

import net.wombatrpgs.mgneschema.rpg.data.FlagStatLinkable;
import net.wombatrpgs.mgneschema.rpg.data.FlagStat;

/**
 * Enum of all flags in Tactics.
 */
public enum Flag implements FlagStatLinkable {
	
	;			// <-- by far the weirdest thing so far

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
