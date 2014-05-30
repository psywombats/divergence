/**
 *  MutantLevelOption.java
 *  Created on May 24, 2014 8:57:52 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.mutant;

import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * A thing that a mutant can elect to do when they level up.
 */
public abstract class Mutation {
	
	protected Chara chara;
	
	/**
	 * Creates a new option for a mutant.
	 * @param	chara			The mutant to create for
	 */
	public Mutation(Chara chara) {
		this.chara = chara;
	}
	
	/**
	 * Returns the description displayed in the message box to the player if
	 * they want to select this option.
	 * @return					The description of the level option
	 */
	public abstract String getDesc();
	
	/**
	 * Apply this option to a character after the player selects it.
	 */
	public abstract void apply();
	
	/**
	 * Returns the stat (if any) that this option will raise. Used to compare
	 * between level options so that two of the same aren't presented.
	 * @return					The stat controlling this level, or null
	 */
	public abstract Stat getStat();

}
