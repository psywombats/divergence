/**
 *  IntentPace.java
 *  Created on Jan 29, 2013 11:48:59 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai.actions;

import net.wombatrpgs.mrogue.characters.CharacterEvent;
import net.wombatrpgs.mrogue.characters.ai.BehaviorList;
import net.wombatrpgs.mrogue.characters.ai.IntentAct;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.objects.TimerListener;
import net.wombatrpgs.mrogue.maps.objects.TimerObject;
import net.wombatrpgs.mrogueschema.maps.data.DirVector;
import net.wombatrpgs.mrogueschema.maps.data.Direction;

/**
 * Randomly choose destinations every once in a while.
 */
public class IntentWander extends IntentAct {
	
	protected static final int PACE_RANGE = 128; // pixels
	protected static final float WAIT_MIN = 4f; // seconds
	protected static final float WAIT_MAX = 7f; // seconds
	
	public IntentWander(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
		this.needsRecalc = true;
	}
	
	protected boolean needsRecalc;

	@Override
	public void act() {
		
		if (!needsRecalc) return;
		needsRecalc = false;
		
		Direction dir = null;
		switch (MGlobal.rand.nextInt(4)) {
		case 0: dir = Direction.DOWN; break;
		case 1: dir = Direction.UP; break;
		case 2: dir = Direction.LEFT; break;
		case 3: dir = Direction.RIGHT; break;
		}
		DirVector vec = dir.getVector();
		float targetX = actor.getX() + (int) vec.x * PACE_RANGE;
		float targetY = actor.getY() + (int) vec.y * PACE_RANGE;
		actor.targetLocation(targetX, targetY);
		
		float duration = MGlobal.rand.nextFloat() * (WAIT_MAX - WAIT_MIN) + WAIT_MIN;
		new TimerObject(duration, actor, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				needsRecalc = true;
			}
		});

	}

}
