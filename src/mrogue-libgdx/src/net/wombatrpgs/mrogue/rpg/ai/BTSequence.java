/**
 *  BTSequence.java
 *  Created on Oct 10, 2013 3:28:39 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * A sequence fails if any child fails. Internal actions should prooobably not
 * be repeatable or this'll get stuck.
 */
public class BTSequence extends BTComposite {

	/**
	 * Constructs a sequence for a specific actor with no children.
	 * @param	actor			The actor that will be doing the action
	 */
	public BTSequence(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#getStatusAndAct()
	 */
	@Override
	public BTState getStatusAndAct() {
		for (int i = children.size()-1; i >= 0; i -= 1) {
			BTNode child = children.get(i);
			switch (child.getStatusAndAct()) {
			case FAILURE:
				return BTState.FAILURE;
			case RUNNING:
				updateRunning(child);
				return BTState.RUNNING;
			case SUCCESS:
				continue;
			}
		}
		return BTState.SUCCESS;
	}

}
