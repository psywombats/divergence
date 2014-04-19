/**
 *  StatusType.java
 *  Created on Apr 2, 2014 1:24:11 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara.data;

import java.util.EnumSet;

import net.wombatrpgs.sagaschema.rpg.stats.Flag;

/**
 * Abnormal status conditions.
 */
public enum Status implements Resistable {
	
	BLIND		(Flag.RESIST_BLIND,		"BLIND"),
	CURSE		(Flag.RESIST_CURSE,		"CURSED"),
	SLEEP		(Flag.RESIST_SLEEP,		"ASLEEP"),
	PARALYZE	(Flag.RESIST_PARALYZE,	"PARALYZED"),
	CONFUSE		(Flag.RESIST_CONFUSE,	"CONFUSED"),
	STONE		(Flag.RESIST_STONE,		"STONE");
	
	private EnumSet<Flag> resistFlags;
	private String tag;
	
	/**
	 * Internal enum constructor.
	 * @param	resistFlags		All flags that could be used to resist damage
	 * @param	tag				The string associated with afflicted charas
	 */
	Status(Flag resistFlags, String tag) {
		this.resistFlags = EnumSet.of(resistFlags);
		this.tag = tag;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getResistFlag()
	 */
	@Override
	public EnumSet<Flag> getResistFlag() {
		return resistFlags;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getWeakFlag()
	 */
	@Override
	public EnumSet<Flag> getWeakFlag() {
		return EnumSet.noneOf(Flag.class);
	}
	
	/**
	 * Returns the string associated with afflicted charas. This is what BLND or
	 * STON would be in FFL, but a little... longer.
	 * @return					The tag for afflicted charas
	 */
	public String getTag() {
		return tag;
	}

}
