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
	
	BLIND		(Flag.RESIST_BLIND),
	CURSE		(Flag.RESIST_CURSE),
	SLEEP		(Flag.RESIST_SLEEP),
	PARALYZE	(Flag.RESIST_PARALYZE),
	CONFUSE		(Flag.RESIST_CONFUSE),
	STONE		(Flag.RESIST_STONE);
	
	private EnumSet<Flag> resistFlags;
	
	/**
	 * Internal enum constructor.
	 * @param	resistFlags		All flags that could be used to resist damage
	 */
	Status(EnumSet<Flag> resistFlags) {
		this.resistFlags = resistFlags;
	}
	
	/**
	 * Internal enum constructor. Assumes single flag resistance.
	 * @param	resistFlag		The flag that can be used to resist damage
	 */
	Status(Flag resistFlag) {
		this(EnumSet.of(resistFlag));
	}
	
	/**
	 * Internal enum constructor. Assumes no flags used to resist.
	 */
	Status() {
		this(EnumSet.noneOf(Flag.class));
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

}
