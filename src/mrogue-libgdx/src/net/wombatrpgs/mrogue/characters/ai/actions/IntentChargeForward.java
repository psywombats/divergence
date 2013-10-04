/**
 *  IntentChargeForward.java
 *  Created on Feb 28, 2013 5:04:18 PM for project rainfall-libgdx
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

/**
 * Charge in a cardinal direction.
 */
public class IntentChargeForward extends IntentAct {
	
	protected static float CHARGE_TIME = 2.0f; // in s
	
	protected boolean finishedCharging;
	
	public IntentChargeForward(BehaviorList parent, CharacterEvent actor) {
		super(parent, actor);
		this.finishedCharging = false;
	}


	@Override
	public void act() {
		
		if (finishedCharging) {
			parent.setBusy(null);
			finishedCharging = false;
		} else if (!actor.isTracking()) {
			actor.faceToward(MGlobal.hero);
			//actor.targetDirection(actor.getFacing());
			
			float duration = CHARGE_TIME;
			new TimerObject(duration, actor, new TimerListener() {
				@Override
				public void onTimerZero(TimerObject source) {
					finishedCharging = true;
				}
			});
			parent.setBusy(this);
		}
	}

}
