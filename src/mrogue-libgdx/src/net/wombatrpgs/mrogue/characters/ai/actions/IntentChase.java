/**
 *  IntentChase.java
 *  Created on Jan 30, 2013 12:07:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.actions;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.BehaviorList;
import net.wombatrpgs.mrogue.characters.ai.IntentAct;
import net.wombatrpgs.mrogue.core.MGlobal;

/**
 * Charge the hero.
 */
public class IntentChase extends IntentAct {

	public IntentChase(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
	}

	@Override
	public void act() {
		actor.targetLocation(MGlobal.hero.getX(), MGlobal.hero.getY());
	}

}
