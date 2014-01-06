/**
 *  StepWait.java
 *  Created on Oct 18, 2013 5:04:16 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.travel;

import net.wombatrpgs.saga.rpg.CharacterEvent;

/**
 * Doesn't move. Ahahahaha.
 */
public class StepWait extends Step {

	/**
	 * Constructs a new wait (??) for a given character.
	 * @param	actor			The actor that will be performing this wait
	 */
	public StepWait(CharacterEvent actor) {
		super(actor);
	}

}
