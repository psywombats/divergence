/**
 *  ConditionHeroSpotted.java
 *  Created on Jan 29, 2013 11:29:35 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.conditions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Condition;
import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * True if hero is within sight range of the enemy, false otherwise.
 */
public class ConditionHeroSpotted extends Condition {
	
	protected static final int SIGHT_RANGE = 80; // in pixels
	
	public ConditionHeroSpotted(CharacterEvent actor) {
		super(actor);
	}

	@Override
	public boolean isMet() {
		return actor.distanceTo(RGlobal.hero) < SIGHT_RANGE; 
	}

}
