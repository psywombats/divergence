/**
 *  RandomSelector.java
 *  Created on Oct 27, 2013 8:29:53 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import java.util.Collections;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Randomly selects a child to act.
 */
public class RandomSelector extends BTSelector {

	/**
	 * Creates a new random selector.
	 * @param	actor			The actor that will be performing
	 */
	public RandomSelector(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTSelector#getStatusAndAct()
	 */
	@Override
	public BTState getStatusAndAct() {
		Collections.shuffle(children);
		return super.getStatusAndAct();
	}

}
