/**
 *  BTAction.java
 *  Created on Oct 10, 2013 2:54:47 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogue.rpg.act.Action;

/**
 * A wrapper for a normal game action to fit into the behavior tree spec.
 */
public class BTAction extends BTNode {
	
	protected Action act;
	protected boolean repeatable;
	protected boolean done;

	/**
	 * Creates a node for a given actor and act.
	 * @param	actor			The actor that will be performing the action
	 * @param	act				The action that will be performed
	 */
	public BTAction(CharacterEvent actor, Action act, boolean repeatable) {
		super(actor);
		this.act = act;
		this.repeatable = repeatable;
		reset();
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#reset()
	 */
	@Override
	public void reset() {
		done = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#getStatusAndAct()
	 */
	@Override
	public BTState getStatusAndAct() {
		if (done && !repeatable) {
			return BTState.SUCCESS;
		} else {
			actor.actAndWait(act);
			return BTState.RUNNING;
		}
	}

}
