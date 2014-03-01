/**
 *  Stat.java
 *  Created on Feb 28, 2014 6:10:13 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.rpg.data;

import net.wombatrpgs.mgneschema.rpg.data.AdditiveStat;
import net.wombatrpgs.mgneschema.rpg.data.MultiplicativeStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;

/**
 * An enum of all numeric stats in Tactics. The name is so brief because it's
 * going to be referenced all over the place.
 */
public enum Stat implements NumericStatLinkable {
	
	MHP					(false),
	HP					(false),
	MOVE_RANGE			(false);
	
	private boolean multiMode;
	private NumericStat stat;
	
	/**
	 * Internal enum constructor.
	 * @param	multiMode		True if stat is multiplicative, false additive
	 */
	Stat(boolean multiMode) {
		this.multiMode = multiMode;
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable#getStat()
	 */
	@Override
	public NumericStat getStat() {
		if (stat == null) {
			if (multiMode) {
				stat = new MultiplicativeStat(this.name());
			} else {
				stat = new AdditiveStat(this.name());
			}
		}
		return stat;
	}

}
