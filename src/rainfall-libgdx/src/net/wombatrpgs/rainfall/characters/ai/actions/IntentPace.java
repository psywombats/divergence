/**
 *  IntentPace.java
 *  Created on Jan 29, 2013 11:48:59 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.BehaviorList;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;

/**
 * Pace back and forth.
 */
public class IntentPace extends IntentAct {
	
	protected static final int PACE_RANGE = 200; // pixels
	
	protected enum PaceState { GOING_LEFT, GOING_RIGHT }
	protected PaceState state;

	public IntentPace(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
		state = PaceState.GOING_RIGHT;
	}

	@Override
	public void act() {
		if (!actor.isTracking()) {
			if (state == PaceState.GOING_LEFT) {
				state = PaceState.GOING_RIGHT;
				actor.targetLocation(actor.getX() + PACE_RANGE, actor.getY());
			} else {
				state = PaceState.GOING_LEFT;
				actor.targetLocation(actor.getX() - PACE_RANGE, actor.getY());
			}
		}
	}

}
