/**
 *  IntentActSitStill.java
 *  Created on Jan 29, 2013 11:36:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.actions;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.BehaviorList;
import net.wombatrpgs.mrogue.characters.ai.IntentAct;

/**
 * Halt.
 */
public class IntentHalt extends IntentAct {

	public IntentHalt(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.characters.ai.IntentAct#act()
	 */
	@Override
	public void act() {
		// HURFADRUFFFASDFA
		actor.halt();
	}

}
