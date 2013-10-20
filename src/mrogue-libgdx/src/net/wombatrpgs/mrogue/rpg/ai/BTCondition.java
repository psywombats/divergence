/**
 *  BTCondition.java
 *  Created on Oct 10, 2013 2:43:51 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * A simple conditional for a behavior tree. Can not return an action and if you
 * ask it to, it will kill itself. Instead, check its status.
 */
public abstract class BTCondition extends BTNode {

	/**
	 * Creates a new condition for a specific actor.
	 * @param	actor			The actor this node will be created for
	 */
	public BTCondition(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#reset()
	 */
	@Override
	public void reset() {
		// if you need this, override it
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#getStatusAndAct()
	 */
	@Override
	public BTState getStatusAndAct() {
		if (isMet()) {
			return BTState.SUCCESS;
		} else {
			return BTState.FAILURE;
		}
	}

	/**
	 * Checks if the titular condition is met.
	 * @return					True if the condition holds, false otherwise
	 */
	protected abstract boolean isMet();

}
