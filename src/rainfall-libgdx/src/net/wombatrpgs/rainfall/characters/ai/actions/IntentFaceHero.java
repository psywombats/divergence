/**
 *  IntentFaceHero.java
 *  Created on Feb 11, 2013 10:10:59 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * Faces in a cardinal direction towards the hero.
 */
public class IntentFaceHero extends IntentAct {

	public IntentFaceHero(CharacterEvent actor) {
		super(actor);
	}

	@Override
	public void act() {
		actor.faceToward(RGlobal.hero);
	}

}
