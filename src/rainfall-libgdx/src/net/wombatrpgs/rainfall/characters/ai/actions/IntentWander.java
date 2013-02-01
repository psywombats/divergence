/**
 *  IntentPace.java
 *  Created on Jan 29, 2013 11:48:59 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.DirVector;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;

/**
 * Randomly choose destinations every once in a while.
 */
public class IntentWander extends IntentAct {
	
	protected static final int PACE_RANGE = 128; // pixels
	protected static final float WAIT_MIN = 4f; // seconds
	protected static final float WAIT_MAX = 7f; // seconds
	
	protected boolean needsRecalc;

	public IntentWander(CharacterEvent actor) {
		super(actor);
		this.needsRecalc = true;
	}

	@Override
	public void act() {
		
		if (!needsRecalc) return;
		needsRecalc = false;
		
		Direction dir = null;
		switch (RGlobal.rand.nextInt(4)) {
		case 0: dir = Direction.DOWN; break;
		case 1: dir = Direction.UP; break;
		case 2: dir = Direction.LEFT; break;
		case 3: dir = Direction.RIGHT; break;
		}
		DirVector vec = dir.getVector();
		int targetX = actor.getX() + vec.x * PACE_RANGE;
		int targetY = actor.getY() + vec.y * PACE_RANGE;
		actor.targetLocation(targetX, targetY);
		
		float duration = RGlobal.rand.nextFloat() * (WAIT_MAX - WAIT_MIN) + WAIT_MIN;
		new TimerObject(duration, actor, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				needsRecalc = true;
			}
		});

	}

}
