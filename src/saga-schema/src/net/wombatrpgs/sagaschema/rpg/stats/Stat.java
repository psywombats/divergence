/**
 *  Stat.java
 *  Created on Feb 28, 2014 6:10:13 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.stats;

import net.wombatrpgs.mgneschema.rpg.data.AdditiveStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStat;
import net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable;

/**
 * An enum of all numeric stats in Saga. The name is so brief because it's
 * going to be referenced all over the place.
 */
public enum Stat implements NumericStatLinkable {
	
	MHP					("max health",	"MHP:"),
	HP					("health",		"HP :"),
	STR					("strength",	"STR:"),
	DEF					("defense",		"DEF:"),
	AGI					("agility",		"AGI:"),
	MANA				("mana",		"MANA");
	
	private NumericStat stat;
	private String label, name;
	
	/**
	 * Internal enum constructor.
	 * @param	name			The full name for this stat
	 * @param	label			The display tag for this stat
	 */
	Stat(String name, String label) {
		this.name = name;
		this.label = label;
	}

	/**
	 * @see net.wombatrpgs.mgneschema.rpg.data.NumericStatLinkable#getStat()
	 */
	@Override
	public NumericStat getStat() {
		if (stat == null) {
			stat = new AdditiveStat(this.name());
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
	
	/**
	 * Returns the official manual-level quality name of this stat.
	 * @return					The name for this stat
	 */
	public String getFullName() {
		return name;
	}

}
