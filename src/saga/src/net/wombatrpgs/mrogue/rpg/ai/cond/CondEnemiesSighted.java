/**
 *  CondEnemiesSighted.java
 *  Created on Oct 16, 2013 3:29:27 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai.cond;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.ai.BTCondition;

/**
 * Checks to see if any hostiles are nearby.
 */
public class CondEnemiesSighted extends BTCondition {

	/**
	 * Constructs a new sighted conditional for a given actor.
	 * @param	actor			
	 */
	public CondEnemiesSighted(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTCondition#isMet()
	 */
	@Override
	protected boolean isMet() {
		return actor.getUnit().getVisibleEnemies().size() > 0;
	}

}
