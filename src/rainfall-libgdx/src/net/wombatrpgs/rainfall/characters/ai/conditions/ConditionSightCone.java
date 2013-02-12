/**
 *  ConditionSightCone.java
 *  Created on Feb 11, 2013 9:55:00 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.conditions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Condition;
import net.wombatrpgs.rainfall.core.RGlobal;

/**
 * True when the enemy is facing the hero, generally
 */
public class ConditionSightCone extends Condition {
	
	protected static final int CONE_LENGTH = 480;
	protected static final float CONE_SLOPE = .3f; // in the 1st quad, right

	public ConditionSightCone(CharacterEvent actor) {
		super(actor);
	}

	@Override
	public boolean isMet() {
		float dist = actor.distanceTo(RGlobal.hero);
		if (dist > CONE_LENGTH) {
			return false;
		}
		float dx = actor.getX() - RGlobal.hero.getX();
		float dy = actor.getY() - RGlobal.hero.getY();
		float slope;
		if (Math.abs(dx) > Math.abs(dy)) {
			slope = dy / dx;
		} else {
			slope = dx / dy;
		}
		return Math.abs(slope) < CONE_SLOPE;
	}

}
