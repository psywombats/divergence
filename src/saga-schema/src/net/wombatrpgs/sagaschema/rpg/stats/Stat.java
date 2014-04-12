/**
 *  Stat.java
 *  Created on Feb 28, 2014 6:10:13 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.stats;

import net.wombatrpgs.mgneschema.rpg.data.AdditiveStat;
import net.wombatrpgs.mgneschema.rpg.data.MultiplicativeStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;

/**
 * An enum of all numeric stats in Saga. The name is so brief because it's
 * going to be referenced all over the place.
 */
public enum Stat implements NumericStatLinkable {
	
	MHP					(false, "MHP:"),
	HP					(false, "HP :"),
	STR					(false, "STR:"),
	DEF					(false, "DEF:"),
	AGI					(false, "AGI:"),
	MANA				(false, "MANA");
	
	private boolean multiMode;
	private NumericStat stat;
	private String label;
	
	/**
	 * Internal enum constructor.
	 * @param	multiMode		True if stat is multiplicative, false additive
	 * @param	label			The display tag for this stat
	 */
	Stat(boolean multiMode, String label) {
		this.multiMode = multiMode;
		this.label = label;
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
	
	/**
	 * Returns the label for this stat (usually all caps).
	 * @return					The label for this stat
	 */
	public String getLabel() {
		return label;
	}

}
