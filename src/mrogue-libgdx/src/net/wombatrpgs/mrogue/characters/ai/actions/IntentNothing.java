/**
 *  IntentNothing.java
 *  Created on Feb 12, 2013 10:15:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.actions;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.BehaviorList;
import net.wombatrpgs.mrogue.characters.ai.IntentAct;

/**
 * Does not interfere with velocity.
 */
public class IntentNothing extends IntentAct {

	public IntentNothing(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
	}

	@Override
	public void act() {
		// HUAHUAFHSDJ for real
	}

}
