/**
 *  ActRoll.java
 *  Created on Apr 11, 2013 10:58:04 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.RollMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Rolls in whatever direction. Whatever.
 */
public class ActRoll extends MovesetAct {
	
	protected RollMDO mdo;

	/**
	 * Inherited constructor. Make move from data.
	 * @param 	actor			The character doing the acting
	 * @param 	mdo				The data to construct from
	 */
	public ActRoll(CharacterEvent actor, RollMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#coreAct
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, final CharacterEvent actor) {
		if (actor.isMoveActive(this)) return;
		actor.startAction(this);
		if (sfx != null) sfx.play();
		if (walkingAppearance == null) {
			RGlobal.reporter.warn("Can't roll and be invisible.");
			return;
		}
		
		Direction dir = actor.getFacing();
		
		// kick
		float newVX = actor.getVX() + dir.getVector().x * mdo.kick;
		float newVY = actor.getVY() + dir.getVector().y * mdo.kick;
		actor.setVelocity(newVX, newVY);
		
		// acceleration
		float duration = walkingAppearance.getDuration();
		float rate = (float) mdo.length / duration;
		float targetVX = dir.getVector().x * rate;
		float targetVY = dir.getVector().y * rate;
		actor.targetVelocity(targetVX, targetVY);
		
		final ActRoll parentRoll = this;
		new TimerObject(duration, actor, new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				actor.stopAction(parentRoll);
			}
		});
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#getType()
	 */
	@Override
	public MoveType getType() {
		return MoveType.MOVEMENT;
	}

}
