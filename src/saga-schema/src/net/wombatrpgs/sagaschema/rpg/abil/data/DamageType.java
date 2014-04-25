/**
 *  WarheadType.java
 *  Created on Feb 24, 2014 7:39:32 PM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data;

import java.util.EnumSet;

import net.wombatrpgs.sagaschema.rpg.chara.data.Resistable;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;

/**
 * All damage falls into one of these categories.
 */
public enum DamageType implements Resistable {
	
	PHYSICAL		(false,	EnumSet.of(Flag.RESIST_DAMAGE),							EnumSet.noneOf(Flag.class)),
	WEAPON			(false,	EnumSet.of(Flag.RESIST_WEAPON, Flag.RESIST_DAMAGE),		EnumSet.noneOf(Flag.class)),
	
	FIRE			(true,	EnumSet.of(Flag.RESIST_FIRE, Flag.RESIST_DAMAGE),		EnumSet.of(Flag.WEAK_FIRE)),
	ICE				(true,	EnumSet.of(Flag.RESIST_ICE, Flag.RESIST_DAMAGE),		EnumSet.of(Flag.WEAK_ICE)),
	EARTH			(true,	EnumSet.of(Flag.RESIST_EARTH, Flag.RESIST_DAMAGE),		EnumSet.of(Flag.WEAK_EARTH)),
	THUNDER			(true,	EnumSet.of(Flag.RESIST_THUNDER, Flag.RESIST_DAMAGE),	EnumSet.of(Flag.WEAK_THUNDER)),
	
	NONELEMENTAL	(false,	EnumSet.noneOf(Flag.class),								EnumSet.noneOf(Flag.class));
	
	private boolean fullResist;
	private EnumSet<Flag> resistFlags;
	private EnumSet<Flag> weakFlags;
	
	/**
	 * Internal enum constructor.
	 * @param	fullResist		True if the damage can be fully negated
	 * @param	resistFlags		All flags that could be used to resist damage
	 * @param	weakFlags		All flags that indicate weakness
	 */
	DamageType(boolean fullResist, EnumSet<Flag> resistFlags, EnumSet<Flag> weakFlags) {
		this.resistFlags = resistFlags;
		this.weakFlags = weakFlags;
		this.fullResist = fullResist;
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
		return weakFlags;
	}

	/**
	 * @see net.wombatrpgs.sagaschema.rpg.chara.data.Resistable#isNegateable()
	 */
	@Override
	public boolean isNegateable() {
		return fullResist;
	}
	
}
