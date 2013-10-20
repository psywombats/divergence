/**
 *  BTSelector.java
 *  Created on Oct 10, 2013 3:00:20 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Performs the duties of a canonical behavior tree selector. This is the upper
 * wrapper for most enemy AIs: ie, it keeps iteratoring through its children
 * until it finds one that works, and then does it. Therefore the first elements
 * inside it have a higher priority.
 */
public class BTSelector extends BTComposite {

	/**
	 * Creates a new selector for a specified actor without any child data. It
	 * needs to be populated before it should be used.
	 * @param	actor			The actor that will be performing
	 */
	public BTSelector(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#getStatusAndAct()
	 */
	@Override
	public BTState getStatusAndAct() {
		boolean succeeded = false;
		for (int i = children.size()-1; i >= 0; i -= 1) {
			BTNode child = children.get(i);
			switch (child.getStatusAndAct()) {
			case FAILURE:
				continue;
			case RUNNING:
				updateRunning(child);
				return BTState.RUNNING;
			case SUCCESS:
				succeeded = true;
				continue;
			}
		}
		return succeeded ? BTState.SUCCESS : BTState.FAILURE;
	}

}
