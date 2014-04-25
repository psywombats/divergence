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
	
	BLIND		(Flag.RESIST_BLIND,		"BLIND",		false),
	CURSE		(Flag.RESIST_CURSE,		"CURSED",		false),
	SLEEP		(Flag.RESIST_SLEEP,		"ASLEEP",		true),
	PARALYZE	(Flag.RESIST_PARALYZE,	"PARALYZED",	true),
	CONFUSE		(Flag.RESIST_CONFUSE,	"CONFUSED",		false),
	STONE		(Flag.RESIST_STONE,		"STONE",		true);
	
	private EnumSet<Flag> resistFlags;
	private String tag;
	private boolean preventsAction;
	
	/**
	 * Internal enum constructor.
	 * @param	resistFlags		All flags that could be used to resist damage
	 * @param	tag				The string associated with afflicted charas
	 * @param	preventsAction	True if this status prevents monster from attack
	 */
	Status(Flag resistFlags, String tag, boolean preventsAction) {
		this.resistFlags = EnumSet.of(resistFlags);
		this.tag = tag;
		this.preventsAction = preventsAction;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getResistFlags()
	 */
	@Override
	public EnumSet<Flag> getResistFlags() {
		return resistFlags;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#getWeakFlags()
	 */
	@Override
	public EnumSet<Flag> getWeakFlags() {
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
	
	/**
	 * Returns whether this status prevents afflicted from acting in battle.
	 * @return					True if prevents action, false otherwise
	 */
	public boolean preventsAction() {
		return preventsAction;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#isNegateable()
	 */
	@Override
	public boolean isNegateable() {
		return true;
	}

}
