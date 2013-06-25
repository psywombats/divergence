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
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfallschema.maps.data.DirVector;

/**
 * Randomly choose destinations every once in a while. Like, actually random.
 */
public class IntentWanderOrganic extends IntentAct {
	
	protected static final int PACE_RANGE = 128; // pixels
	protected static final float WAIT_MIN = 1.0f; // seconds
	protected static final float WAIT_MAX = 2.0f; // seconds
	
	protected boolean needsRecalc;

	public IntentWanderOrganic(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
		this.needsRecalc = true;
	}

	@Override
	public void act() {
		
		if (!needsRecalc) return;
		needsRecalc = false;
		
		float a = (float) (RGlobal.rand.nextFloat() * 2*Math.PI);
		DirVector vec = new DirVector((float) Math.cos(a), (float) Math.sin(a));
		int targetX = (int) (actor.getX() + vec.x * PACE_RANGE);
		int targetY = (int) (actor.getY() + vec.y * PACE_RANGE);
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
