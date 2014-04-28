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
	
	BLIND		(Flag.RESIST_BLIND,		"BLIND",		false,	5,	" is blind",		" regains sight"),
	CURSE		(Flag.RESIST_CURSE,		"CURSED",		false,	10,	" is cursed",		" is rid of the curse"),
	SLEEP		(Flag.RESIST_SLEEP,		"ASLEEP",		true,	15,	" is sleeping",		" wakes up"),
	PARALYZE	(Flag.RESIST_PARALYZE,	"PARALYZED",	true,	20,	" is paralyzed",	" is rid of paralysis"),
	CONFUSE		(Flag.RESIST_CONFUSE,	"CONFUSED",		false,	17,	" is confused",		"'s mind is back to normal"),
	STONE		(Flag.RESIST_STONE,		"STONE",		true,	50,	" is stone",		" regains life");
	
	private EnumSet<Flag> resistFlags;
	private String tag, healString, inflictString;
	private boolean preventsAction;
	private int priority;
	
	/**
	 * Internal enum constructor.
	 * @param	resistFlags		All flags that could be used to resist damage
	 * @param	tag				The string associated with afflicted charas
	 * @param	preventsAction	True if this status prevents monster from attack
	 * @param	priority		The priority to inflict relative to other status
	 * @param	inflictString	The affliction message, ie " is put to sleep"
	 * @param	healString		The healing message, ie " wakes up"
	 */
	Status(Flag resistFlags, String tag, boolean preventsAction, int priority,
			String inflictString, String healString) {
		this.resistFlags = EnumSet.of(resistFlags);
		this.tag = tag;
		this.preventsAction = preventsAction;
		this.healString = healString;
		this.inflictString = inflictString;
		this.priority = priority;
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
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#isNegateable()
	 */
	@Override
	public boolean isNegateable() {
		return true;
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
	 * Returns the healing message for this status. Should be prefixed with the
	 * character's name (but not space) and suffixed with a period.
	 * @return					The heal string for this condition
	 */
	public String getHealString() {
		return healString;
	}
	
	/**
	 * Returns the affliction message for this status. Should be prefixed with
	 * the character's name (but not space) and suffixed with a period.
	 * @return					The affliction string for this condition
	 */
	public String getInflictString() {
		return inflictString;
	}
	
	/**
	 * Returns the priority of this status relative to other statuses. A status
	 * with higher priorty than another will be inflicted over top of it.
	 * @return					The priority of this status (relative)
	 */
	public int getPriority() {
		return priority;
	}

}
