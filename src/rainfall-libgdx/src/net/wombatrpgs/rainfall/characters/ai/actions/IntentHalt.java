/**
 *  IntentActSitStill.java
 *  Created on Jan 29, 2013 11:36:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;

/**
 * Halt.
 */
public class IntentHalt extends IntentAct {
	
	public IntentHalt(CharacterEvent actor) {
		super(actor);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.ai.IntentAct#act()
	 */
	@Override
	public void act() {
		// HURFADRUFFFASDFA
		actor.halt();
	}

}
