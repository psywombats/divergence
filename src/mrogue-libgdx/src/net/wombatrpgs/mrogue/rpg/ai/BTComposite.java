/**
 *  BTComposite.java
 *  Created on Oct 10, 2013 2:12:30 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Any node made up of multiple nodes!
 */
public abstract class BTComposite extends BTNode {
	
	// keep in mind this is ordered
	protected List<BTNode> children;
	protected BTNode current;

	/**
	 * Creates a new composite node for a specific actor.
	 * @param	actor			The character that will be performing the action
	 */
	public BTComposite(CharacterEvent actor) {
		super(actor);
		children = new ArrayList<BTNode>();
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (BTNode child : children) {
			child.update(elapsed);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.ai.BTNode#reset()
	 */
	@Override
	public void reset() {
		for (BTNode child : children) {
			child.reset();
		}
	}
	
	/**
	 * Adds a new child to the end of this composite. It will be accessed last
	 * during a traversal.
	 * @param	child			The child to add
	 */
	public void addChild(BTNode child) {
		children.add(0, child);
	}
	
	/**
	 * Internally sets the running node. Call once per select/act.
	 * @param	node			The currently running node in the subtree
	 */
	protected void updateRunning(BTNode node) {
		if (node != current) {
			if (current != null) {
				current.reset();
			}
			current = node;
		}
	}
	
}
