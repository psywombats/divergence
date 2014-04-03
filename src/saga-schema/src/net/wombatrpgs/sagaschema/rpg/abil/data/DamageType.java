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
	
	PHYSICAL		(EnumSet.of(Flag.RESIST_DAMAGE),						EnumSet.noneOf(Flag.class)),
	WEAPON			(EnumSet.of(Flag.RESIST_DAMAGE, Flag.RESIST_WEAPON),	EnumSet.noneOf(Flag.class)),
	
	FIRE			(EnumSet.of(Flag.RESIST_DAMAGE, Flag.RESIST_FIRE),		EnumSet.of(Flag.WEAK_FIRE)),
	ICE				(EnumSet.of(Flag.RESIST_DAMAGE, Flag.RESIST_ICE),		EnumSet.of(Flag.WEAK_ICE)),
	EARTH			(EnumSet.of(Flag.RESIST_DAMAGE, Flag.RESIST_EARTH),		EnumSet.of(Flag.WEAK_EARTH)),
	THUNDER			(EnumSet.of(Flag.RESIST_DAMAGE, Flag.RESIST_THUNDER),	EnumSet.of(Flag.WEAK_THUNDER)),
	
	NONELEMENTAL	(EnumSet.noneOf(Flag.class),							EnumSet.noneOf(Flag.class));
	
	private EnumSet<Flag> resistFlags;
	private EnumSet<Flag> weakFlags;
	
	/**
	 * Internal enum constructor.
	 * @param	resistFlags		All flags that could be used to resist damage
	 * @param	weakFlags		All flags that indicate weakness
	 */
	DamageType(EnumSet<Flag> resistFlags, EnumSet<Flag> weakFlags) {
		this.resistFlags = resistFlags;
		this.weakFlags = weakFlags;
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
		return weakFlags;
	}
	
}
