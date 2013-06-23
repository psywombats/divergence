/**
 *  IntentChase.java
 *  Created on Jan 30, 2013 12:07:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.BehaviorList;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * Charge the hero.
 */
public class IntentChase extends IntentAct {

	public IntentChase(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
	}

	@Override
	public void act() {
		actor.targetLocation(RGlobal.hero.getX(), RGlobal.hero.getY());
	}

}
